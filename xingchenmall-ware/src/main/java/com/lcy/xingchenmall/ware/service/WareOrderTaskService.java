package com.lcy.xingchenmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcy.common.utils.PageUtils;
import com.lcy.xingchenmall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:32:56
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

