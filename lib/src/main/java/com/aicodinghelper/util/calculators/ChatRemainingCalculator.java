package com.aicodinghelper.util.calculators;

import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.database.dao.helpers.ChatCountHelper;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ChatRemainingCalculator {


    public static Long calculateRemaining(User_AuthToken u_aT, Transaction transaction) throws DBSerializerException, InterruptedException, SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        // Get Transaction for userID
//        Transaction transaction = TransactionDAOPooled.getMostRecent(u_aT.getUserID());

        // Create remaining as null Long to ensure even if for some reason calculate remaining subscription returns null it will still fall back to the free subscription
        Long remaining = null;

        // If transaction is not null and active calculate remaining for subscription
        if (transaction != null && (transaction.getSubscriptionStatus() == AppStoreSubscriptionStatus.ACTIVE || transaction.getSubscriptionStatus() == AppStoreSubscriptionStatus.BILLING_GRACE))
            remaining = calculateRemainingSubscription(u_aT.getUserID(), transaction);

        // If remaining is null calculate remaining free
        if (remaining == null)
            remaining = calculateRemainingFree(u_aT.getUserID(), u_aT.getCreationDate());

        // Return remaining
        return remaining;
    }

    private static Long calculateRemainingFree(Integer userID, LocalDateTime creationDate) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Get the most recent week start date for the current period
        long daysBetween = ChronoUnit.DAYS.between(creationDate, LocalDateTime.now());
        long fullWeeks = daysBetween / 7;
        LocalDateTime mostRecentWeekStart = creationDate.plusWeeks(fullWeeks);

        // Get count of weighted tokens from chats from mostRecentWeekStart to now
        Long count = ChatCountHelper.countWeightedChatTokensSince(userID, mostRecentWeekStart);

        // Get cap for free
        Integer cap = TokenCapCalculator.getFreeChatCap();

        return cap - count;
    }

    private static Long calculateRemainingSubscription(Integer userID, Transaction transaction) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Get recentSubscriptionStartDate from Transaction recentSubscriptionStartDateEpoch
        LocalDateTime recentSubscriptionStartDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(transaction.getRecentSubscriptionStartDateEpoch()), ZoneId.systemDefault());

        // Get count of weighted tokens from chats from recentSubscriptionStartDate to now
        Long count = ChatCountHelper.countWeightedChatTokensSince(userID, recentSubscriptionStartDate);

        // Get cap for Transaction productID
        Integer cap = TokenCapCalculator.getChatCapFromAppStoreProductID(transaction.getProductID());

        // If cap is null, return null
        if (cap == null)
            return null;

        return cap - count;
    }

}
