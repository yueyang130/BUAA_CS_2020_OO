package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML008 规则异常
 */
public class UmlRule007Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule007Exception() {
        super("Failed when check R007, Final State have outgoing transitions.");
    }
}
