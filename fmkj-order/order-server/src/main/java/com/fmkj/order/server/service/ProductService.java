package com.fmkj.order.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.dao.queryVo.ProductQueryVo;

import java.util.List;

/**
* @Description:
* @Author: youxun
* @Version: 1.0
**/
public interface ProductService extends BaseService<ProductInfo> {

    List<ProductInfo> getProductPage(ProductQueryVo productQueryVo);
}