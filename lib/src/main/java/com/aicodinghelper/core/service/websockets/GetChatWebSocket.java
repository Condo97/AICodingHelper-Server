package com.aicodinghelper.core.service.websockets;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.apple.iapvalidation.TransactionPersistentAppleUpdater;
import com.aicodinghelper.core.service.response.StatusResponse;
import com.aicodinghelper.core.service.response.factory.StatusResponseFactory;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.util.TokenCounter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.model.OAIClient;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequestStreamOptions;
import com.oaigptconnector.model.response.chat.completion.stream.OpenAIGPTChatCompletionStreamResponse;
import com.aicodinghelper.Constants;
import com.aicodinghelper.exceptions.CapReachedException;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.exceptions.responsestatus.InvalidAuthenticationException;
import com.aicodinghelper.exceptions.responsestatus.MalformedJSONException;
import com.aicodinghelper.exceptions.responsestatus.UnhandledException;
import com.aicodinghelper.database.dao.factory.ChatFactoryDAO;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.util.calculators.ChatRemainingCalculator;
import com.aicodinghelper.core.service.response.factory.BodyResponseFactory;
import com.aicodinghelper.keys.Keys;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.core.service.ResponseStatus;
import com.aicodinghelper.core.service.request.GetChatRequest;
import com.aicodinghelper.core.service.response.BodyResponse;
import com.aicodinghelper.core.service.response.ErrorResponse;
import com.aicodinghelper.core.service.response.GetChatStreamResponse;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@WebSocket(maxTextMessageSize = 1073741824)
public class GetChatWebSocket {

    private static final int tokenEstimationTolerance = Constants.Additional.tokenEstimationTolerance;

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(com.oaigptconnector.Constants.AI_TIMEOUT_MINUTES)).build(); // TODO: Is this fine to create here?


    /***
     * Connected
     *
     * Gets a chat from OpenAI using the given messages map array
     *
     * @param session Session for Spark WebSocket
     */
    @OnWebSocketConnect
    public void connected(Session session) {
//        System.out.println(session.getUpgradeRequest().getQueryString());

    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
//        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) {
//        System.out.println("Received: " + message);

        try {
            getChat(session, message);
        } catch (CapReachedException e) {
            // TODO: Cap reached stuff
        } catch (MalformedJSONException | InvalidAuthenticationException e) {
            // Print stack trace
            e.printStackTrace();

            // Create ErrorResponse with responseStatus and reason
            ErrorResponse errorResponse = new ErrorResponse(
                    e.getResponseStatus(),
                    e.getMessage()
            );

            try {
                session.getRemote().sendString(new ObjectMapper().writeValueAsString(errorResponse));
            } catch (IOException eI) {
                // This is just called if there was an issue writing errorResponse as a String, so I think just print the stack trace
                eI.printStackTrace();
            }
        } catch (UnhandledException e) {
            // Print stack trace
            e.printStackTrace();

            // Create ErrorResponse with responseStatus and reason
            ErrorResponse errorResponse = new ErrorResponse(
                    e.getResponseStatus(),
                    e.getMessage()
            );

            try {
                session.getRemote().sendString(new ObjectMapper().writeValueAsString(errorResponse));
            } catch (IOException eI) {
                // This is just called if there was an issue writing errorResponse as a String, so I think just print the stack trace
                eI.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: Is this ever called? I'm thinking maybe for nullPointerException or something like that
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    protected void getChat(Session session, String message) throws MalformedJSONException, InvalidAuthenticationException, UnhandledException, CapReachedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, UnrecoverableKeyException, AppStoreErrorResponseException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, InstantiationException {
//        System.out.println("Received message: " + message);

        /*** PARSE REQUEST ***/

        // Get start time
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime getAuthTokenTime;
        LocalDateTime getIsPremiumTime;
        LocalDateTime beforeStartStreamTime;
        AtomicReference<LocalDateTime> firstChatTime = new AtomicReference<>();

        // Read GetChatRequest, otherwise read GetChatLegacyRequest and adapt to GetChatRequest, otherwise just throw!
        GetChatRequest gcRequest;
        try {
            // Read GetChatRequest
            gcRequest = new ObjectMapper().readValue(message, GetChatRequest.class);
        } catch (IOException e) {
            // Print message, stacktrace, and throw MalformedJSONException with e as reason
            System.out.println("The message: " + message);
            e.printStackTrace();
            throw new MalformedJSONException(e, "Error parsing message: " + message);
        }

        // Get u_aT for userID
        User_AuthToken u_aT = null;
        try {
            u_aT = User_AuthTokenDAOPooled.get(gcRequest.getAuthToken());
        } catch (DBObjectNotFoundFromQueryException e) {
            // I'm pretty sure this is the only one that is called if everything is correct but the authToken is invalid, so throw an InvalidAuthenticationException
            throw new InvalidAuthenticationException(e, "Error authenticating user. Please try closing and reopening the app, or report the issue if it continues giving you trouble.");
        } catch (DBSerializerException | SQLException | InterruptedException | InvocationTargetException |
                 IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            // Pretty sure these are all setup stuff, so throw UnhandledException unless I see it throwing in the console :)
            throw new UnhandledException(e, "Error getting User_AuthToken for authToken. Please report this and try again later.");
        }

        getAuthTokenTime = LocalDateTime.now();

        // If OpenAIKey is included, set isUsingSelfServeOpenAIKey to true and openAIKey to it otherwise false and openAIKey to Keys.openAIKey
        boolean isUsingSelfServeOpenAIKey = gcRequest.getOpenAIKey() != null;
        String openAIKey = gcRequest.getOpenAIKey() != null ? gcRequest.getOpenAIKey() : Keys.openAiAPI;

        // If not using self serve openAIKey get estimated tokens and ensure user has enough tokens to generate the chat
        if (!isUsingSelfServeOpenAIKey) {
            /*** GET TOKENS REMAINING ***/

            // Get cooldown controlled apple updated most recent transaction
            Transaction mostRecentTransaction = TransactionPersistentAppleUpdater.getCooldownControlledAppleUpdatedMostRecentTransaction(u_aT.getUserID());

            // Get remainingTokens
            Long remainingTokens;
            try {
                remainingTokens = ChatRemainingCalculator.calculateRemaining(u_aT, mostRecentTransaction);
            } catch (DBSerializerException | InterruptedException | SQLException | InvocationTargetException |
                     IllegalAccessException | NoSuchMethodException | InstantiationException e) {
                // I'm pretty sure these are just setup stuff too so throw UnhandledException unless I see it throwing in the console :)
                throw new UnhandledException(e, "Error calculating remainingTokens. Please report this and try again later.");
            }

            // Get estimated tokens for chat with TokenCounter
            int estimatedTokens = TokenCounter.getTotalTokenCount(gcRequest.getChatCompletionRequest().getModel(), gcRequest.getChatCompletionRequest());

            // If remainingTokens is not null (infinite) and it minus estimatedTokens plus tolerance is less than 0, send cap reached response in GetChatResponse
            if (remainingTokens != null && remainingTokens - estimatedTokens + tokenEstimationTolerance <= 0) {
                /* TODO: Do the major annotations and rework all of this to be better lol */
                // Send BodyResponse with cap reached error and GetChatResponse with cap reached response
//            GetChatResponse gcr = new GetChatResponse(GetChatCapReachedResponses.getRandomResponse(), null, null, 0l); TODO: Reinstate this after new app build has been published so that it works properly and is cleaner here.. unless this implementation is better and the "limit" just needs to be a constant somewhere
                ResponseStatus rs = ResponseStatus.CAP_REACHED_ERROR;

                // Create StatusResponse with ResponseStatus
                StatusResponse sr = StatusResponseFactory.createStatusResponse(rs);

                // Send StatusResponse
                try {
                    session.getRemote().sendString(new ObjectMapper().writeValueAsString(sr));
                } catch (IOException e) {
                    // I'm pretty sure this happens if the socket connection is just invalid, so maybe I'll handle it differently in the future but for now throw UnhandledException unless I see it throwing in the console :)
                    throw new UnhandledException(e, "Error sending body response to socket connection. Please report this and try again later.");
                }

                // Print stream chat generation cap reached TODO: Move this and make logging better
                System.out.println("Chat Stream Cap Reached " + new SimpleDateFormat("HH:mm:ss").format(new Date()));

                throw new CapReachedException("Chat cap reached for user. Please upgrade to continue.");
            }
        }

        // Get and chat completion request stream_options include usage to true
        OAIChatCompletionRequest chatCompletionRequest = gcRequest.getChatCompletionRequest();
        OAIChatCompletionRequestStreamOptions streamOptions = gcRequest.getChatCompletionRequest().getStream_options(); // Get the stream options from the chat completion request in case there are more added and the user has defined them
        if (streamOptions == null)
            streamOptions = new OAIChatCompletionRequestStreamOptions(true); // If user has not included stream options create them with include usage set to true
        else
            streamOptions.setInclude_usage(true); // If user has included stream options set include usage to true
        chatCompletionRequest.setStream_options(new OAIChatCompletionRequestStreamOptions(true));

        // Create stream set to null
        Stream<String> chatStream = null;

        // Do stream request with OpenAI right here for now TODO:
        try {
            chatStream = OAIClient.postChatCompletionStream(chatCompletionRequest, openAIKey, httpClient);
        } catch (IOException e) {
            // I think this is called if the chat stream is closed at any point including when it normally finishes, so just do nothing for now... If so, these should be combined and the print should be removed and I think it's just fine lol.. Actually these may not be called unless there is an error establishing a connection.. So maybe just throw UnhandledException unless I see it throwing in the console
            System.out.println("CONNECTION CLOSED (IOException)");
            throw new UnhandledException(e, "Connection closed during chat stream. Please report this and try again later.");
        } catch (InterruptedException e) {
            // I think this is called if the chat stream is closed at any point including when it normally finishes, so just do nothing for now... If so, these should be combined and the print should be removed and I think it's just fine lol.. Actually these may not be called unless there is an error establishing a connection.. So maybe just throw UnhandledException unless I see it throwing in the console
            System.out.println("CONNECTION CLOSED (InterruptedException)");
            throw new UnhandledException(e, "Connection closed during chat stream. Please report this and try again later.");
        }


        /*** GENERATION - Begin ***/

        // Create completionTokens and promptTokens
        AtomicReference<Integer> completionTokens = new AtomicReference<>(0);
        AtomicReference<Integer> promptTokens = new AtomicReference<>(0);

        // Parse OpenAIGPTChatCompletionStreamResponse then convert to GetChatResponse and send it in BodyResponse as response :-)
        chatStream.forEach(response -> {
//            System.out.println(response);
            try {
                // Trim "data: " off of response TODO: Make this better lol
                final String dataPrefixToRemove = "data: ";
                if (response.length() >= dataPrefixToRemove.length() && response.substring(0, dataPrefixToRemove.length()).equals(dataPrefixToRemove))
                    response = response.substring(dataPrefixToRemove.length(), response.length());

                // Get response as JsonNode
                JsonNode responseJSON = new ObjectMapper().readValue(response, JsonNode.class);

//                System.out.println("RESPONSE: " + response);

                // Get responseJSON as OpenAIGPTChatCompletionStreamResponse
                OpenAIGPTChatCompletionStreamResponse streamResponse = new ObjectMapper().treeToValue(responseJSON, OpenAIGPTChatCompletionStreamResponse.class);

                // Create gcResponse with streamResponse
                GetChatStreamResponse gcResponse = new GetChatStreamResponse(
                        streamResponse
                );

                // Create success BodyResponse with getChatResponse
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(gcResponse);

                // Send BodyResponse
                session.getRemote().sendString(new ObjectMapper().writeValueAsString(br));

                // Once received and also only once, set completion_tokens and prompt_tokens for userID TODO: This is more readable than a big conditional, but what would be even more readable than this? Like is there a more concise way to do null checks here?
                if (completionTokens.get() == 0)
                    if (streamResponse.getUsage() != null)
                        if (streamResponse.getUsage().getCompletion_tokens() != null)
                            if (streamResponse.getUsage().getCompletion_tokens() > 0)
                                completionTokens.compareAndSet(0, streamResponse.getUsage().getCompletion_tokens()); // CompareAndSet helps with thread safety, and the 0 is the expected value since that is the initial or empty value
                if (promptTokens.get() == 0)
                    if (streamResponse.getUsage() != null)
                        if (streamResponse.getUsage().getPrompt_tokens() != null)
                            if (streamResponse.getUsage().getPrompt_tokens() > 0)
                                promptTokens.compareAndSet(0, streamResponse.getUsage().getPrompt_tokens()); // CompareAndSet helps with thread safety, and the 0 is the expected value since that is the initial or empty value

            } catch (JsonMappingException | JsonParseException e) {
                // TODO: If cannot map response as JSON, skip for now, this is fine as there is only one format for the response as far as I know now

            } catch (IOException e) {
                // TODO: This is only called in this case if ObjectMapper does not throw a JsonMappingException or JsonParseException, but it is thrown in the same methods that call those, so it's okay for now for the same reason

            }
        });

        // Close chatStream
        chatStream.close();


        /*** FINISH UP ***/

        // If not isUsingSelfServeOpenAIKey, Insert Chat in DB
        if (!isUsingSelfServeOpenAIKey) {
            ChatFactoryDAO.create(
                    u_aT.getUserID(),
                    completionTokens.get(),
                    promptTokens.get()
            );
        }

//        // Print log to console
//        printStreamedGeneratedChatDoBetterLoggingLol(gc, purifiedOAIChatCompletionRequest.getRequest(), isPremium, startTime, getAuthTokenTime, getIsPremiumTime, beforeStartStreamTime, firstChatTime);
    }

    protected static void dothestreamtesting() {
        URI uri = null;
        try {
            uri = new URI("https://api.openai.com/v1/chat/completions");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String postString = """
                {
                    "model": "gpt-4",
                    "messages": [{"role": "user", "content": "a totally unique mythology about an ocean"}],
                    "stream": true,
                    "temperature": 0.7
                }
                """;
        //"messages": [{"role": "user", "content": "a totally unique mythology about an ocean"}],
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer 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")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postString))
                .build();

        Stream<String> lines = null;
        try {
            lines = client.send(request, HttpResponse.BodyHandlers.ofLines()).body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        lines.forEach(text -> {
//            System.out.println(text);
////            sessions.forEach(session -> {
////                try {
////                    session.getRemote().sendString(text);
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
////            });
//        });

    }

//    // TODO: Better logging lol
//    private void printStreamedGeneratedChatDoBetterLoggingLol(GeneratedChat gc, OAIChatCompletionRequest completionRequest, Boolean isPremium, LocalDateTime startTime, LocalDateTime getAuthTokenTime, LocalDateTime getIsPremiumTime, LocalDateTime beforeStartStreamTime, AtomicReference<LocalDateTime> firstChatTime) {
//        final int maxLength = 40;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date();
//
//        String output = gc.getChat().getText();
//
//        int charsInCompletionRequest = 0;
//        for (OAIChatCompletionRequestMessage message: completionRequest.getMessages())
//            for (OAIChatCompletionRequestMessageContent content: message.getContent())
//                switch (content.getType()) {
//                    case TEXT -> charsInCompletionRequest += ((OAIChatCompletionRequestMessageContentText)content).getText().length();
//                    // TODO: Image and ImageURL maybe
//                }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Chat ");
//        sb.append(gc.getChat().getChat_id());
//        sb.append(" Streamed ");
//        sb.append(sdf.format(date));
//        if (output != null) {
//            sb.append("\t");
//            sb.append(output.length() >= maxLength ? output.substring(0, maxLength) : output);
//            sb.append("... ");
//            sb.append(output.length() + charsInCompletionRequest);
//            sb.append(" total chars");
//        }
//
//        // Append isPremium to sb if isPremium is true
//        if (isPremium) {
//            sb.append(", is premium");
//        }
//
//        // Get current time
//        LocalDateTime now = LocalDateTime.now();
//
//        // Append difference from startTime to getAuthTokenTime to sb
//        if (getAuthTokenTime != null && startTime != null) {
//            long startToAuthTokenMS = Duration.between(startTime, getAuthTokenTime).toMillis();
//            sb.append(", Start to authToken: " + startToAuthTokenMS + "ms");
//        }
//
//        // Append difference from getAuthTokenTime to getIsPremiumTime
//        if (getIsPremiumTime != null && getAuthTokenTime != null) {
//            long authTokenToIsPremiumMS = Duration.between(getAuthTokenTime, getIsPremiumTime).toMillis();
//            sb.append(", to isPremium: " + authTokenToIsPremiumMS + "ms");
//        }
//
//        // Append difference from getIsPremiumTime to beforeStartStreamTime
//        if (beforeStartStreamTime != null && getIsPremiumTime != null) {
//            long isPremiumToStartStreamMS = Duration.between(getIsPremiumTime, beforeStartStreamTime).toMillis();
//            sb.append(", to before stream start: " + isPremiumToStartStreamMS + "ms");
//        }
//
//        // Append difference from beforeStreamStartTime to firstChatTime
//        if (firstChatTime != null && firstChatTime.get() != null && beforeStartStreamTime != null) {
//            long startStreamToFirstChatMS = Duration.between(beforeStartStreamTime, firstChatTime.get()).toMillis();
//            sb.append(", to first chat: " + startStreamToFirstChatMS + "ms");
//        }
//
//        // Append difference in seconds from start time to first chat generated to sb
//        if (firstChatTime != null && firstChatTime.get() != null && startTime != null) {
//            long startToFirstChatSeconds = Duration.between(startTime, firstChatTime.get()).toSeconds();
//            sb.append(", first chat took " + startToFirstChatSeconds + " seconds,");
//        }
//
//        if (now != null && startTime != null) {
//            long startToFinishGeneratingSeconds = Duration.between(startTime, now).toSeconds();
//            sb.append(" complete generation took " + startToFinishGeneratingSeconds + " seconds.");
//        }
//
//        System.out.println(sb.toString());
//    }

}
