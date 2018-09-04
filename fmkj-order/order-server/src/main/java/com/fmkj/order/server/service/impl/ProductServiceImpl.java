package com.fmkj.order.server.service.impl;


import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.dao.mapper.ProductMapper;
import com.fmkj.order.dao.queryVo.ProductQueryVo;
import com.fmkj.order.server.service.ProductService;
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
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, ProductInfo> implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductInfo> getProductPage(ProductQueryVo productQueryVo) {
        List<ProductInfo> result = productMapper.queryProductPage(productQueryVo);
        return result;
    }
}