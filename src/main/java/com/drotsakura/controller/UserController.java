package com.drotsakura.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drotsakura.common.R;
import com.drotsakura.pojo.User;
import com.drotsakura.service.UserService;
import com.drotsakura.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public  R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone = user.getPhone();
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        httpSession.setAttribute("phone",phone);
        httpSession.setAttribute("code",code);

        log.info("验证码为:{}",code);
        return R.success("验证码已发送");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession HttpSession){
        log.info("{}",map);
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        Object cookiePhone = HttpSession.getAttribute("phone");
        Object cookieCode = HttpSession.getAttribute("code");

        if (!cookiePhone.equals(phone)){
            return R.error("手机号错误");
        }

        if (!cookieCode.equals(code)){
            return R.error("验证码错误");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(queryWrapper);

        if (user == null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }

        log.debug("userId-controller{}",user.getId());

        HttpSession.setAttribute("user",user.getId());
        return R.success(user);
    }
}
