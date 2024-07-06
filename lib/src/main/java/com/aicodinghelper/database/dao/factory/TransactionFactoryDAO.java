package com.aicodinghelper.database.dao.factory;

import com.aicodinghelper.database.dao.pooled.TransactionDAOPooled;
import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;
import com.aicodinghelper.database.model.objects.Transaction;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransactionFactoryDAO {

    public static Transaction create(Integer userID, Long appstoreTransactionID, String productID, Long recentSubscriptionStartDateEpoch, LocalDateTime recordDate, AppStoreSubscriptionStatus subscriptionStatus) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Create Transaction object
        Transaction transaction = new Transaction(
                userID,
                appstoreTransactionID,
                productID,
                recentSubscriptionStartDateEpoch,
                recordDate,
                subscriptionStatus
        );

        // Insert using TransactionDAOPooled and return
        TransactionDAOPooled.insertOrUpdateByMostRecentTransactionID(transaction);

        return transaction;
    }

}
