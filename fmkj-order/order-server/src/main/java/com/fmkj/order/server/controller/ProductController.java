package com.fmkj.order.server.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 订单服务
 */
@RestController
@RequestMapping("/product")
@DependsOn("springContextHolder")
public class ProductController extends BaseController<ProductInfo, ProductService> implements BaseApiService<ProductInfo> {


    @Autowired
    private ProductService productService;


    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "查询商品列表")
    @GetMapping("/getProductPage")
    public BaseResult<Page<ProductInfo>> getProductPage(@RequestParam HashMap<String, Object> params){
        return super.selectPage(params);
    }


}
