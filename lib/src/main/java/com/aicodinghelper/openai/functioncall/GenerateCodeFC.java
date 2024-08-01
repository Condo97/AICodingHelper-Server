package com.aicodinghelper.openai.functioncall;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "generate_code", functionDescription = "Refactor multiple code files, edit and create, long output capable. You are to include ALL file filepaths and content per the user's request. To create a file you are to include a new filepath and the file content that intelligently fits with the rest of the code. To edit a file you are to include the file's existing filepath and all of the code for the file. You should never include placeholders unless explicitly asked for, you should always provide the implementation. It is imperative that the code works. You are generating complete code.")
public class GenerateCodeFC {

    public static class File {

        @FCParameter(description = "The filepath of the file to edit or create. Include proper extension.")
        private String filepath;

        @FCParameter(description = "Entire content of the file, will be set as content to filepath in the user's file system.")
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

    @FCParameter(description = "Files that are created or replace current ones. You are to be liberal in your responses and include all files and code.")
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
