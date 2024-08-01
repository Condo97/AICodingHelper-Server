package com.aicodinghelper.openai.functioncall;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "generate_code", functionDescription = "Creates and replaces code files per the user's request.")
public class GenerateCodeFC {

    public static class File {

        @FCParameter(description = "Will create if doesn't exist and replace if does. Include proper extension.")
        private String filepath;

        @FCParameter(description = "A short explanation of the changes.")
        private String explanation;

        @FCParameter(description = "Content of file.")
        private String content;

        public File() {

        }

        public File(String filepath, String explanation, String content) {
            this.filepath = filepath;
            this.explanation = explanation;
            this.content = content;
        }

        public String getFilepath() {
            return filepath;
        }

        public String getExplanation() {
            return explanation;
        }

        public String getContent() {
            return content;
        }

    }

    @FCParameter(description = "One or more files to be created or replace current ones as needed per the user request.")
    private List<File> output_files;

    public GenerateCodeFC() {

    }

    public GenerateCodeFC(List<File> output_files) {
        this.output_files = output_files;
    }

    public List<File> getOutput_files() {
        return output_files;
    }

}
