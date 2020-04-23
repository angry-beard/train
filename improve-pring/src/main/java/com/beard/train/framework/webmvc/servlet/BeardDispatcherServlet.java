package com.beard.train.framework.webmvc.servlet;

import com.beard.train.framework.annotation.*;
import com.beard.train.framework.context.BeardApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 负责任务调度 请求分发
 */
public class BeardDispatcherServlet extends HttpServlet {

    private BeardApplicationContext applicationContext;
    private List<BeardHandlerMapping> handlerMappings = new ArrayList<>();
    private Map<BeardHandlerMapping, BeardHandlerAdapter> handlerAdapterMap = new HashMap<>();
    private List<BeardViewResolver> viewResolvers = new ArrayList<>();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.doDispatch(request, response);
        } catch (Exception e) {
            try {
                processDispatchResult(request, response, BeardModelAndView.builder().viewName("500").build());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BeardHandlerMapping handlerMapping = getHandler(request);
        if (Objects.isNull(handlerMapping)) {
            processDispatchResult(request, response, BeardModelAndView.builder().viewName("404").build());
            return;
        }

        BeardHandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);

        assert handlerAdapter != null;

        BeardModelAndView modelAndView = handlerAdapter.handler(request, response, handlerMapping);

        processDispatchResult(request, response, modelAndView);
    }

    private BeardHandlerAdapter getHandlerAdapter(BeardHandlerMapping handlerMapping) {
        if (this.handlerAdapterMap.isEmpty()) {
            return null;
        }
        return this.handlerAdapterMap.get(handlerMapping);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, BeardModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (BeardViewResolver viewResolver : this.viewResolvers) {
            BeardView view = viewResolver.resolveViewName(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
            return;
        }
    }

    private BeardHandlerMapping getHandler(HttpServletRequest request) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replace("/+", "/");
        for (BeardHandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

    public void init(ServletConfig config) {
        //初始化Spring 的核心api
        applicationContext = new BeardApplicationContext(config.getInitParameter("contextConfigLocation"));
        initStrategies(applicationContext);
        System.out.println("angry beard spring framework is init.");
    }

    private void initStrategies(BeardApplicationContext context) {
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
    }

    private void initViewResolvers(BeardApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new BeardViewResolver(templateRoot));
        }
    }

    private void initHandlerAdapters(BeardApplicationContext context) {
        for (BeardHandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapterMap.put(handlerMapping, new BeardHandlerAdapter());
        }
    }

    private void initHandlerMappings(BeardApplicationContext context) {
        if (context.getBeanDefinitionCount() == 0) {
            return;
        }
        for (String beanName : context.getBeanDefinitionNames()) {
            Object instance = context.getBean(beanName);
            Class<?> clazz = context.getBean(beanName).getClass();
            if (!clazz.isAnnotationPresent(BeardController.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(BeardRequestMapping.class)) {
                BeardRequestMapping beardRequestMapping = clazz.getAnnotation(BeardRequestMapping.class);
                baseUrl = beardRequestMapping.value().trim();
            }
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(BeardRequestMapping.class)) {
                    continue;
                }
                BeardRequestMapping beardRequestMapping = method.getAnnotation(BeardRequestMapping.class);
                String regex = ("/" + baseUrl + "/" + beardRequestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMappings.add(BeardHandlerMapping.builder()
                        .controller(instance)
                        .method(method)
                        .pattern(pattern)
                        .build());
                System.out.println("Mapped: " + regex + "," + method);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }

}
