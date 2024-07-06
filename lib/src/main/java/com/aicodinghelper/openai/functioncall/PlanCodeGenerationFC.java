package com.aicodinghelper.openai.functioncall;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "plan_code_generation", functionDescription = "Plan code creation for the given files and instructions. You can edit, add, or delete. You must plan the entire process here")
public class PlanCodeGenerationFC {

    public static class Step {

        @FCParameter
        private Integer index;

        @FCParameter(stringEnumValues = {"edit", "create", "delete"})
        private String action;

        @FCParameter
        private String filepath;

        @FCParameter(description = "only for edit, the summary of the edits to be made, for use when prompting GPT with the changes that should be made", required = false)
        private String edit_summary;

        public Step() {

        }

        public Step(Integer index, String action, String filepath, String edit_summary) {
            this.index = index;
            this.action = action;
            this.filepath = filepath;
            this.edit_summary = edit_summary;
        }

        public Integer getIndex() {
            return index;
        }

        public String getAction() {
            return action;
        }

        public String getFilepath() {
            return filepath;
        }

        public String getEdit_summary() {
            return edit_summary;
        }

    }

    @FCParameter
    private List<Step> steps;

    public PlanCodeGenerationFC() {

    }

    public PlanCodeGenerationFC(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }

}
