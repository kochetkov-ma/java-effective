package ru.iopump.qa.lombok;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joor.Reflect;
import org.junit.Test;

@Slf4j
public class LombokTest {

    @Test
    public void standardMethods() {
        StandardMethods standardMethods = new StandardMethods();
        log.info("toString >>> '{}'", standardMethods);

        log.info("final field >>> '{}'", (AtomicReference<?>) Reflect.on(standardMethods).get("field1"));

        assertThatThrownBy(() -> standardMethods.field3(null)).isInstanceOf(NullPointerException.class);

        standardMethods.field4("string").setField2(null);
        assertThatCode(() -> standardMethods.getField2()).doesNotThrowAnyException(); // баг lombok
    }

    @Test
    public void data() {
        DataClass dataClass = new DataClass();
        assert "init".equals(dataClass.getField1()); // уже инициализирован

        dataClass.setField2("test");
        assert "test".equals(dataClass.getField2());

        log.info("toString >>> '{}'", dataClass);
        log.info("hashCode >>> '{}'", dataClass.hashCode());

        assert new DataClass().equals(new DataClass());
    }

    @Test
    public void immutable() {
        // ImmutableClass immutableClass = new ImmutableClass("field2"); // private constructor

        ImmutableClass immutableClass = ImmutableClass.of("field2");

        assert "init".equals(immutableClass.getField1()); // уже инициализирован

        assert "field2".equals(immutableClass.getField2());

        log.info("toString >>> '{}'", immutableClass);
        log.info("hashCode >>> '{}'", immutableClass.hashCode());

        assert ImmutableClass.of("field2").equals(ImmutableClass.of("field2"));


        val i1 = ImmutableClass.of("field2"); // final ImmutableClass i1 =
        val i2 = ImmutableClass.of("field2"); // final ImmutableClass i2 =

        // в базовом классе остались не final поля
        i1.setNumber(1);
        i2.setNumber(2);
        // в equals и hashCode не учтен базовый класс
        assert i1.equals(i2);
    }

    @Test(expected = RuntimeException.class)
    public void closeable() {
        CloseableClass external = null;
        try {
            @Cleanup("dispose") CloseableClass closeableClass = new CloseableClass();
            external = closeableClass; // компилятор предупредил (красава)! 'warning: You're assigning an auto-cleanup variable to something else. This is a bad idea.'

            assert !external.disposed;

            closeableClass.exception();
        } finally {
            assert external.disposed;
        }
    }

    public static class CloseableClass {
        boolean disposed;

        public void dispose() {
            disposed = true;
        }

        void exception() {
            throw new RuntimeException();
        }
    }

    @Test
    public void builder() {
        // BuilderClassBuilder builderClass = BuilderClass.builder(); // так не работает
        BuilderClass builderClass = BuilderClass.builder()
            .withField1("a")
            .build();

        assert builderClass != null;
    }


    @Test
    public void utils() {
        assertThatThrownBy(() -> new UtilClass()).isInstanceOf(UnsupportedOperationException.class);

        assert "string".equals(UtilClass.CONST);
        assert "string".equals(UtilClass.get());
    }

    @UtilityClass
    public static class UtilClass {
        public String CONST = "string";

        public String get() {
            return "string";
        }
    }

    @Test
    public void constructor() {
        new FinalOnly("a");
        // new FinalOnly(); // нет
        // new FinalOnly("a", "2"); // нет
    }

    @RequiredArgsConstructor
    public static class FinalOnly {
        private final String f1;
        private String f2;
    }

    @Test
    public void constructorAll() {
        new AllAndNoOnly();
        new AllAndNoOnly("1", "2");
        // new AllAndNoOnly("1"); // нет

        // показать по умолчанию
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class AllAndNoOnly {
        private String f1;
        private String f2;
    }
}
