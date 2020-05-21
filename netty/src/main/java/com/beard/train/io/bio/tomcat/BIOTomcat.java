package com.beard.train.io.bio.tomcat;

public class BIOTomcat {

    //J2EE标准
    //Servlet
    //Request
    //Response

    //1.配置好启动端口 默认8080 ServerSocket IP:localhost
    //2.配置web.xml 自己写的Servlet继承HttpServlet
    // servlet-name
    // servlet-class
    // url-pattern
    //3.读取配置，url-pattern 和 Servlet建立一个映射关系
    // Map servletMapping
    //4.HTTP请求，发送的数据就是字符串，有规律的字符安串（协议）
    //5.从协议内容中拿到URL,把相应的Servlet进行实例化
    //6.调用实例化对象的service()方法，执行具体的逻辑 doGet doPost
    //7.Request(InputStream)/Response(OutputStream)
}
