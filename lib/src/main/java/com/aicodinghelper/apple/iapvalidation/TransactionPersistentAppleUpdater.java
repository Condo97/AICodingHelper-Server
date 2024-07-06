package com.aicodinghelper.apple.iapvalidation;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.Constants;
import com.aicodinghelper.database.dao.factory.TransactionFactoryDAO;
import com.aicodinghelper.database.dao.pooled.TransactionDAOPooled;
import com.aicodinghelper.database.model.objects.Transaction;
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
import java.time.Duration;
import java.time.LocalDateTime;

public class TransactionPersistentAppleUpdater {

    public static Transaction getCooldownControlledAppleUpdatedMostRecentTransaction(Integer userID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreErrorResponseException, UnrecoverableKeyException, DBSerializerPrimaryKeyMissingException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Get most recent transaction from database
        Transaction mostRecentTransaction = TransactionDAOPooled.getMostRecent(userID);

        // If most recent transaction is null, return null
        if (mostRecentTransaction == null)
            return null;

//        // If most recent transaction check date + Transaction_Status_Apple_Update_Cooldown is before current local date time
//        if (mostRecentTransaction.getCheckDate().plus(Duration.ofSeconds(Constants.Transaction_Status_Apple_Update_Cooldown)).isBefore(LocalDateTime.now())) {
        // If current timestamp is after most recent transaction check date plus cooldown
        if (LocalDateTime.now().isAfter(mostRecentTransaction.getCheckDate().plus(Duration.ofSeconds(Constants.Transaction_Status_Apple_Update_Cooldown)))) {
            // Update and save the Apple transaction status
            updateAndSaveAppleTransactionStatus(mostRecentTransaction);
        }

        return mostRecentTransaction;

    }

    public static Transaction insertAppleUpdatedTransaction(Integer userID, Long transactionID) throws UnrecoverableKeyException, AppStoreErrorResponseException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get updated transaction status from Apple
        AppleTransactionUpdater.UpdatedTransaction updatedTransaction = AppleTransactionUpdater.getUpdatedTransactionStatusFromApple(transactionID);

        // Create and return Transaction for userID
        return TransactionFactoryDAO.create(
                userID,
                transactionID,
                updatedTransaction.getProductID(),
                updatedTransaction.getRecentSubscriptionStartDateEpochTime(),
                updatedTransaction.getCheckDate(), // Use checkDate because it is the most recent update date for the new transaction
                updatedTransaction.getSubscriptionStatus()
        );
    }

    public static void updateAndSaveAppleTransactionStatus(Transaction transaction) throws AppStoreErrorResponseException, UnrecoverableKeyException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get updated transaction status from Apple
        AppleTransactionUpdater.UpdatedTransaction updatedTransaction = AppleTransactionUpdater.getUpdatedTransactionStatusFromApple(transaction.getAppstoreTransactionID());

        // Set transaction fields to updatedTransaction fields
        transaction.setProductID(updatedTransaction.getProductID());
        transaction.setRecentSubscriptionStartDateEpoch(updatedTransaction.getRecentSubscriptionStartDateEpochTime());
        transaction.setSubscriptionStatus(updatedTransaction.getSubscriptionStatus());
        transaction.setCheckDate(updatedTransaction.getCheckDate());

        System.out.println("Updated transaction status with apple: " + transaction.getSubscriptionStatus().getValue());

        // Insert or update transaction in database
        TransactionDAOPooled.insertOrUpdateByMostRecentTransactionID(transaction);
    }

}
