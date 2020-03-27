package ru.iopump.qa.lombok;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value(staticConstructor = "of")
public class ImmutableClass extends BaseClass {
    String field1 = "init";
    String field2;
}

