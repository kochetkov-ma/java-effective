package ru.iopump.qa.proxy2;

import org.junit.Test;
import org.mockito.Mockito;
import ru.iopump.qa.proxy.Item;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;

public class MockTest {

    @Test
    public void test() {
        Item item = Mockito.mock(Item.class);

        Mockito.when(item.getId()).thenReturn(1L);
        Mockito.when(item.getName()).thenReturn("name");

        assertThat(Proxy.isProxyClass(item.getClass())).isFalse();

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("name");
        assertThat(item.getUUID()).isEqualTo(null);

        Mockito.verify(item, atLeast(2)).getId();
    }

}