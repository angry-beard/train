package com.beard.train.framework.webmvc.servlet;

import lombok.*;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardModelAndView {

    private String viewName;
    private Map<String, ?> model;
}
