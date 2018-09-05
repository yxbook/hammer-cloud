package com.fmkj.order.server.service.impl;


import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.order.dao.domain.OrderInfo;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.dao.dto.OrderDto;
import com.fmkj.order.dao.mapper.OrderMapper;
import com.fmkj.order.dao.mapper.ProductMapper;
import com.fmkj.order.dao.queryVo.OrderQueryVo;
import com.fmkj.order.server.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: youxun
 * @Version: 1.0
 **/
@Service
@Transactional
@BaseService
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<OrderDto> getOrderPage(OrderQueryVo orderQueryVo) {
        return orderMapper.queryOrderPage(orderQueryVo);
    }

    @Override
    public boolean sellerPayConfirm(OrderInfo orderInfo) {
        int result = orderMapper.updateById(orderInfo);
        if(result > 0){
            ProductInfo productInfo = productMapper.selectById(orderInfo.getProductId());
            Double stock = productInfo.getProductStock();
            Double tradeNum = orderInfo.getTradeNum();
            productInfo.setProductStock(stock - tradeNum);
            productMapper.updateById(productInfo);
            return true;
        }
        return false;
    }

    /**
     *  从商品表的库存减掉
     * @param orderInfo
     * @return
     */
    @Override
    public boolean addOrder(OrderInfo orderInfo) {
       int result = orderMapper.insert(orderInfo);
       if(result > 0){
           ProductInfo productInfo = productMapper.selectById(orderInfo.getProductId());
           Double stock = productInfo.getProductStock();
           Double tradeNum = orderInfo.getTradeNum();
           productInfo.setProductStock(stock - tradeNum);
           productMapper.updateById(productInfo);
           return true;
       }
       return false;
    }

    /**
     * 需要把P能量退回到商品的库存中
     * @param orderInfo
     * @return
     */
    @Override
    public boolean cancelOrder(OrderInfo orderInfo) {
        int result = orderMapper.updateById(orderInfo);
        if(result > 0){
            ProductInfo productInfo = productMapper.selectById(orderInfo.getProductId());
            Double stock = productInfo.getProductStock();
            Double tradeNum = orderInfo.getTradeNum();
            productInfo.setProductStock(stock + tradeNum);
            productMapper.updateById(productInfo);
            return true;
        }
        return false;
    }
}