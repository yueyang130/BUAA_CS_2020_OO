package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML008 规则异常
 */
public class UmlRule006Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule006Exception() {
        super("Failed when check R006, All attributes and operations of interface must be public.");
    }
}
