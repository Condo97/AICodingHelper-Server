package com.aicodinghelper;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.util.TokenCounter;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.aicodinghelper.core.service.endpoints.*;
import com.aicodinghelper.core.service.response.*;
import com.aicodinghelper.exceptions.AutoIncrementingDBObjectExistsException;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.connectionpool.SQLConnectionPoolInstance;
import com.aicodinghelper.database.dao.pooled.TransactionDAOPooled;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.keys.Keys;
import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.core.service.request.AuthRequest;
import com.aicodinghelper.core.service.request.RegisterTransactionRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

public class Tests {

    private static String authTokenRandom = "DQ4yr8KmudhBWzYPYQe4sM4PSveVrdyQEXOgnmVqMnNBE0NG6SopHgmlYjr2iwtLV5UK7MEf2RA4GCahqGzBHLmws2B+JCYpJ+Gi5wzmqOkfuO+0qJa6slGWnj8RGKO24qHFXe5e4ZDRN9sXpsjxes8YHBZrk92sXV7gYaSZ/3c=";

    @BeforeAll
    static void setUp() throws SQLException {
        SQLConnectionPoolInstance.create(Constants.MYSQL_URL, Keys.MYSQL_USER, Keys.MYSQL_PASS, 10);
    }

//    @Test
//    @DisplayName("Try creating a SELECT Prepared Statement")
//    void testSelectPreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            // Try complete Select PS
//            ComponentizedPreparedStatement cps = SelectComponentizedPreparedStatementBuilder.forTable("Chat").select("chat_id").select("user_id").where("user_text", SQLOperators.EQUAL, 5).limit(5).orderBy(OrderByComponent.Direction.DESC, "date").build();
//
//            PreparedStatement cpsPS = cps.connect(conn);
//            System.out.println(cpsPS.toString());
//            cpsPS.close();
//
//            // Try minimal Select PS
//            ComponentizedPreparedStatement selectCPSMinimal = SelectComponentizedPreparedStatementBuilder.forTable("Chat").build();
//
//            PreparedStatement selectCPSMinimalPS = selectCPSMinimal.connect(conn);
//            System.out.println(selectCPSMinimalPS.toString());
//            selectCPSMinimalPS.close();
//
//            // Try partial Select PS
//            ComponentizedPreparedStatement selectCPSPartial = SelectComponentizedPreparedStatementBuilder.forTable("Chat").select("chat_id").where("user_text", SQLOperators.EQUAL, false).build();
//
//            PreparedStatement selectCPSPartialPS = selectCPSPartial.connect(conn);
//            System.out.println(selectCPSPartialPS.toString());
//            selectCPSPartialPS.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    @Test
//    @DisplayName("Try creating an INSERT INTO Prepared Statement")
//    void testInsertIntoPreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            // Build the insert componentized statement
//            ComponentizedPreparedStatement insertCPSComplete = InsertIntoComponentizedPreparedStatementBuilder.forTable("Chat").addColAndVal("chat_id", Types.NULL).addColAndVal("user_id", 5).addColAndVal("user_text", "hi").addColAndVal("ai_text", "hello").addColAndVal("date", LocalDateTime.now()).build(true);
//
//            System.out.println(insertCPSComplete);
//
//            // Do update and get generated keys
//            List<Map<String, Object>> generatedKeys = DBClient.updateReturnGeneratedKeys(conn, insertCPSComplete);
//
//            System.out.println(generatedKeys);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    @Test
//    @DisplayName("Try creating an UPDATE Prepared Statement")
//    void testUpdatePreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            ComponentizedPreparedStatement updatePSComplete = UpdateComponentizedPreparedStatementBuilder.forTable("Chat").set("user_text", "wow!").set("date", LocalDateTime.now()).where("user_id", SQLOperators.EQUAL, 5).where("chat_id", SQLOperators.EQUAL, 65842).build();
//
//            DBClient.update(conn, updatePSComplete);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    @Test
//    @DisplayName("HttpHelper Testing")
//    void testBasicHttpRequest() {
//        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofMinutes(Constants.AI_TIMEOUT_MINUTES)).build();
//        OAIChatCompletionRequestMessage promptMessageRequest = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
//                .addText("write me a short joke")
//                .build();//new OAIChatCompletionRequestMessage(CompletionRole.USER, "write me a short joke");
//        OAIChatCompletionRequest promptRequest = OAIChatCompletionRequest.build("gpt-3.5-turbo", 100, 0.7, Arrays.asList(promptMessageRequest));
//        Consumer<HttpRequest.Builder> c = requestBuilder -> {
//            requestBuilder.setHeader("Authorization", "Bearer " + Keys.openAiAPI);
//        };
//
//        try {
//            OAIGPTChatCompletionResponse response = OAIClient.postChatCompletion(promptRequest, Keys.openAiAPI, client);
//            System.out.println(response.getChoices()[0].getMessage().getContent());
//
//        } catch (OpenAIGPTException e) {
//            System.out.println(e.getErrorObject().getError().getMessage());
//        } catch (IOException e) {
//             throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    @Test TODO: Reimplement
//    @DisplayName("Test Registering Transaction")
    void testTransactionValidation() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, AppStoreErrorResponseException, UnrecoverableKeyException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, InstantiationException {
        /* REGISTER TRANSACTION ENDPOINT */
        // Input
        final Long sampleTransactionId = 2000000351816446l;
        // Expected Results
        final AppStoreSubscriptionStatus expectedStatus = AppStoreSubscriptionStatus.EXPIRED;
        final Boolean expectedIsPremiumValue1 = false;

        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Get authToken
        String authToken = aResponse.getAuthToken();

        // Create register transaction request
        RegisterTransactionRequest rtr = new RegisterTransactionRequest(authToken, sampleTransactionId, null);

        // Register transaction
        BodyResponse registerTransactionBR = RegisterTransactionEndpoint.registerTransaction(rtr);
        IsActiveResponse ipr1 = (IsActiveResponse)registerTransactionBR.getBody();

        // Get User_AuthToken
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(authToken);

        // Verify transaction registered successfully
        Transaction transaction = TransactionDAOPooled.getMostRecent(u_aT.getUserID());
        assert(transaction != null);
        System.out.println(transaction.getAppstoreTransactionID() + " " + sampleTransactionId);
        assert(transaction.getAppstoreTransactionID().equals(sampleTransactionId));
//        assert(transaction.getStatus() == expectedStatus);

        // Verify registered transaction successfully got isPremium value
//        assert(ipr1.getIsPremium() == expectedIsPremiumValue1);

        /* IS PREMIUM ENDPOINT */
        // Expected Results
        final Boolean expectedIsPremiumValue2 = false;

        // Create authRequest
        AuthRequest aRequest = new AuthRequest(authToken, null);

        // Get Is Premium from endpoint TODO: Should there be an endpoint for getting the subscription tier? What would the data received be?
//        BodyResponse isPremiumBR = GetIsPremiumEndpoint.getIsPremium(aRequest);
//        IsPremiumResponse ipr2 = (IsPremiumResponse)isPremiumBR.getBody();

        // Verify results
//        assert(ipr2.getIsPremium() == expectedIsPremiumValue2);
    }

//    @Test
//    @DisplayName("Test Submit Feedback Endpoint")
//    void testSubmitFeedbackEndpoint() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        final FeedbackRequest feedbackRequest = new FeedbackRequest(
//                authTokenRandom,
//                "This is the feedback"
//        );
//
//        StatusResponse sr = SubmitFeedbackEndpoint.submitFeedback(feedbackRequest);
//
//        assert(sr != null);
//    }

    @Test
    @DisplayName("Test Token Counter")
    void testTokenCounter() throws IOException, URISyntaxException {
        String input = "\nComriotis/Library/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic toy/Containers/com.acapplications.AICodingHelper/Data/Downloads/files_temp/FileSystemView_temp1.swift\nContent:\n\n---------------------------------\nPath: /Usren()\n        \n        // Reapply the previously expanded paths to the new root node.\n        rootNode.applyExpandedPaths(expandedPaths)\n    }\n\n}\n\n// Assuming FileNode is defined somewhere in your project.\nclass FileNode {\n    var path: String\n    var children: [FileNode]?\n\n    init(path: String) {\n        self.path = path\n    }\n\n    func discoverChildren() {\n        // Logic to discover child nodes\n    }\n\n    func expandedPaths() -> [String] {\n        // Logic to return currently expanded paths\n        return []\n    }\n\n    func applyExpandedPaths(_ paths: [String]) {\n        // Logic to apply previously expanded paths\n    }\n}\n---------------------------------\n\n";
        Integer tokenCount = TokenCounter.getTokenCount(
                OpenAIGPTModels.GPT_4_TURBO.getName(),
                input
        );

        System.out.println(tokenCount);

        assert(tokenCount > 0);
    }

    @Test
    @DisplayName("Test WebSocket Logic")
    void testWebSocket() {
        // Register user
    }

    @Test
    @DisplayName("Misc Modifyable")
    void misc() {
//        System.out.println("Here it is: " + Table.USER_AUTHTOKEN);
    }
}
