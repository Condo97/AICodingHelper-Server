package com.aicodinghelper.core;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.aicodinghelper.core.service.request.*;
import com.aicodinghelper.core.service.response.factory.BodyResponseFactory;
import com.aicodinghelper.exceptions.*;
import com.aicodinghelper.exceptions.responsestatus.MalformedJSONException;
import com.aicodinghelper.core.service.endpoints.*;
import com.aicodinghelper.core.service.ResponseStatus;
import com.aicodinghelper.core.service.response.BodyResponse;
import com.aicodinghelper.core.service.response.StatusResponse;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import spark.Request;
import spark.Response;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class Server {

    /***
     * Calculate Tokens
     *
     * Calculates the tokens for the input.
     *
     * Request: {
     *     authToken: String - Authentication token generated from registerUser
     *     model: String - The model to use when calculating the tokens for input
     *     input: String - The input to calculate the tokens for
     * }
     *
     * Response: {
     *     Body: {
     *         tokens: Integer - The estimated count of tokens for the input
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param req Request object given by Spark
     * @param res Response object given by Spark
     * @return Value of JSON response as String
     */
    public static String calculateTokens(Request req, Response res) throws IOException, MalformedJSONException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, URISyntaxException {
        CalculateTokensRequest ctRequest;

        try {
            ctRequest = new ObjectMapper().readValue(req.body(), CalculateTokensRequest.class);
        } catch (JsonMappingException | JsonParseException e) {
            System.out.println("Exception when Getting Remaining Tokens... The request: " + req.body());
            e.printStackTrace();
            throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
        }

        BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(
                CalculateTokensEndpoint.calculateTokens(ctRequest)
        );

        return new ObjectMapper().writeValueAsString(br);
    }

    /***
     * Function Call
     *
     * Performs the given function call and returns the response.
     *
     * Request: {
     *     authToken: String - Authentication token generated from registerUser
     *     model: String - The GPT model to use
     *     input: String - The input given
     * }
     *
     * Response: {
     *     response: OAIGPTChatCompletionResponse - The response given by OpenAI GPT's server to the function call
     * }
     *
     * @param req Request object given by Spark
     * @param res Response object given by Spark
     * @return Value of JSON response as String
     */
    public static String functionCall(Request req, Response res, Class<?> fcClass) throws IOException, MalformedJSONException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OAISerializerException, OpenAIGPTException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        FunctionCallRequest fcRequest;

        try {
            fcRequest = new ObjectMapper().readValue(req.body(), FunctionCallRequest.class);
        } catch (JsonMappingException | JsonParseException e) {
            System.out.println("Exception when Getting Remaining Tokens... The request: " + req.body());
            e.printStackTrace();
            throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
        }

        BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(
                FunctionCallEndpoint.functionCall(fcRequest, fcClass)
        );

        return new ObjectMapper().writeValueAsString(br);
    }

    /***
     * Register User
     *
     * Registers a user to the database. This is a blank POST request and may be changed to a GET in the future.
     *
     * Request: {
     *
     * }
     *
     * Response: {
     *     Body: {
     *         authToken: String - Authentication token generated by the server
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON response as String
     */
    public static String registerUser(Request request, Response response) throws SQLException, SQLGeneratedKeyException, PreparedStatementMissingArgumentException, IOException, DBSerializerPrimaryKeyMissingException, DBSerializerException, AutoIncrementingDBObjectExistsException, IllegalAccessException, InterruptedException, InvocationTargetException {

        BodyResponse bodyResponse = RegisterUserEndpoint.registerUser();

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    public static Object registerTransaction(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreErrorResponseException, UnrecoverableKeyException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException {
        // Parse the request
        RegisterTransactionRequest rtr = new ObjectMapper().readValue(request.body(), RegisterTransactionRequest.class);

        BodyResponse bodyResponse = RegisterTransactionEndpoint.registerTransaction(rtr);

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    /***
     * Submit Feedback
     *
     * Stores feedback :)
     *
     * Request: {
     *     authToken: String - Authentication token, generated from registerUser
     *     feedback: String - The feedback
     * }
     *
     * Response: {
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON represented as String
     */
    public static Object submitFeedback(Request request, Response response) throws IOException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get feedbackRequest
        FeedbackRequest feedbackRequest = new ObjectMapper().readValue(request.body(), FeedbackRequest.class);

        StatusResponse sr = SubmitFeedbackEndpoint.submitFeedback(feedbackRequest);

        return new ObjectMapper().writeValueAsString(sr);
    }


    /***
     * Get Remaining Tokens
     *
     * Gets the amount of tokens remaining for the user in the current period for their subscription tier.
     *
     * Request: {
     *     authToken: String - Authentication token, generated from registerUser
     * }
     *
     * Response: {
     *     Body: {
     *         remainingTokens: Integer - The amount of tokens remaining for the user for their tier
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON represented as String
     */
    public static Object getRemainingTokens(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, AppStoreErrorResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, MalformedJSONException {
        AuthRequest aRequest;

        try {
            aRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);
        } catch (JsonMappingException | JsonParseException e) {
            System.out.println("Exception when Getting Remaining Tokens... The request: " + request.body());
            e.printStackTrace();
            throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
        }

        BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(
                GetRemainingTokensEndpoint.getRemainingTokens(aRequest)
        );

        return new ObjectMapper().writeValueAsString(br);
    }


    // --------------- //

    public static String getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus status) {

        //TODO: - This is the default implementation that goes along with the app... This needs to be put as legacy and a new way of handling errors needs to be developed!
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode bodyNode = mapper.createObjectNode();
        bodyNode.put("output", "There was an issue getting your chat. Please try again..."); // Move this!
        bodyNode.put("remaining", -1);
        bodyNode.put("finishReason", "");

        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("Success", ResponseStatus.SUCCESS.Success);
        baseNode.put("Body", bodyNode);

        return baseNode.toString();
//        return "{\"Success\":" + ResponseStatus.EXCEPTION_MAP_ERROR.Success + "}";
    }
}
