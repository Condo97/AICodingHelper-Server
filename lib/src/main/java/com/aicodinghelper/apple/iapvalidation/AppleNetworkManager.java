package com.aicodinghelper.apple.iapvalidation;

import appletransactionclient.JWTSigner;
import appletransactionclient.SubscriptionAppleHttpClient;
import appletransactionclient.SubscriptionStatusJWTGenerator;
import appletransactionclient.exception.AppStoreErrorResponseException;
import appletransactionclient.http.response.status.AppStoreStatusResponse;
import com.aicodinghelper.Constants;
import com.aicodinghelper.keys.Keys;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class AppleNetworkManager {

    public static AppStoreStatusResponse getAppStoreSubscriptionStatus(Long appstoreTransactionID) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, AppStoreErrorResponseException, URISyntaxException, InterruptedException {
        // Create SubscriptionAppleHttpClient instance and JWTGenerator instance
        SubscriptionAppleHttpClient subscriptionAppleHttpClient = new SubscriptionAppleHttpClient(Constants.Apple_Storekit_Base_URL, Constants.Apple_Sandbox_Storekit_Base_URL, Constants.Apple_Get_Subscription_Status_V1_Full_URL_Path);
        JWTSigner jwtSigner = new JWTSigner(Constants.Apple_SubscriptionKey_JWS_Path, Keys.appStoreConnectPrivateKeyID);

        // Generate JWT
        String jwt = SubscriptionStatusJWTGenerator.generateJWT(jwtSigner, Keys.appStoreConnectIssuerID, Constants.Apple_Bundle_ID);

        // Get the status response from apple
        return subscriptionAppleHttpClient.getStatusResponseV1(appstoreTransactionID, jwt);
    }
}
