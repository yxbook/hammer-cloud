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
 * @Date: 2018/8/29 14:52
 * @Description:
 */
@TableName("fm_product_info")
public class ProductInfo extends Model<ProductInfo> implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;                //主键

    @TableField("product_no")
    private Integer productNo;    //商品编号

    @TableField("product_name")
    private String productName;   //商品名称

    @TableField("prodcut_price")
    private BigDecimal prodcutPrice;  //商品单价

    @TableField("prodcut_stock")
    private BigDecimal prodcutStock;   //库存

    @TableField("prodcut_icon")
    private String prodcutIcon;   //商品图标

    private Integer status;       //商品状态0、正常; 1、下架; -1、删除

    @TableField("category_type")
    private Integer categoryType;  //类型

    @TableField("user_id")
    private Integer userId;  //商品创建人

    @TableField("create_time")
    private Date createTime;  //创建时间

    @TableField("update_time")
    private Date update_time;   //修改时间

    @TableField("product_detail")
    private String productDetail;  //商品描述

    @TableField("product_type")
    private Integer productType;//商品类型1、卖出;2、买入

    private Integer currency; //币种

    @TableField("max_price")
    private BigDecimal maxPrice;  //最高可成交的价格

    @TableField("min_limit")
    private BigDecimal minLimit; //每一笔交易额的最小限额

    @TableField("max_limit")
    private BigDecimal maxLimit; //每一笔交易额的最大限额

    @TableField("payment_term")
    private Integer paymentTerm; //付款期限

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProdcutPrice() {
        return prodcutPrice;
    }

    public void setProdcutPrice(BigDecimal prodcutPrice) {
        this.prodcutPrice = prodcutPrice;
    }

    public BigDecimal getProdcutStock() {
        return prodcutStock;
    }

    public void setProdcutStock(BigDecimal prodcutStock) {
        this.prodcutStock = prodcutStock;
    }

    public String getProdcutIcon() {
        return prodcutIcon;
    }

    public void setProdcutIcon(String prodcutIcon) {
        this.prodcutIcon = prodcutIcon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(BigDecimal minLimit) {
        this.minLimit = minLimit;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
