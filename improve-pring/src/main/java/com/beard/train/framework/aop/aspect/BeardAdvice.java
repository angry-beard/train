package com.beard.train.framework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardAdvice {

    private Object aspect;
    private Method adviceMethod;
    private String throwName;

    public BeardAdvice(Object newInstance, Method method) {
        this.aspect = newInstance;
        this.adviceMethod = method;
    }
}
