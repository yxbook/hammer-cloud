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
import com.fmkj.order.dao.dto.OrderDto;
import com.fmkj.order.dao.queryVo.OrderQueryVo;
import com.fmkj.order.dao.queryVo.ProductQueryVo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.enmu.OrderEnum;
import com.fmkj.order.server.service.OrderService;
import com.fmkj.order.server.service.ProductService;
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
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @ApiOperation(value="查询订单列表", notes="分页查询订单列表")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "查询订单列表")
    @PutMapping("/getOrderPage")
    public BaseResult<Page<OrderDto>> getOrderPage(@RequestBody OrderQueryVo orderQueryVo){
        try {
            Page<OrderDto> tPage =new Page<OrderDto>(orderQueryVo.getPageNo(),orderQueryVo.getPageSize());
            List<OrderDto> list = orderService.getOrderPage(orderQueryVo);
            if(StringUtils.isNotEmpty(list)){
                tPage.setTotal(list.size());
            }
            tPage.setRecords(list);
            return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "查询成功", tPage);
        } catch (Exception e) {
            throw new RuntimeException("查询订单列表异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="取消订单", notes="取消订单、需要把P能量退回到商品的库存中")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "取消订单")
    @PostMapping("/cancelOrder")
    public BaseResult cancelOrder(@RequestBody OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || StringUtils.isNull(orderInfo.getId())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "订单ID不能为空", false);
            }
            if(StringUtils.isNull(orderInfo.getProductId())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "商品ID不能为空", false);
            }
            synchronized (this){
                orderInfo.setUpdateTime(new Date());
                orderInfo.setOrderStatus(OrderEnum.ORDER_CANCEL.status);
                boolean result = service.updateById(orderInfo);
                if(result){
                    //需要把P能量退回到商品的库存中
                    ProductInfo productInfo = productService.selectById(orderInfo.getProductId());
                    Double stock = productInfo.getProductStock();
                    Double tradeNum = orderInfo.getTradeNum();
                    productInfo.setProductStock(stock + tradeNum);
                    productService.updateById(productInfo);
                    return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "订单取消成功", "商品ID【" + productInfo.getId() + "】库存增加" + tradeNum + "P");
                }else{
                    return new BaseResult(BaseResultEnum.ERROR.getStatus(), "取消订单失败", false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("取消订单异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="新建订单", notes="新建订单，需要扣除商品表的库存")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "新建订单")
    @PostMapping("/addOrder")
    public BaseResult addOrder(@RequestBody OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || StringUtils.isNull(orderInfo.getId())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", false);
            }
            if(StringUtils.isNull(orderInfo) || StringUtils.isNull(orderInfo.getProductId())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "商品ID不能为空", false);
            }
            synchronized (this){
                orderInfo.setOrderNo(MakeOrderNumUtils.createOrderNum());
                orderInfo.setCreateTime(new Date());
                boolean result = service.insert(orderInfo);
                if(result){
                    //从商品表的库存减掉
                    ProductInfo productInfo = productService.selectById(orderInfo.getProductId());
                    Double stock = productInfo.getProductStock();
                    Double tradeNum = orderInfo.getTradeNum();
                    productInfo.setProductStock(stock - tradeNum);
                    productService.updateById(productInfo);
                    return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "新建订单成功", "从商品ID【" + productInfo.getId() + "】库存中减掉" + tradeNum + "P");
                }else{
                    return new BaseResult(BaseResultEnum.ERROR.getStatus(), "新建订单失败", false);
                }
            }
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
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", false);
            }
            orderInfo.setUpdateTime(new Date());
            return super.updateById(orderInfo);
        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="买方支付确认", notes="作为买方购买P能量、确认已支付金额")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "买方支付确认")
    @PostMapping("/buyerPayConfirm")
    public BaseResult buyerPayConfirm(@RequestBody OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || orderInfo.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "订单ID不能为空", false);
            }
            orderInfo.setUpdateTime(new Date());
            orderInfo.setOrderStatus(OrderEnum.ORDER_PAY.status);
            if (service.updateById(orderInfo))return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "支付成功","买方支付金额成功");
            else return new BaseResult(BaseResultEnum.ERROR.getStatus(),"支付失败","买方支付金额失败");
        } catch (Exception e) {
            throw new RuntimeException("买方支付金额失败：" + e.getMessage());
        }
    }

    @ApiOperation(value="卖方支付确认", notes="卖方出售P能量、收到买方支付金额后确认放出P能量")
    @OrderLog(module= LogConstant.HC_ORDER, actionDesc = "卖方支付确认")
    @PostMapping("/sellerPayConfirm")
    public BaseResult sellerPayConfirm(@RequestBody OrderInfo orderInfo){
        try {
            if(StringUtils.isNull(orderInfo) || orderInfo.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "订单ID不能为空", false);
            }
            orderInfo.setUpdateTime(new Date());
            // 可能要加卖方支付确认字段
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
        if(StringUtils.isNotNull(orderQueryVo.getUserId())){
            entityWrapper.eq("userId", orderQueryVo.getUserId());
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
