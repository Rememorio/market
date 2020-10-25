package com.newbee.maggie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController  //@RestController标签是由两个标签组成，表明我们这个类是一个controller
// 一个是@Controller  表示该类是一个controller 能够用来接收前台发送过来的请求 去做相应的数据处理、请求的响应
//两外一个标签是@ResponseBody  将我们的数据、内容或对象作为http响应正文返回

public class Hello {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)  // 定义路由 告诉前端怎么访问到该方法  GRT的形式：既url直接传参的方式去接受请求
    public String hello() { //编写hello方法去做前台的响应
        //返回Hello SpringBoot!
        return "Hello World!";
    }
}