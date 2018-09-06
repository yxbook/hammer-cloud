package com.fmkj.order.server.service.impl;


import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.order.dao.domain.HcAccount;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.dao.dto.ProductDto;
import com.fmkj.order.dao.mapper.HcAccountMapper;
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

    @Autowired
    private HcAccountMapper hcAccountMapper;

    @Override
    public List<ProductDto> getProductPage(Pagination page, ProductQueryVo productQueryVo) {
        List<ProductDto> result = productMapper.queryProductPage(page, productQueryVo);
        return result;
    }

    /**
     * 发布商品
     * //第一步：更新商品表状态
     * //第二步：扣除P能量
     * @param productInfo
     * @return
     */
    @Override
    public boolean publishProduct(ProductInfo productInfo) {
        int result = productMapper.updateById(productInfo);
        if (result > 0){
            HcAccount hcAccount = hcAccountMapper.selectById(productInfo.getUserId());
            Double productSum = productInfo.getProductSum();
            Double myP = hcAccount.getMyP();
            hcAccount.setMyP(myP - productSum);
            hcAccountMapper.updateById(hcAccount);
            return true;
        }
        return false;
    }

    /**
     * 下架
     * @param productInfo
     * @return
     */
    @Override
    public boolean unLineProduct(ProductInfo productInfo) {
        int result = productMapper.updateById(productInfo);
        if(result > 0){
            if (productInfo.getProductStock() > 0){
                HcAccount hcAccount = hcAccountMapper.selectById(productInfo.getUserId());
                Double myP = hcAccount.getMyP();
                hcAccount.setMyP(myP + productInfo.getProductStock());
                hcAccountMapper.updateById(hcAccount);
            }
            return true;
        }
        return false;
    }
}