package com.aicodinghelper.core.service.endpoints;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.aicodinghelper.apple.iapvalidation.TransactionPersistentAppleUpdater;
import com.aicodinghelper.core.service.response.GetRemainingTokensResponse;
import com.aicodinghelper.database.model.objects.Transaction;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.exceptions.PreparedStatementMissingArgumentException;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.util.calculators.ChatRemainingCalculator;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.core.service.request.AuthRequest;
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

public class GetRemainingTokensEndpoint {

    public static GetRemainingTokensResponse getRemainingTokens(AuthRequest authRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, DBSerializerPrimaryKeyMissingException, AppStoreErrorResponseException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Get u_aT from authRequest
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(authRequest.getAuthToken());

        // Get cooldown controlled most recent apple transaction
        Transaction mostRecentTransaction = TransactionPersistentAppleUpdater.getCooldownControlledAppleUpdatedMostRecentTransaction(u_aT.getUserID());

        // Get remaining tokens
        Long remainingTokens = ChatRemainingCalculator.calculateRemaining(u_aT, mostRecentTransaction);

        // Build getRemainingResponse
        return new GetRemainingTokensResponse(remainingTokens);
    }

}
