package com.fmkj.user.dao.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户身份证图片表
 * </p>
 *
 * @author youxun
 * @since 2018-09-18
 */
@TableName("hc_userimage")
public class HcUserimage extends Model<HcUserimage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer Id;
    private Integer uid;
    /**
     * 照片路径
     */
    private String url;
    /**
     * 正面照名字
     */
    @TableField("full_photo")
    private String fullPhoto;
    /**
     * 反面照片名字
     */
    @TableField("reverse_photo")
    private String reversePhoto;
    /**
     * 存储时间
     */
    private Date time;
    /**
     * 审核状态--0.未审核 1.通过 2.未通过3.实名认证未通过
     */
    private Integer status;
    /**
     * 支付宝账号
     */
    @TableField("alipay_account")
    private String alipayAccount;
    /**
     * 支付宝的收款码照片
     */
    @TableField("alipay_photo")
    private String alipayPhoto;
    /**
     * 微信账号
     */
    @TableField("wechat_account")
    private String wechatAccount;
    /**
     * 微信的收款码照片
     */
    @TableField("wechat_photo")
    private String wechatPhoto;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullPhoto() {
        return fullPhoto;
    }

    public void setFullPhoto(String fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getReversePhoto() {
        return reversePhoto;
    }

    public void setReversePhoto(String reversePhoto) {
        this.reversePhoto = reversePhoto;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayPhoto() {
        return alipayPhoto;
    }

    public void setAlipayPhoto(String alipayPhoto) {
        this.alipayPhoto = alipayPhoto;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }

    public String getWechatPhoto() {
        return wechatPhoto;
    }

    public void setWechatPhoto(String wechatPhoto) {
        this.wechatPhoto = wechatPhoto;
    }

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

    @Override
    public String toString() {
        return "HcUserimage{" +
        "Id=" + Id +
        ", uid=" + uid +
        ", url=" + url +
        ", fullPhoto=" + fullPhoto +
        ", reversePhoto=" + reversePhoto +
        ", time=" + time +
        ", status=" + status +
        ", alipayAccount=" + alipayAccount +
        ", alipayPhoto=" + alipayPhoto +
        ", wechatAccount=" + wechatAccount +
        ", wechatPhoto=" + wechatPhoto +
        "}";
    }
}
