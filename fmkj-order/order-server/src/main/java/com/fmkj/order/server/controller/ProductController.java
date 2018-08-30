package com.fmkj.order.server.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
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
@Api(tags ={ "商品服务"},description = "商品服务接口-网关路径/api-race")
public class ProductController extends BaseController<ProductInfo, ProductService> implements BaseApiService<ProductInfo> {


    @Autowired
    private ProductService productService;


    @ApiOperation(value="查询商品列表", notes="分页查询商品列表")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "查询商品列表")
    @GetMapping("/getProductPage")
    public BaseResult<Page<ProductInfo>> getProductPage(@RequestParam HashMap<String, Object> params){
        try {
            return super.selectPage(params);
        } catch (Exception e) {
           throw new RuntimeException("查询商品列表异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="删除商品", notes="根据ID删除商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "删除商品")
    @DeleteMapping("/deleteById")
    public BaseResult deleteById(String id){
        try {
            if(id == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            return super.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("删除异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="新增商品", notes="新增商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "新增商品")
    @PostMapping("/addProduct")
    public BaseResult addProduct(ProductInfo productInfo){
        try {
            productInfo.setCreateTime(new Date());
            return super.insert(productInfo);
        } catch (Exception e) {
            throw new RuntimeException("新增异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="修改商品", notes="根据ID修改商品")
    @OrderLog(module= LogConstant.HC_PRODUCT, actionDesc = "修改商品")
    @PostMapping("/updateProduct")
    public BaseResult updateProduct(ProductInfo productInfo){
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


}
