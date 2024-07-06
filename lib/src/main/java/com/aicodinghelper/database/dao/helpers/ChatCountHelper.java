package com.aicodinghelper.database.dao.helpers;

import com.dbclient.DBManager;
import com.aicodinghelper.connectionpool.SQLConnectionPoolInstance;
import com.aicodinghelper.database.model.objects.Chat;
import com.aicodinghelper.database.model.DBRegistry;
import com.aicodinghelper.database.model.Sender;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.preparedstatement.component.PSComponent;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperatorCondition;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ChatCountHelper {

    private static Double completionTokenWeight = 1.0; // TODO: Move to Constants
    private static Double promptTokenWeight = 0.5; // TODO: Move to Constants

    public static Long countWeightedChatTokensSince(Integer userID, LocalDateTime sinceDate) throws InterruptedException, DBSerializerException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO: Should this pool access be here? Can we move this to GeneratedChatDAO and use GeneratedChatDAO, or is this a special case because it uses both Conversation and Chat, and not GeneratedChat, so would an independent class like this be the best case, and therefore should this have a pool or what?
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return countWeightedChatTokensSince(conn, userID, sinceDate);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static Long countWeightedChatTokensSince(Connection conn, Integer userID, LocalDateTime sinceDate) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Build SQL conditions SELECT response_tokens, prompt_tokens FROM Chat WHERE userID="userID", recent_subscription_renewal_date < sinceDate;
        SQLOperatorCondition userIDCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.user_id, SQLOperators.EQUAL, userID);
        SQLOperatorCondition sinceDateEpochCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.date, SQLOperators.GREATER_THAN, sinceDate);

        // Get Chats
        List<Chat> chats = DBManager.selectAllWhere(
                conn,
                Chat.class,
                List.of(
                        userIDCondition,
                        sinceDateEpochCondition
                )
        );

        // Loop through received Chats, apply token weights, and return weighted total tokens as Long
        Double weightedTotalTokens = 0.0;
        for (Chat chat: chats) {
            // Add completionTokens multiplied by completionTokenWeight
            weightedTotalTokens += chat.getCompletionTokens() * completionTokenWeight;

            // Add promptTokens multiplied by promptTokenWeight
            weightedTotalTokens += chat.getPromptTokens() * promptTokenWeight;
        }

        return weightedTotalTokens.longValue();
    }




//    public static Long countTodaysGeneratedChats(Integer userID) throws DBSerializerException, SQLException, InterruptedException {
//        // TODO: Should this pool access be here? Can we move this to GeneratedChatDAO and use GeneratedChatDAO, or is this a special case because it uses both Conversation and Chat, and not GeneratedChat, so would an independent class like this be the best case, and therefore should this have a pool or what?
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            return countTodaysGeneratedChats(conn, userID);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static Long countTodaysGeneratedChats(Connection conn, Integer userID) throws DBSerializerException, InterruptedException, SQLException {
//        // Build SQL conditions
//        SQLOperatorCondition userIDCondition = new SQLOperatorCondition(DBRegistry.Table.Conversation.user_id, SQLOperators.EQUAL, userID);
//        SQLOperatorCondition senderNotUserCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.sender, SQLOperators.NOT_EQUAL, Sender.USER.toString());
//        SQLOperatorCondition dateCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.date, SQLOperators.GREATER_THAN, LocalDateTime.now().minus(Duration.ofDays(1)));
//
//        List<PSComponent> sqlConditions = List.of(userIDCondition, senderNotUserCondition, dateCondition);
//
//        // Get chats from today for the conversation, need to do an inner join with Chat
//        return DBManager.countObjectWhereInnerJoin(
//                conn,
//                Conversation.class,
//                sqlConditions,
//                Chat.class,
//                DBRegistry.Table.Conversation.conversation_id
//        );
//    }

//    public static Long countTodaysGeneratedChats(Integer userID) throws DBSerializerException, InterruptedException, SQLException {
//        // Build SQL conditions
//        SQLOperatorCondition userIDCondition = new SQLOperatorCondition(DBRegistry.Table.Conversation.user_id, SQLOperators.EQUAL, userID);
//        SQLOperatorCondition senderNotUserCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.sender, SQLOperators.NOT_EQUAL, Sender.USER.toString());
//        SQLOperatorCondition dateCondition = new SQLOperatorCondition(DBRegistry.Table.Chat.date, SQLOperators.GREATER_THAN, LocalDateTime.now().minus(Duration.ofDays(1)));
//
//        List<PSComponent> sqlConditions = List.of(userIDCondition, senderNotUserCondition, dateCondition);
//
//        // Get chats from today for the conversation, need to do an inner join with Chat
//        return DBManager.countObjectWhereInnerJoin(
//                Conversation.class,
//                sqlConditions,
//                Chat.class,
//                DBRegistry.Table.Conversation.conversation_id
//        );
//    }

}
