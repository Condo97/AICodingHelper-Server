package com.aicodinghelper.apple.iapvalidation;

import appletransactionclient.*;
import appletransactionclient.exception.AppStoreErrorResponseException;
import appletransactionclient.http.response.status.AppStoreStatusResponse;
import appletransactionclient.http.response.status.AppStoreStatusResponseLastTransactionItem;
import appletransactionclient.http.response.status.AppStoreStatusResponseSubscriptionGroupIdentifierItem;
import appletransactionclient.http.response.status.SignedRenewalInfo;
import com.aicodinghelper.Constants;
import com.aicodinghelper.keys.Keys;
import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;
import com.aicodinghelper.database.model.objects.Transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

public class AppleTransactionUpdater {

    public static class UpdatedTransaction {

        private String productID;
        private Long recentSubscriptionStartDateEpochTime;
        private AppStoreSubscriptionStatus subscriptionStatus;
        private LocalDateTime checkDate;

        public UpdatedTransaction(String productID, Long recentSubscriptionStartDateEpochTime, AppStoreSubscriptionStatus subscriptionStatus, LocalDateTime checkDate) {
            this.productID = productID;
            this.recentSubscriptionStartDateEpochTime = recentSubscriptionStartDateEpochTime;
            this.subscriptionStatus = subscriptionStatus;
            this.checkDate = checkDate;
        }

        public String getProductID() {
            return productID;
        }

        public Long getRecentSubscriptionStartDateEpochTime() {
            return recentSubscriptionStartDateEpochTime;
        }

        public AppStoreSubscriptionStatus getSubscriptionStatus() {
            return subscriptionStatus;
        }

        public LocalDateTime getCheckDate() {
            return checkDate;
        }

    }

    /***
     * Takes given Transaction and updates its status and check date
     */
    public static UpdatedTransaction getUpdatedTransactionStatusFromApple(Long appstoreTransactionID) throws AppStoreErrorResponseException, UnrecoverableKeyException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
        // Get AppStoreStatusResponse from AppleNetworkManager
        AppStoreStatusResponse appStoreSubscriptionStatusResponse = AppleNetworkManager.getAppStoreSubscriptionStatus(appstoreTransactionID);

        //.. is probably how it should be sorta.. however, because we're using one transactionID in the query it should only return one, but if it returns multiple, if there is one where status == SubscriptionStatus.ACTIVE, then it needs to be "dominant", otherwise the first status is used I guess? Should also print out if there are multiple transactions just in case there is more investigation I need to do on this
        String productID = null;
        AppStoreSubscriptionStatus subscriptionStatus = null;
        Long recentSubscriptionStartDateEpochTime = null;
        for (AppStoreStatusResponseSubscriptionGroupIdentifierItem data: appStoreSubscriptionStatusResponse.getData()) {
            for (AppStoreStatusResponseLastTransactionItem lastTransaction: data.getLastTransactions()) {
                // TODO: What is the originalTransactionID in the lastTransaction?
                // Expected Behaviour: Set the subscriptionStatus with the first subscription status in the list, and if there is a later item in the list that has a subscription status of ACTIVE, override subscriptionStatus with this

                // If subscriptionStatus is null or lastTransaction status is ACTIVE, set subscriptionStatus to lastTransaction status (by doing it this way instead of getting the enum for the lastTransaction.getStatus() value we can achieve O(1) instead of O(n) for the if statement!)
                if (subscriptionStatus == null || lastTransaction.getStatus() == AppStoreSubscriptionStatus.ACTIVE.getValue()) {
                    // Set productID and latestRenewalEpochTime
                    SignedRenewalInfo signedRenewalInfo = SubscriptionJWSDecoder.decodeSignedRenewalInfo(lastTransaction.getSignedRenewalInfo());
                    productID = signedRenewalInfo.getProductID();
                    recentSubscriptionStartDateEpochTime = signedRenewalInfo.getRecentSubscriptionStartDate();

                    // Set subscriptionStatus
                    subscriptionStatus = AppStoreSubscriptionStatus.fromValue(lastTransaction.getStatus());
                }
            }
        }

        // For logging purposes to see if there are any times there are more than one data or lastTransaction in the statusResponse
        if (appStoreSubscriptionStatusResponse.getData().length != 1 || appStoreSubscriptionStatusResponse.getData()[0].getLastTransactions().length != 1)
            System.out.println("Found more than one data or lastTransaction object in statusResponse in AppleTransactionUpdater updateTransactionStatusFromApple!\t" + appStoreSubscriptionStatusResponse.getData().length + "-data[] length");

        // Return UpdatedTransaction with product ID, recent subscription epoch time, subscription status, and check date
        return new UpdatedTransaction(
                productID,
                recentSubscriptionStartDateEpochTime,
                subscriptionStatus,
                LocalDateTime.now()
        );
    }

}
