package com.lcy.xingchenmall.order.dao;

import com.lcy.xingchenmall.order.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 10:07:08
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
