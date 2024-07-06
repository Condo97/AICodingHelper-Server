package com.aicodinghelper.database.dao.pooled;

import com.aicodinghelper.connectionpool.SQLConnectionPoolInstance;
import com.aicodinghelper.database.dao.FeedbackDAO;
import com.aicodinghelper.database.model.objects.Feedback;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class FeedbackDAOPooled {

    public static void insert(Feedback feedback) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            FeedbackDAO.insert(conn, feedback);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}
