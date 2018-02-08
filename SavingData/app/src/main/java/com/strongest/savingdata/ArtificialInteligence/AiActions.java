package com.strongest.savingdata.ArtificialInteligence;

/**
 * Created by Cohen on 11/26/2017.
 */

public class AiActions {


    public AiActions() {

    }

    public static class Notes extends AiActions{
        private String note;

        public Notes(String note){

            this.note = note;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class Suggestion extends AiActions{

    }

    public static class Analysis extends AiActions{

        private String analysis;
        private boolean action;
        private String analysisAction;
        private String analysisTag;

        public Analysis(String analysis, boolean action, String analysisAction, String analysisTag) {
            this.analysis = analysis;
            this.action = action;
            this.analysisAction = analysisAction;
            this.analysisTag = analysisTag;
        }

        public Analysis(String analysis, boolean action){
            this.analysis = analysis;
            this.action = action;
        }

        public String getAnalysis() {
            return analysis;
        }
    }
}
