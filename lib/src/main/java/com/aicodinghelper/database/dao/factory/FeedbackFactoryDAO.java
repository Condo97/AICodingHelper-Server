package com.aicodinghelper.database.dao.factory;

import com.aicodinghelper.database.dao.pooled.FeedbackDAOPooled;
import com.aicodinghelper.database.model.objects.Feedback;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FeedbackFactoryDAO {

    public static Feedback create(String feedback) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return create(
                feedback,
                LocalDateTime.now()
        );
    }

    public static Feedback create(String feedback, LocalDateTime dateTime) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Feedback feedbackO = new Feedback(null, feedback, dateTime);

        FeedbackDAOPooled.insert(feedbackO);

        return feedbackO;
    }

}
