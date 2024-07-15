package com.aicodinghelper.core.service.endpoints;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.apple.iapvalidation.TransactionPersistentAppleUpdater;
import com.aicodinghelper.core.AppStoreSubscriptionStatusToIsActiveAdapter;
import com.aicodinghelper.core.service.request.AuthRequest;
import com.aicodinghelper.core.service.response.BodyResponse;
import com.aicodinghelper.core.service.response.IsActiveResponse;
import com.aicodinghelper.core.service.response.factory.BodyResponseFactory;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.database.model.Subscriptions;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.util.ProductIDSubscriptionAdapter;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class GetIsActiveEndpoint {

    public static IsActiveResponse getIsActive(AuthRequest request) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, IOException, UnrecoverableKeyException, DBSerializerPrimaryKeyMissingException, AppStoreErrorResponseException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Get u_aT from authRequest
        User_AuthToken u_aT;
        try {
            u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());
        } catch (DBObjectNotFoundFromQueryException e) {
            throw new AuthenticationException("Could not find authToken.");
        }

        // Get isActive and subscription from Transaction
        Transaction transaction = TransactionPersistentAppleUpdater.getCooldownControlledAppleUpdatedMostRecentTransaction(u_aT.getUserID());
        boolean isActive = AppStoreSubscriptionStatusToIsActiveAdapter.getIsActive(transaction.getSubscriptionStatus());
        Subscriptions subscription = ProductIDSubscriptionAdapter.getSubscription(transaction.getProductID());

        // Build and return isActiveResponse
        return new IsActiveResponse(
                isActive,
                subscription);
    }

}
