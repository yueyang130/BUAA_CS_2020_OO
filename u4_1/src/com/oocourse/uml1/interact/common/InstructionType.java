package com.oocourse.uml1.interact.common;

import com.oocourse.uml1.interact.parser.InputArgumentParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum InstructionType {
    CLASS_COUNT,
    CLASS_OPERATION_COUNT,
    CLASS_ATTR_COUNT,
    CLASS_ASSO_COUNT,
    CLASS_ASSO_CLASS_LIST,
    CLASS_OPERATION_VISIBILITY,
    CLASS_ATTR_VISIBILITY,
    CLASS_TOP_BASE,
    CLASS_IMPLEMENT_INTERFACE_LIST,
    CLASS_INFO_HIDDEN;

    public static final InputArgumentParser COMMON_PARSER
            = InputArgumentParser.newInstance(new Class<?>[]{InstructionType.class}, String.class);
    public static final Map<InstructionType, InputArgumentParser> INSTRUCTION_PARSERS
            = Collections.unmodifiableMap(new HashMap<InstructionType, InputArgumentParser>() {{
        put(CLASS_COUNT, InputArgumentParser.newInstance(InstructionType.class));
        put(CLASS_OPERATION_COUNT, InputArgumentParser.newInstance(new Class<?>[]{InstructionType.class, String.class}, OperationQueryType.class));
        put(CLASS_ATTR_COUNT, InputArgumentParser.newInstance(InstructionType.class, String.class, AttributeQueryType.class));
        put(CLASS_ASSO_COUNT, InputArgumentParser.newInstance(InstructionType.class, String.class));
        put(CLASS_ASSO_CLASS_LIST, InputArgumentParser.newInstance(InstructionType.class, String.class));
        put(CLASS_OPERATION_VISIBILITY, InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
        put(CLASS_ATTR_VISIBILITY, InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
        put(CLASS_TOP_BASE, InputArgumentParser.newInstance(InstructionType.class, String.class));
        put(CLASS_IMPLEMENT_INTERFACE_LIST, InputArgumentParser.newInstance(InstructionType.class, String.class));
        put(CLASS_INFO_HIDDEN, InputArgumentParser.newInstance(InstructionType.class, String.class));
    }});
}
