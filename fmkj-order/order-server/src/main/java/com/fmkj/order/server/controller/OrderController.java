package com.fmkj.order.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.order.dao.domain.OrderInfo;
import com.fmkj.order.dao.domain.ProductInfo;
import com.fmkj.order.dao.queryVo.OrderQueryVo;
import com.fmkj.order.dao.queryVo.ProductQueryVo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.service.OrderService;
import com.fmkj.order.server.util.MakeOrderNumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 订单服务
 */
@RestController
@RequestMapping("/order")
@DependsOn("springContextHolder")
@Api(tags ={ "订单服务"},description = "商品服务接口-网关路径/api-order")
public class OrderController extends BaseController<OrderInfo, OrderService> implements BaseApiService<OrderInfo> {


    @Autowired
    OrderService orderService;

    @ApiOperation(value="查询订单列表", notes="分页查询订单列表")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "查询订单列表")
    @PutMapping("/getOrderPage")
    public BaseResult<Page<OrderInfo>> getOrderPage(@RequestBody OrderQueryVo orderQueryVo){
        try {
            Page<OrderInfo> tPage =new Page<OrderInfo>(orderQueryVo.getPageNo(),orderQueryVo.getPageSize());
            List<OrderInfo> list = orderService.getOrderPage(orderQueryVo);
            if(StringUtils.isNotEmpty(list)){
                tPage.setTotal(list.size());
            }
            tPage.setRecords(list);
            return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "查询成功", tPage);
        } catch (Exception e) {
            throw new RuntimeException("查询订单列表异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="删除订单", notes="根据ID删除订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "删除订单")
    @PostMapping("/deleteById")
    public BaseResult deleteById(@RequestBody OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || StringUtils.isNull(orderInfo.getId())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            service.deleteById(orderInfo.getId());
            return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "删除成功", "数据删除成功");
        } catch (Exception e) {
            throw new RuntimeException("删除异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="新建订单", notes="新建订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "新建订单")
    @PostMapping("/addOrder")
    public BaseResult addOrder(@RequestBody OrderInfo orderInfo){
        try {
            orderInfo.setOrderNo(MakeOrderNumUtils.createOrderNum());
            orderInfo.setCreateTime(new Date());
            return super.insert(orderInfo);
        } catch (Exception e) {
            throw new RuntimeException("新增异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="修改订单", notes="根据ID修改订单")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "修改订单")
    @PostMapping("/updateOrder")
    public BaseResult updateProduct(@RequestBody OrderInfo orderInfo){
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

    /**
     * 构建查询条件
     * @param productQueryVo
     */
    private EntityWrapper<OrderInfo> transWrapper(OrderQueryVo orderQueryVo) {
        EntityWrapper<OrderInfo> entityWrapper = new EntityWrapper<OrderInfo>();
        if(StringUtils.isNotEmpty(orderQueryVo.getOrderNo())){
            entityWrapper.eq("order_no", orderQueryVo.getOrderNo());
        }
        if(StringUtils.isNotNull(orderQueryVo.getPaymentType())){
            entityWrapper.eq("payment_type", orderQueryVo.getPaymentType());
        }
        if(StringUtils.isNotNull(orderQueryVo.getOrderStatus())){
            entityWrapper.eq("order_status", orderQueryVo.getOrderStatus());
        }
        if(StringUtils.isNotNull(orderQueryVo.getIsPay())){
            entityWrapper.eq("is_pay", orderQueryVo.getIsPay());
        }
        if(StringUtils.isNotNull(orderQueryVo.getBuyerId())){
            entityWrapper.eq("buyerId", orderQueryVo.getBuyerId());
        }
        // 降序字段
        if(StringUtils.isNotEmpty(orderQueryVo.getOrderBy())) {
            entityWrapper.orderBy(orderQueryVo.getOrderBy(),false);
        }
        // 升序字段
        if(StringUtils.isNotEmpty(orderQueryVo.getOrderByAsc())) {
            entityWrapper.orderBy(orderQueryVo.getOrderByAsc(),true);
        }

        return entityWrapper;

    }



}
