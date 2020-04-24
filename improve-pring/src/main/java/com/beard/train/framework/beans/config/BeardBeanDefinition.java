package com.beard.train.framework.beans.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardBeanDefinition {

    private Boolean lazyInit;
    private String factoryBeanName;
    private String beanClassName;
}
