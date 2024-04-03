package jpabook.jpashop.controler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // log
public class HomeController {
    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home"; // home.html 리턴
    }
}
