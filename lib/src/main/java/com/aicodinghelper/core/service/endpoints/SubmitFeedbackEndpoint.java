package com.aicodinghelper.core.service.endpoints;

import com.aicodinghelper.core.service.request.FeedbackRequest;
import com.aicodinghelper.core.service.response.StatusResponse;
import com.aicodinghelper.core.service.response.factory.StatusResponseFactory;
import com.aicodinghelper.database.dao.factory.FeedbackFactoryDAO;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.exceptions.DBObjectNotFoundFromQueryException;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class SubmitFeedbackEndpoint {

    public static StatusResponse submitFeedback(FeedbackRequest fr) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException {
        // Get u_aT from authRequest
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(fr.getAuthToken());

        // Save to DB
        FeedbackFactoryDAO.create(fr.getFeedback());

        return StatusResponseFactory.createSuccessStatusResponse();
    }

}
