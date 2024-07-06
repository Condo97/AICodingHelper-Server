package com.aicodinghelper.database.dao.factory;

import com.aicodinghelper.database.dao.helpers.AuthTokenGenerator;
import com.aicodinghelper.database.model.objects.User_AuthToken;
import com.aicodinghelper.exceptions.AutoIncrementingDBObjectExistsException;
import com.aicodinghelper.database.dao.pooled.User_AuthTokenDAOPooled;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class User_AuthTokenFactoryDAO {

    public static User_AuthToken create() throws AutoIncrementingDBObjectExistsException, DBSerializerException, DBSerializerPrimaryKeyMissingException, IllegalAccessException, SQLException, InterruptedException, InvocationTargetException {
        return create(null);
    }

    public static User_AuthToken create(Integer userID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create User_AuthToken
        User_AuthToken u_aT = new User_AuthToken(
                userID,
                AuthTokenGenerator.generateAuthToken(),
                LocalDateTime.now()
        );

        // Insert using User_AuthTokenDAOPooled and return
        User_AuthTokenDAOPooled.insert(u_aT);

        return u_aT;
    }

}
