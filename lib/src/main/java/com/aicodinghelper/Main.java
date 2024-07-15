package com.aicodinghelper;

import com.aicodinghelper.openai.functioncall.PlanCodeGenerationFC;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.aicodinghelper.connectionpool.SQLConnectionPoolInstance;
import com.aicodinghelper.core.Server;
import com.aicodinghelper.core.service.websockets.GetChatWebSocket;
import com.aicodinghelper.core.service.ResponseStatus;
import com.aicodinghelper.keys.Keys;
import com.aicodinghelper.core.service.response.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import static spark.Spark.*;

public class Main {

    private static final String threadArgPrefix = "-t";
    private static final String connectionsArgPrefix = "-c";
    private static final String debugArg = "-debug";


    private static final int MAX_THREADS = 4;
    private static final int MIN_THREADS = 1;
    private static final int TIMEOUT_MS = 8000;

    private static final int DEFAULT_PORT = 9500;
    private static final int DEBUG_PORT = 9501;

    public static void main(String... args) throws SQLException {
        // Get threads from thread arg or MAX_THREADS
        int threads = parseArg(args, threadArgPrefix, MAX_THREADS);

        // Get connections from connections arg or double the threads
        int connections = parseArg(args, connectionsArgPrefix, threads * 2);

        // Get isDebug from debug arg
        boolean isDebug = argIncluded(args, debugArg);

        // Set up MySQL Driver
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Configure web sockets
        configureWebSockets();

//        WebSocketPolicy

        webSocketIdleTimeoutMillis(60 * 1000);

        // Set up SQLConnectionPoolInstance
        SQLConnectionPoolInstance.create(Constants.MYSQL_URL, Keys.MYSQL_USER, Keys.MYSQL_PASS, connections);

        // Set up Policy static file location
        staticFiles.location("/policies");

        // Set up Spark thread pool and port
        threadPool(threads, MIN_THREADS, TIMEOUT_MS);
        port(isDebug ? DEBUG_PORT : DEFAULT_PORT);

        // Set up SSL
        secure("chitchatserver.com.jks", Keys.sslPassword, null, null);

        // Set up https v1 path
        path("/v1", () -> configureHttpEndpoints());

        // Set up https dev path
        path("/dev", () -> configureHttpEndpoints(true));

        // Set up legacy / path, though technically I think configureHttp() can be just left plain there in the method without the path call
        configureHttpEndpoints();

        // Exception Handling
        exception(JsonMappingException.class, (error, req, res) -> {
            System.out.println("The request: " + req.body());
            error.printStackTrace();

            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.JSON_ERROR));
        });

        exception(OpenAIGPTException.class, (error, req, res) -> {
            System.out.println("The request: " + req.body());
            error.printStackTrace();

            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.OAIGPT_ERROR));
        });

        exception(IllegalArgumentException.class, (error, req, res) -> {
            System.out.println("The request: " + req.body());
            error.printStackTrace();

            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.ILLEGAL_ARGUMENT));
        });

        exception(Exception.class, (error, req, res) -> {
            System.out.println("The request: " + req.body());
            error.printStackTrace();

            res.body(Server.getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus.UNHANDLED_ERROR));
        });

        // Handle not found (404)
        notFound((req, res) -> {
            System.out.println("The request: " + req.body());
            System.out.println(req.uri() + " 404 Not Found!");

            System.out.println(activeThreadCount());
            res.status(404);
            return "<html><a href=\"" + Constants.SHARE_URL + "\">Download WriteSmith</a></html>";
        });
    }

    private static void configureWebSockets() {
        // TODO: Do constants and make this better :O
        /* v1 */
        final String v1Path = "/v1";

        webSocket(v1Path + Constants.URIs.GET_CHAT_STREAM_URI, GetChatWebSocket.class);

        /* dev */
        final String devPath = "/dev";

        webSocket(v1Path + Constants.URIs.GET_CHAT_STREAM_URI, GetChatWebSocket.class);
    }

    private static void configureHttpEndpoints() {
        configureHttpEndpoints(false);
    }

    private static void configureHttpEndpoints(boolean dev) {
        // POST Functions
        post(Constants.URIs.CALCULATE_TOKENS_URI, Server::calculateTokens);
        post(Constants.URIs.GET_IS_ACTIVE_URI, Server::getIsActive);
        post(Constants.URIs.GET_REMAINING_TOKENS_URI, Server::getRemainingTokens);
        post(Constants.URIs.REGISTER_USER_URI, Server::registerUser);
        post(Constants.URIs.REGISTER_TRANSACTION_URI, Server::registerTransaction);
        post(Constants.URIs.SUBMIT_FEEDBACK, Server::submitFeedback);

//        post(Constants.URIs.Function.CREATE_RECIPE_IDEA, Server.Func::createRecipeIdea);

        // Function Calls
        post(Constants.URIs.PLAN_CODE_GENERATION, (req, res) -> Server.functionCall(req, res, PlanCodeGenerationFC.class));

        // Get Constants
        post(Constants.URIs.GET_IMPORTANT_CONSTANTS_URI, (req, res) -> new ObjectMapper().writeValueAsString(new BodyResponse(ResponseStatus.SUCCESS, new GetImportantConstantsResponse())));

        // dev functions
        if (dev) {

        }
    }

    private static boolean argIncluded(String[] args, String arg) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(arg))
                return true;
        }

        return false;
    }

    private static String parseArg(String[] args, String argPrefix, String defaultValue) {
        for (int i = 0; i < args.length; i++) {
            if (argIncluded(args, argPrefix) && args.length > i + 1) {
                return args[i + 1];
            }
        }

        return defaultValue;
    }

    private static int parseArg(String[] args, String argPrefix, int defaultValue) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argPrefix) && args.length > i + 1) {
                try {
                    return Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Could not parse arg " + argPrefix + ", please make sure it is an int. Will use " + defaultValue + " instead.");
                }
            }
        }

        return defaultValue;
    }


}
