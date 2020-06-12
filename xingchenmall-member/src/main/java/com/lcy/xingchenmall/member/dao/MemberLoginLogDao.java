package com.lcy.xingchenmall.member.dao;

import com.lcy.xingchenmall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:19:15
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
