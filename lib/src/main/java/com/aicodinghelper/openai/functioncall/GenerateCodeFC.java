package com.aicodinghelper.openai.functioncall;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "generate_code")
public class GenerateCodeFC {

    public static class File {

        @FCParameter(description = "Will create if doesn't exist and replace if does. Include proper extension.")
        private String filepath;

        @FCParameter(description = "Content of file.")
        private String content;

        public File() {

        }

        public File(String filepath, String content) {
            this.filepath = filepath;
            this.content = content;
        }

        public String getFilepath() {
            return filepath;
        }

        public String getContent() {
            return content;
        }

    }

    @FCParameter(description = "One or more files to be created or replace current ones as needed per the user request.")
    private List<File> files;

    public GenerateCodeFC() {

    }

    public GenerateCodeFC(List<File> files) {
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

}
