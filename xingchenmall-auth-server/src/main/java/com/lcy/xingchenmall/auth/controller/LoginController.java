package com.lcy.xingchenmall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.lcy.common.constant.AuthServerConstant;
import com.lcy.common.utils.R;
import com.lcy.xingchenmall.auth.feign.MemberFeignService;
import com.lcy.xingchenmall.auth.service.CaptchaService;
import com.lcy.xingchenmall.auth.vo.UserLoginVo;
import com.lcy.xingchenmall.auth.vo.UserRegistVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    CaptchaService captchaService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    /**
     * 发送一个请求直接跳转到一个页面。
     * SpringMVC viewcontroller：将请求和页面映射过来
     *
     */

    //    @GetMapping("/sms/sendCode")
    //    public R sendCode(){
    //        return R.ok();
    //    }

    /**
     * 验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response,String phone)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

//        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
//        long l = Long.parseLong(redisCode.split("_")[1]);
//        if(System.currentTimeMillis()-l<60*1000){
//            //60秒内不能再发
//            return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
//        }

        String codeStr = UUID.randomUUID().toString().substring(0,5);
        //redis缓存验证码。防止同一个phone在60秒内再次发送验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,codeStr,10, TimeUnit.MINUTES);

        //获取图片验证码
        BufferedImage image = captchaService.getCaptcha(codeStr);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * //TODO 重定向携带数据，利用session原理。将数据放在session中。
     *   只要跳到下一个页面取出这个数据后，session里面的数据就会被删掉
     * //TODO 1、分布式下session问题
     *  RedirectAttributes redirectAttributes模拟重定向携带数据
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            /*.map(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errors.put(field,message);
            })*/
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.xingchenmall.com/reg.html";
        }
        //真正注册。调用远程服务进行注册
        //1、校验验证码
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equals(s)){
                //删除验证码；令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证码通过  //真正注册。调用远程服务进行注册
                R r = memberFeignService.regist(vo);
                if(r.getCode()==0){
                    //成功
                    return "redirect:http://auth.xingchenmall.com/login.html";
                }else{
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addAttribute("errors",errors);
                    return "redirect:http://auth.xingchenmall.com/reg.html";
                }
            }else{
                Map<String, String> errors =new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                //校验出错，转发到注册页
                return "redirect:http://auth.xingchenmall.com/reg.html";
            }
        }else{
            Map<String, String> errors =new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.xingchenmall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo,RedirectAttributes redirectAttributes){
        //远程登陆
        R login = memberFeignService.login(vo);
        if(login.getCode()==0){
            return "redirect:http://xingchenmall.com";
        }else{
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.xingchenmall.com/login.html";
        }
    }
}
