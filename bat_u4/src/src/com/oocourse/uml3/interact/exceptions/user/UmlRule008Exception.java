package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML008 规则异常
 */
public class UmlRule008Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule008Exception() {
        super("Failed when check R008, An initial vertex can have at most one outgoing transition.");
    }
}
