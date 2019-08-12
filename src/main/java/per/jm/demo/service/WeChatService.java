package per.jm.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WeChatService {
    public  String ProcessWeChatUserRequests(HttpServletRequest request, HttpServletResponse response);
}
