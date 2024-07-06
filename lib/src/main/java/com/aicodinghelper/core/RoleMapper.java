package com.aicodinghelper.core;

import com.aicodinghelper.database.model.Sender;
import com.oaigptconnector.model.request.chat.completion.CompletionRole;

public class RoleMapper {

    public static CompletionRole getRole(Sender sender) {
        switch (sender) {
            case USER:
                return CompletionRole.USER;
            case AI:
                return CompletionRole.ASSISTANT;
        }

        return null;
    }

}
