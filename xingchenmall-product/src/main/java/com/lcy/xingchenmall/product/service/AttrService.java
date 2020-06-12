package com.lcy.xingchenmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.product.entity.AttrEntity;
import com.lcy.xingchenmall.product.vo.AttrGroupRelationVo;
import com.lcy.xingchenmall.product.vo.AttrRespVo;
import com.lcy.xingchenmall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * ÉÌÆ·ÊôÐÔ
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
public interface AttrService extends IService<AttrEntity> {


    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性里面，挑选检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);

}

