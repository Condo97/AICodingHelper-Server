package com.aicodinghelper.core.service.endpoints;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.apple.iapvalidation.TransactionPersistentAppleUpdater;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.core.service.response.factory.BodyResponseFactory;
import com.aicodinghelper.core.AppStoreSubscriptionStatusToIsActiveAdapter;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.core.service.request.RegisterTransactionRequest;
import com.aicodinghelper.core.service.response.BodyResponse;
import com.aicodinghelper.core.service.response.IsActiveResponse;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterTransactionEndpoint {

    public static BodyResponse registerTransaction(RegisterTransactionRequest registerTransactionRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException, AppStoreErrorResponseException {
        // Get the user_authToken object to get the user id
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(registerTransactionRequest.getAuthToken());

        // Create and insert Apple updated transaction
        Transaction transaction = TransactionPersistentAppleUpdater.insertAppleUpdatedTransaction(u_aT.getUserID(), registerTransactionRequest.getTransactionId());

        // Get isActive
        boolean isActive = AppStoreSubscriptionStatusToIsActiveAdapter.getIsActive(transaction.getSubscriptionStatus());

                // TODO: Just logging to see things, remove and make a better logging system!
                if (isActive == true)
                    System.out.println("User " + u_aT.getUserID() + " just registered a transaction at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));

        // Create full validate premium response
        IsActiveResponse fvpr = new IsActiveResponse(isActive);

        // Create and return successful body response with full validate premium response
        return BodyResponseFactory.createSuccessBodyResponse(fvpr);
    }

}
