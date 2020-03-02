package ru.iopump.qa.proxy;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class StandardProxyTest {

    /**
     * Standard proxy pattern.
     */
    @Test
    public void testStaticProxy() {
        Item source = new ItemImpl();
        Item proxy = new ItemImplProxy(source);

        assert !proxy.hasName("empty");
        assert proxy.hasName(source.getClass().getSimpleName());
        proxy.getId();
        proxy.getName();
        proxy.getUUID();

        System.out.println("-----------------------------------------");
        System.out.println(proxy.getClass());
        System.out.println(Proxy.isProxyClass(proxy.getClass()));
    }


    /**
     * JDK Dynamic proxy with source object.
     */
    @Test
    public void testDynamicProxyWithSourceObject() {
        final Item SOURCE_OBJECT_TO_DELEGATE_METHOD_INVOCATION = new ItemImpl();

        Item proxy = (Item) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),


                new Class[]{Item.class},


                (createdProxy, method, args) -> {

                    System.out.println("[DYNAMIC PROXY] Invoke " + method.getName() + " " + Arrays.toString(args));

                    try {

                        return method.invoke(SOURCE_OBJECT_TO_DELEGATE_METHOD_INVOCATION, args);

                    } catch (InvocationTargetException ex) {

                        ex.getCause().printStackTrace();

                        throw ex.getCause();
                    }
                }
        );

        assert !proxy.hasName("empty");
        assert proxy.hasName(SOURCE_OBJECT_TO_DELEGATE_METHOD_INVOCATION.getClass().getSimpleName());
        proxy.getId();
        proxy.getName();
        proxy.getUUID();

        assert SOURCE_OBJECT_TO_DELEGATE_METHOD_INVOCATION.toString().equals(proxy.toString()); // from ItemImpl
        assert proxy.hashCode() == proxy.hashCode(); // from ItemImpl

        System.out.println("------------------------------------------");
        System.out.println(proxy.getClass());
        System.out.println(Proxy.isProxyClass(proxy.getClass()));
        System.out.println(Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(), Item.class));
    }

    /**
     * JDK Dynamic proxy without source object.
     */
    @Test
    public void testDynamicProxyWithoutSourceObject() {
        // NO SOURCE OBJECT
        Item proxy = (Item) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),

                new Class[]{Item.class},

                (createdProxy, method, args) -> {

                    System.out.println("[DYNAMIC PROXY] Invoke " + method.getName() + " " + Arrays.toString(args));
                    // Get method name and implement every method
                    String mName = method.getName();
                    if ("hasName".equals(mName)) {
                        return true;
                    }
                    if ("getId".equals(mName)) {
                        return new Random().nextLong();
                    }
                    if ("getUUID".equals(mName)) {
                        return UUID.randomUUID();
                    }
                    if ("toString".equals(mName)) {
                        return "fully proxy object";
                    }
                    if ("hashCode".equals(mName)) {
                        return 0;
                    }
                    // getName and others will return null.
                    return null;
                }
        );

        assert proxy.hasName("any value will return true");

        proxy.getId();
        assert proxy.getName() == null;
        assert proxy.getUUID() != null;

        assert "fully proxy object".equals(proxy.toString());
        assert proxy.hashCode() == 0;

        System.out.println("------------------------------------------");
        System.out.println(proxy.getClass());
        System.out.println(Proxy.isProxyClass(proxy.getClass()));
        System.out.println(Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(), Item.class));
    }
}