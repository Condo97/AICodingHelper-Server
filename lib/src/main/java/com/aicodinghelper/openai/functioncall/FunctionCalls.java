package com.aicodinghelper.openai.functioncall;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

public enum FunctionCalls {

    GENERATE_CODE("generate_code");

    private String name;

    FunctionCalls(String name) {
        this.name = name;
    }

    @JsonCreator
    public static FunctionCalls from(String name) {
        for (FunctionCalls functionCall: FunctionCalls.values()) {
            if (functionCall.getName().equals(name)) {
                return functionCall;
            }
        }

        return null;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    public Class<?> getFunctionClass() {
        switch (this) {
            case GENERATE_CODE: return GenerateCodeFC.class;
        }

        return null;
    }

}
