package ru.iopump.qa.spring.beans;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SingletonBean {
    private final PrototypeBean prototypeBean;


    /* Prototype via Scoped Proxy */
    private final PrototypeProxyBean prototypeProxyBean;
    /* Prototype via ObjectFactory */
    private final ObjectFactory<PrototypeBean> prototypeBeanViaObjectFactory;
    /* Prototype via BeanFactory */
    private final BeanFactory prototypeBeanViaBeanFactory;

    //// GET BEAN ////

    /**
     * As Singleton
     */
    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }

    /**
     * As Prototype
     */
    public PrototypeBean getPrototypeBeanViaObjectFactory() {
        return prototypeBeanViaObjectFactory.getObject();
    }

    /**
     * As Prototype
     */
    public PrototypeBean getPrototypeBeanViaBeanFactory() {
        return prototypeBeanViaBeanFactory.getBean(PrototypeBean.class);
    }



    /**
     * As Prototype
     */
    @Lookup
    public PrototypeBean getPrototypeBeanLookup() {
        return null;
    }


    /**
     * As Prototype
     */
    public PrototypeProxyBean getPrototypeProxyBean() {
        return prototypeProxyBean;
    }
}
