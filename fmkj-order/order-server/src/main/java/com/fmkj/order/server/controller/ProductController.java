package com.fmkj.order.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.queryVo.ProductQueryVo;
import com.fmkj.order.server.service.ProductService;
import com.fmkj.order.server.util.MakeOrderNumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

/**
 * 商品服务
 */
@RestController
@RequestMapping("/product")
@DependsOn("springContextHolder")
@Api(tags ={ "商品服务"},description = "商品服务接口-网关路径/api-order")
public class ProductController extends BaseController<ProductInfo, ProductService> implements BaseApiService<ProductInfo> {


    @Autowired
    private ProductService productService;


    @ApiOperation(value="查询商品列表", notes="分页查询商品列表")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "查询商品列表")
    @PutMapping("/getProductPage")
    public BaseResult<Page<ProductInfo>> getProductPage(@RequestBody ProductQueryVo productQueryVo){
        try {
            Page<ProductInfo> tPage =new Page<ProductInfo>(productQueryVo.getPageNo(),productQueryVo.getPageSize());
            EntityWrapper<ProductInfo> entityWrapper = transWrapper(productQueryVo);
            return new BaseResult(BaseResultEnum.SUCCESS,service.selectPage(tPage, entityWrapper));
        } catch (Exception e) {
           throw new RuntimeException("查询商品列表异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="删除商品", notes="根据ID删除商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "删除商品")
    @PostMapping("/deleteById")
    public BaseResult deleteById(@RequestBody ProductInfo productInfo){
        try {
            if(StringUtils.isNull(productInfo) || productInfo.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            service.deleteById(productInfo.getId());
            return new BaseResult(BaseResultEnum.SUCCESS,"数据删除成功");
        } catch (Exception e) {
            throw new RuntimeException("删除异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="新增商品", notes="新增商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "新增商品")
    @PostMapping("/addProduct")
    public BaseResult addProduct(@RequestBody ProductInfo productInfo){
        try {
            productInfo.setProductNo(MakeOrderNumUtils.createProductNum());
            productInfo.setCreateTime(new Date());
            return super.insert(productInfo);
        } catch (Exception e) {
            throw new RuntimeException("新增异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="修改商品", notes="根据ID修改商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "修改商品")
    @PostMapping("/updateProduct")
    public BaseResult updateProduct(@RequestBody ProductInfo productInfo){
        try {
            if(StringUtils.isNull(productInfo) || productInfo.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            productInfo.setUpdateTime(new Date());
            return super.updateById(productInfo);
        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }


    /**
     * 构建查询条件
     * @param productQueryVo
     */
    private EntityWrapper<ProductInfo> transWrapper(ProductQueryVo productQueryVo) {
        EntityWrapper<ProductInfo> entityWrapper = new EntityWrapper<ProductInfo>();
        if(StringUtils.isNotEmpty(productQueryVo.getProductName())){
            entityWrapper.eq("product_name", productQueryVo.getProductName());
        }
        if(StringUtils.isNotEmpty(productQueryVo.getProductNo())){
            entityWrapper.eq("product_no", productQueryVo.getProductNo());
        }
        if(StringUtils.isNotNull(productQueryVo.getProdcutPrice())){
            entityWrapper.eq("prodcut_price", productQueryVo.getProdcutPrice());
        }
        if(StringUtils.isNotNull(productQueryVo.getProdcutStock())){
            entityWrapper.eq("prodcut_stock", productQueryVo.getProdcutStock());
        }
        if(StringUtils.isNotNull(productQueryVo.getStatus())){
            entityWrapper.eq("status", productQueryVo.getStatus());
        }
        if(StringUtils.isNotNull(productQueryVo.getCategoryType())){
            entityWrapper.eq("category_type", productQueryVo.getCategoryType());
        }
        if(StringUtils.isNotNull(productQueryVo.getUserId())){
            entityWrapper.eq("user_id", productQueryVo.getUserId());
        }
        if(StringUtils.isNotNull(productQueryVo.getCreateTime())){
            entityWrapper.eq("create_time", productQueryVo.getCreateTime());
        }
        if(StringUtils.isNotNull(productQueryVo.getUpdateTime())){
            entityWrapper.eq("update_time", productQueryVo.getUpdateTime());
        }
        if(StringUtils.isNotNull(productQueryVo.getProductType())){
            entityWrapper.eq("product_type", productQueryVo.getProductType());
        }
        // 降序字段
        if(StringUtils.isNotEmpty(productQueryVo.getOrderBy())) {
            entityWrapper.orderBy(productQueryVo.getOrderBy(),false);
        }
        // 升序字段
        if(StringUtils.isNotEmpty(productQueryVo.getOrderByAsc())) {
            entityWrapper.orderBy(productQueryVo.getOrderByAsc(),true);
        }
        return entityWrapper;

    }

}
