package com.writesmith.core.endpoints;

import com.writesmith.Constants;
import com.writesmith.common.exceptions.PreparedStatementMissingArgumentException;
import com.writesmith.database.DBManager;
import com.writesmith.database.managers.ChatDBManager;
import com.writesmith.database.managers.ConversationDBManager;
import com.writesmith.database.managers.User_AuthTokenDBManager;
import com.writesmith.common.exceptions.AutoIncrementingDBObjectExistsException;
import com.writesmith.common.exceptions.CapReachedException;
import com.writesmith.common.exceptions.DBObjectNotFoundFromQueryException;
import com.writesmith.core.generation.GenerationGrantor;
import com.writesmith.model.database.Sender;
import com.writesmith.model.database.objects.*;
import com.writesmith.model.generation.objects.GrantedGeneratedChat;
import com.writesmith.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.writesmith.model.http.client.openaigpt.exception.OpenAIGPTException;
import com.writesmith.model.http.server.ResponseStatus;
import com.writesmith.model.http.server.request.GetChatRequest;
import com.writesmith.model.http.server.response.BodyResponse;
import com.writesmith.model.http.server.response.GetChatResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class GetChatEndpoint {

    private static final String[] responses = {"I'd love to keep chatting, but my program uses a lot of computer power. Please upgrade to unlock unlimited chats.",
            "Thank you for chatting with me. To continue, please upgrade to unlimited chats.",
            "I hope I was able to help. If you'd like to keep chatting, please subscribe for unlimited chats. There's a 3 day free trial!",
            "You are appreciated. You are loved. Show us some support and subscribe to keep chatting.",
            "Upgrade today for unlimited chats and a free 3 day trial!"};

    public static BodyResponse getChat(GetChatRequest request) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, IllegalAccessException, DBObjectNotFoundFromQueryException, OpenAIGPTException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, PreparedStatementMissingArgumentException, AppleItunesResponseException {

        // Get User_AuthToken for userID
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Create conversation object and it conversationID is null get by creating in database
        Conversation conversation;
        if (request.getConversationID() == null) {
            conversation = createConversationInDB(u_aT.getUserID());
        } else {
            // Get conversation by id, use the first in the list TODO: Is it okay to cast here, or should this all just be done in ConversationDBManager?
            conversation = ConversationDBManager.getFirstByPrimaryKey(request.getConversationID());

            // Create conversation if conversation is null or there's a userID mismatch with the received conversation
            if (conversation == null || !conversation.getUserID().equals(u_aT.getUserID())) {
                conversation = createConversationInDB(u_aT.getUserID());
            }

        }

        // Save chat to database by createInDB
        ChatDBManager.createInDB(
                conversation.getID(),
                Sender.USER,
                request.getInputText(),
                LocalDateTime.now()
        );

        // Create body response
        GetChatResponse getChatResponse;
        ResponseStatus responseStatus;

        try {
            // Generate chat with conversation
            GrantedGeneratedChat grantedGeneratedChat = GenerationGrantor.generateFromConversationIfPermitted(conversation);


            // Save the Chat
            DBManager.deepInsert(grantedGeneratedChat.getGeneratedChat(), true);

            // Calculate remaining TODO: this is a second DB call for countToday's chats, should this be combined?

            // Set the responseStatus as successful
            responseStatus = ResponseStatus.SUCCESS;

            //TODO: Fix this in the iOS app or something, since it deosn't like the null
            long remainingNotNull = -1;
            if (grantedGeneratedChat.getRemaining() != null) remainingNotNull = grantedGeneratedChat.getRemaining();

            //TODO: - This needs to be fixed! It seems like the \n\n prefix was removed from openAI, so adding it back here to ensure iOS app functionality
            String aiChatTextResponse = grantedGeneratedChat.getGeneratedChat().getChat().getText();
            int maxContainsSearchLength = 8;
            if (aiChatTextResponse.length() >= maxContainsSearchLength && !aiChatTextResponse.substring(0, maxContainsSearchLength).contains("\n\n"))
                aiChatTextResponse = "\n\n" + aiChatTextResponse;

            // Construct and return the GetChatResponse
            getChatResponse = new GetChatResponse(aiChatTextResponse, grantedGeneratedChat.getGeneratedChat().getFinish_reason(), conversation.getID(), remainingNotNull);

        } catch (CapReachedException e) {
            // If the cap was reached, then respond with ResponseStatus.CAP_REACHED_ERROR and cap reached response

            int randomIndex = new Random().nextInt(responses.length - 1);

            // Get random aiChatTextResponse from array
            String aiChatTextResponse = responses[randomIndex];

            // Set response status to cap reached error
            responseStatus = ResponseStatus.CAP_REACHED_ERROR;

            // Set the getChatResponse with the random response, null finish reason, and 0 remaining since the cap was reached TODO: Is this 0 fine here?
            getChatResponse = new GetChatResponse(aiChatTextResponse, null, null, 0l);
        }

        // Create body response with responseStatus TODO: This status should be in getChatResponse so that bodyResponse can be assembled by Server
        BodyResponse bodyResponse = new BodyResponse(responseStatus, getChatResponse);

        return bodyResponse;

    }

    private static Conversation createConversationInDB(Integer userID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return ConversationDBManager.createInDB(userID, Constants.DEFAULT_BEHAVIOR);
    }

}