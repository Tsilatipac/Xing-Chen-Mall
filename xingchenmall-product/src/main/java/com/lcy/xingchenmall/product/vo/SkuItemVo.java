package com.lcy.xingchenmall.product.vo;

import com.lcy.xingchenmall.product.entity.SkuImagesEntity;
import com.lcy.xingchenmall.product.entity.SkuInfoEntity;
import com.lcy.xingchenmall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    //1、sku基本信息获取
    SkuInfoEntity info;

    boolean hasStock = true;

    //2、sku的图片信息
    List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    SpuInfoDescEntity desp;

    //5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttr;


}
