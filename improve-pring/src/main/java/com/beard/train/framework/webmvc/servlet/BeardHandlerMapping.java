package com.beard.train.framework.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardHandlerMapping {

    private Pattern pattern;
    private Method method;
    private Object controller;
}
