package com.kep.core.model.exception;

public class NotFoundIssueException extends RuntimeException {

	public NotFoundIssueException() {
		super("Issue Not Found");
	}

	public NotFoundIssueException(String msg) {
		super(msg);
	}

	public NotFoundIssueException(Exception e) {
		super(e);
	}
}
