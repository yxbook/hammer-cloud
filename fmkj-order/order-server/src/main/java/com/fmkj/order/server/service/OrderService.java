package com.fmkj.order.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.order.dao.domain.OrderInfo;
import com.fmkj.order.dao.dto.OrderDto;
import com.fmkj.order.dao.queryVo.OrderQueryVo;

import java.util.List;

/**
* @Description:
* @Author: youxun
* @Version: 1.0
**/
public interface OrderService extends BaseService<OrderInfo> {

    List<OrderDto> getOrderPage(OrderQueryVo orderQueryVo);

    boolean sellerPayConfirm(OrderInfo orderInfo);

    boolean addOrder(OrderInfo orderInfo);

    boolean updateOrder(OrderInfo orderInfo);

    List<OrderDto> getOrderPageBySeller(OrderQueryVo orderQueryVo);

    boolean sellPToPublisher(OrderInfo orderInfo);
}