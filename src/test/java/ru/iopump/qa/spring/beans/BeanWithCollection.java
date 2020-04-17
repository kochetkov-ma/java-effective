package ru.iopump.qa.spring.beans;

import java.util.Collection;
import org.springframework.stereotype.Component;
import ru.iopump.qa.spring.cfg.IBean;

@Component
public class BeanWithCollection {
    private final Collection<? extends IBean> iBeans;

    public BeanWithCollection(Collection<? extends IBean> iBeans) {
        this.iBeans = iBeans;
    }

    public Collection<? extends IBean> getIBeans() {
        return iBeans;
    }
}
