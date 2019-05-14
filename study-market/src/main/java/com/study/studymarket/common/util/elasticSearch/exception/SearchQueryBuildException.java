package com.study.studymarket.common.util.elasticSearch.exception;

public class SearchQueryBuildException extends RuntimeException{

    private static final long serialVersionUID = -6791173556201337768L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SearchQueryBuildException(String message) {
        super(message);
        this.message = message;
    }

    public SearchQueryBuildException(Throwable e) {
        super(e);
        this.message = e.getMessage();
    }
}
