package com.writesmith.model.http.server.request;

public class DeleteChatRequest extends AuthRequest {

    private Integer chatID;

    public DeleteChatRequest() {

    }

    public DeleteChatRequest(String authToken, Integer chatID) {
        super(authToken);
        this.chatID = chatID;
    }

    public Integer getChatID() {
        return chatID;
    }

}
