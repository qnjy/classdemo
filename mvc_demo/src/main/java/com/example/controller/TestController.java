package com.example.controller;

import com.example.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class TestController {

//    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/toAdd")
    public String toAdd(User user){

        return "add";
    }

    @PostMapping("/add")
    public String add(@Valid User user, BindingResult bindingResult, RedirectAttributes attr){
//        StringUtils.isBlank(user.getName());
//        return Result.fail("用户名不能为空");
        if (bindingResult.hasErrors()){
            return "add";
        }
        //RedirectAttributes 用于重定向后依然携带参数跳转
        // addFlashAttribute 参数会暂存到session中  不会携带在URL后面
        attr.addAttribute("id",user.getId());
        attr.addFlashAttribute("user",user);
        return "redirect:/results";
    }

    @GetMapping("/results")
    public String result(){
        return "result";
    }

}
