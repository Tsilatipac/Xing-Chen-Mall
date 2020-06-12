package com.lcy.xingchenmall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcy.xingchenmall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:32:56
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuStock(Long skuId);

}

