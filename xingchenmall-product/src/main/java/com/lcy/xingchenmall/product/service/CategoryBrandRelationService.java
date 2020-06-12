package com.lcy.xingchenmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.product.entity.BrandEntity;
import com.lcy.xingchenmall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * Æ·ÅÆ·ÖÀà¹ØÁª
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

