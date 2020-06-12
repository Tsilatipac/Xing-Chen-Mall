package com.lcy.xingchenmall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.to.SkuReductionTo;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:08:26
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);


}



