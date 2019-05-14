package com.study.studymarket.common.util.elasticSearch.exception;

public class SearchResultBuildException extends RuntimeException {

    private static final long serialVersionUID = -4857439090166084524L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SearchResultBuildException(String message) {
        super(message);
        this.message = message;
    }

    public SearchResultBuildException(Throwable e) {
        super(e);
        this.message = e.getMessage();
    }
}
