package com.lcy.xingchenmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * skuÐÅÏ¢
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);


    List<SkuInfoEntity> getSkuBySpuId(Long spuId);
}

