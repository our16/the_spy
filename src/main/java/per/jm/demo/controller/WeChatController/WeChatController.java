package per.jm.demo.controller.WeChatController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import per.jm.demo.service.WeChatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static per.jm.demo.service.impl.WeChatServiceImpl.VerifyWeChatInterface;

@Controller
@RequestMapping(value = "/wx")
@Slf4j
public class WeChatController extends HandlerInterceptorAdapter {

    @Resource
    WeChatService weChatService;

    /*处理微信发来的请求*/
    @RequestMapping(value = "/thespy",produces = {"application/xml; charset=UTF-8"})
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
        log.info("rquest："+request.getQueryString());
        //获取返回信息
        String resXmlStr = weChatService.ProcessWeChatUserRequests(request,response);
        if(resXmlStr != null){
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print(resXmlStr);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }finally {
                out.close();
            }
        }else
        {
            try {
                response.sendRedirect("/error.jsp");
            } catch (IOException e) {
                System.out.println("跳转失败");
            }
        }
         //微信验证接口
       //VerifyWeChatInterface(request,response);
    }
}
