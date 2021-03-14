package com.oocourse.uml2.interact.exceptions.user;

/**
 * 多个QueryType冲突
 * 用于传出Failed消息
 */
public class ConflictQueryTypeException extends UserProcessException {
    public ConflictQueryTypeException() {
        super("Failed, conflict modes.");
    }
}