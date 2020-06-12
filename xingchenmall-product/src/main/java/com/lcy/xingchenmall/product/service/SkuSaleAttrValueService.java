package com.lcy.xingchenmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.product.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * skuÏúÊÛÊôÐÔ&Öµ
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

