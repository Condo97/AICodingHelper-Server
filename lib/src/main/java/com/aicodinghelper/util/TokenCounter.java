package com.aicodinghelper.util;

import com.aicodinghelper.Constants;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequestMessage;
import com.oaigptconnector.model.request.chat.completion.content.OAIChatCompletionRequestMessageContentText;

import java.io.*;
import java.net.URISyntaxException;

public class TokenCounter {

    private static String getTokenCounterPYScriptFilepath() throws URISyntaxException {
        return Constants.PY.tokenCounterFilepath;
    }

    // TODO: This function should probably be moved to a different class lol
    public static int getTotalTokenCount(String modelName, OAIChatCompletionRequest request) throws URISyntaxException {
        // Count total tokens for request messages
        int totalTokenCount = 0;
        for (OAIChatCompletionRequestMessage message: request.getMessages()) {
            // If message content is OAIChatCompletionRequestMessageContentText get tokens and add to total token count
            if (message.getContent() instanceof OAIChatCompletionRequestMessageContentText content) {
                // Get contentText from content
                String contentText = content.getText();

                // Continue if contentText is null
                if (contentText == null)
                    continue;

                // Get tokens from content text and add to totalTokenCount
                try {
                    totalTokenCount += getTokenCount(modelName, contentText);
                } catch (IOException e) {
                    // TODO: Fix this if necessary
                    System.out.println("Error counting total tokens in TokenCounter...");
                    e.printStackTrace();
                }
            }
        }

        // Return totalTokenCount
        return totalTokenCount;
    }

    public static int getTokenCount(String modelName, String inputString) throws IOException, URISyntaxException {
        String[] command = new String[]{"python3", getTokenCounterPYScriptFilepath()};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        try (OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
             BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            // Send model name
            osw.write(modelName + "\n");
            // Send input string
            osw.write(inputString + "\n");
            osw.flush();  // Ensure all data is sent
            osw.close();  // Close stream to signal end-of-input

            // Read the standard error for any potential errors
            String errorLine;
            StringBuilder stdError = new StringBuilder();
            while ((errorLine = stdErrReader.readLine()) != null) {
                stdError.append(errorLine).append(System.lineSeparator());
            }

            // Read the standard output
            String tokenCountStr = stdOutReader.readLine();
            if (tokenCountStr == null) {
                if (stdError.length() > 0) {
                    throw new IOException("Error from Python script: " + stdError.toString());
                } else {
                    throw new IOException("No output received from the Python script.");
                }
            }

            int tokenCount = Integer.parseInt(tokenCountStr.trim());

            if (process.waitFor() != 0) {
                throw new IOException("Python script execution failed with error: " + stdError.toString());
            }

            return tokenCount;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("The process was interrupted", e);
        }
    }

}