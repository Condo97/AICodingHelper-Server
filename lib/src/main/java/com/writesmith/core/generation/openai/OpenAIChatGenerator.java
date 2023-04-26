package com.writesmith.core.generation.openai;

import com.writesmith.model.database.Sender;
import com.writesmith.model.database.objects.Chat;
import com.writesmith.model.database.objects.Conversation;
import com.writesmith.database.managers.ConversationDBManager;
import com.writesmith.common.exceptions.AutoIncrementingDBObjectExistsException;
import com.writesmith.model.database.objects.GeneratedChat;
import com.writesmith.model.http.client.openaigpt.RoleMapper;
import com.writesmith.model.http.client.openaigpt.exception.OpenAIGPTException;
import com.writesmith.model.http.client.openaigpt.request.prompt.OpenAIGPTChatCompletionRequest;
import com.writesmith.model.http.client.openaigpt.request.prompt.OpenAIGPTChatCompletionMessageRequest;
import com.writesmith.model.http.client.openaigpt.response.prompt.OpenAIGPTChatCompletionResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OpenAIChatGenerator {

    public static GeneratedChat generateFromConversation(Conversation conversation, int contextCharacterLimit, String model, Integer temperature, Integer tokenLimit) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, IOException, OpenAIGPTException {
        // Get all chats in conversation
        List<Chat> chats = ConversationDBManager.getChatsInDB(conversation, contextCharacterLimit);

        // Create OpenAIGPTPromptMessageRequests
        List<OpenAIGPTChatCompletionMessageRequest> messageRequests = new ArrayList<>();

        // Append chats as message requests maintaining order
        chats.forEach(v -> messageRequests.add(new OpenAIGPTChatCompletionMessageRequest(RoleMapper.getRole(v.getSender()), v.getText())));

        // Create OpenAIGPTPromptRequest messageRequests and default values
        OpenAIGPTChatCompletionRequest request = new OpenAIGPTChatCompletionRequest(
                model,
                tokenLimit,
                temperature,
                messageRequests
        );

        // Get response from OpenAIGPTHttpHelper
        try {
            OpenAIGPTChatCompletionResponse response = OpenAIGPTHttpsClientHelper.postChatCompletion(request);

            // Return first choice if it exists
            if (response.getChoices().length > 0) {
                Chat chat = new Chat(
                        conversation.getID(),
                        Sender.AI,
                        response.getChoices()[0].getMessage().getContent(),
                        LocalDateTime.now()
                );

                return new GeneratedChat(
                        chat,
                        response.getChoices()[0].getFinish_reason()
                );
            }

            // No choices, so return null
            return null;

        } catch (OpenAIGPTException e) {
            //TODO: - Process AI Error Response
            throw e;
        }

    }

}