package com.lcy.xingchenmall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcy.xingchenmall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ÊôÐÔ&ÊôÐÔ·Ö×é¹ØÁª
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);

}
