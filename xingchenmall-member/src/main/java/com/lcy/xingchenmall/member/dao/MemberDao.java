package com.lcy.xingchenmall.member.dao;

import com.lcy.xingchenmall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:19:14
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
