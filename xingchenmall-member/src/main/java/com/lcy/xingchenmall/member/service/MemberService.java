package com.lcy.xingchenmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.member.entity.MemberEntity;

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
}

