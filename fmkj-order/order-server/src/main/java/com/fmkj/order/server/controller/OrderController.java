package com.fmkj.order.server.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.order.dao.domain.OrderInfo;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

/**
 * 订单服务
 */
@RestController
@RequestMapping("/order")
@DependsOn("springContextHolder")
@Api(tags ={ "订单服务"},description = "商品服务接口-网关路径/api-race")
public class OrderController extends BaseController<OrderInfo, OrderService> implements BaseApiService<OrderInfo> {


    @Autowired
    OrderService orderService;

    @ApiOperation(value="查询订单列表", notes="分页查询订单列表")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "查询订单列表")
    @GetMapping("/getOrderPage")
    public BaseResult<Page<OrderInfo>> getOrderPage(@RequestParam HashMap<String, Object> params){
        try {
            return super.selectPage(params);
        } catch (Exception e) {
            throw new RuntimeException("查询订单列表异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="删除订单", notes="根据ID删除订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "删除订单")
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

    @ApiOperation(value="新建订单", notes="新建订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "新建订单")
    @PostMapping("/addOrder")
    public BaseResult addOrder(OrderInfo orderInfo){
        try {
            orderInfo.setCreateTime(new Date());
            return super.insert(orderInfo);
        } catch (Exception e) {
            throw new RuntimeException("新增异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="修改订单", notes="根据ID修改订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "修改订单")
    @PostMapping("/updateOrder")
    public BaseResult updateProduct(OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || orderInfo.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            orderInfo.setUpdateTime(new Date());
            return super.updateById(orderInfo);
        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }



}
