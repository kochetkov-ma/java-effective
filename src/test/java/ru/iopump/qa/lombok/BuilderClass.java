package ru.iopump.qa.lombok;

import lombok.Builder;
import lombok.Value;

@Builder(setterPrefix = "with")
@Value
public class BuilderClass {
    String field1;
    String field2;
    String field3;
    String field4;
    String field5;
    String field6;
}

