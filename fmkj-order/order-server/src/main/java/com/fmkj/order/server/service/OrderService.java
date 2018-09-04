package com.fmkj.order.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.order.dao.domain.OrderInfo;
import com.fmkj.order.dao.queryVo.OrderQueryVo;

import java.util.List;

/**
* @Description:
* @Author: youxun
* @Version: 1.0
**/
public interface OrderService extends BaseService<OrderInfo> {

    List<OrderInfo> getOrderPage(OrderQueryVo orderQueryVo);
}