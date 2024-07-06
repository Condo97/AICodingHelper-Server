package com.aicodinghelper.database.dao.pooled;

import com.aicodinghelper.connectionpool.SQLConnectionPoolInstance;
import com.aicodinghelper.database.dao.ChatDAO;
import com.aicodinghelper.database.model.objects.Chat;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class ChatDAOPooled {

    public static Chat getFirstByPrimaryKey(Object primaryKey) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return ChatDAO.getFirstByPrimaryKey(conn, primaryKey);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void insert(Chat chat) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            ChatDAO.insert(conn, chat);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}