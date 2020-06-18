package com.lcy.xingchenmall.auth.feign;

import com.lcy.common.utils.R;
import com.lcy.xingchenmall.auth.vo.UserLoginVo;
import com.lcy.xingchenmall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("xingchenmall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo) ;

    @PostMapping("/member/member/login")
     R login(@RequestBody UserLoginVo vo);
}
