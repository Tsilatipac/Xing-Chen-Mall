package com.lcy.xingchenmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.member.entity.MemberEntity;
import com.lcy.xingchenmall.member.exception.PhoneExsitException;
import com.lcy.xingchenmall.member.exception.UsernameExsitException;
import com.lcy.xingchenmall.member.vo.MemberLoginVo;
import com.lcy.xingchenmall.member.vo.MemberRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:19:14
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExsitException;

    void checkUsernameUnique(String username) throws UsernameExsitException;

    MemberEntity login(MemberLoginVo vo);
}

