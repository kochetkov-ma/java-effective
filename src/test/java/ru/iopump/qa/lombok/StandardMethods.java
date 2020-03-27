package ru.iopump.qa.lombok;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true, of = {"field1", "field2", "field3", "field4", "field5"})
public class StandardMethods extends BaseClass {

    @Getter(value = AccessLevel.PUBLIC, lazy = true)
    private final String field1 = "init";

    @Getter(onMethod_ = @NonNull) // баг lombok
    private String field2;


    @Setter(value = AccessLevel.PUBLIC, onParam_ = @NonNull)
    @Accessors(chain = true, fluent = true)
    private String field3;


    @Accessors(chain = true, fluent = true)
    private String field4;


    private String field5;
    private String field6;
}

