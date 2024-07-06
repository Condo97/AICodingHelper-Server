package com.aicodinghelper.core.service.endpoints;

import com.aicodinghelper.core.service.request.CalculateTokensRequest;
import com.aicodinghelper.core.service.response.CalculateTokensResponse;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import com.aicodinghelper.util.TokenCounter;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class CalculateTokensEndpoint {

    public static CalculateTokensResponse calculateTokens(CalculateTokensRequest calculateTokensRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, IOException, URISyntaxException {
        // Get u_aT to validate authToken
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(calculateTokensRequest.getAuthToken());

        // Calculate tokens
        Integer tokens = TokenCounter.getTokenCount(
                calculateTokensRequest.getModel().getName(),
                calculateTokensRequest.getInput()
        );

        // Return CalculateTokensResponse with tokens
        return new CalculateTokensResponse(tokens);
    }

}
