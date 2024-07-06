package com.aicodinghelper.database.model.objects;

import com.aicodinghelper.database.model.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.User_AuthToken.TABLE_NAME)
public class User_AuthToken {

    @DBColumn(name = DBRegistry.Table.User_AuthToken.user_id, primaryKey = true)
    private Integer userID;

    @DBColumn(name = DBRegistry.Table.User_AuthToken.auth_token)
    private String authToken;

    @DBColumn(name = DBRegistry.Table.User_AuthToken.creation_date)
    private LocalDateTime creationDate;


    public User_AuthToken() {

    }

    public User_AuthToken(Integer userID, String authToken, LocalDateTime creationDate) {
        this.userID = userID;
        this.authToken = authToken;
        this.creationDate = creationDate;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer id) {
        this.userID = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
