package com.lcy.xingchenmall.member.dao;

import com.lcy.xingchenmall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:19:15
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {

    MemberLevelEntity getDefaultLevel();

}
