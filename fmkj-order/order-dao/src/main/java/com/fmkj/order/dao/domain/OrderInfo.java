package com.fmkj.order.dao.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: youxun
 * @Date: 2018/8/29 15:13
 * @Description:
 */
@TableName("fm_order_info")
public class OrderInfo extends Model<OrderInfo> implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;                //主键

    @TableField("order_no")
    private Integer orderNo;    //订单编号

    @TableField("buyer_id")
    private Integer buyerId;   //买家

    private BigDecimal payment;  //买家支付总金额

    @TableField("payment_type")
    private Integer paymentType;  //买家支付方式1、微信; 2、支付宝

    @TableField("order_status")
    private Integer order_status;  //订单状态1、未付款; 2、已付款(付款确认); 3、订单取消; 4、交易成功

    @TableField("payment_time")
    private Date paymentTime;  //支付时间

    @TableField("end_time")
    private Date endTime;      //交易完成时间

    @TableField("create_time")
    private Date createTime;  //下单时间

    @TableField("update_time")
    private Date updateTime;  //订单修改时间

    @TableField("leave_msg")
    private String leaveMsg;  //下单留言

    @TableField("is_pay")
    private Integer is_pay;  //付款确认0、未付款确认;1、确认已付款


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public Integer getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(Integer is_pay) {
        this.is_pay = is_pay;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
