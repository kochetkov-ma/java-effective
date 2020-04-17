package ru.iopump.qa.spring.beans;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.iopump.qa.spring.cfg.IBean;

@AllArgsConstructor
@Component
@Getter
public class BeanWithCollection {
    private final Collection<? extends IBean> iBeans;
}
