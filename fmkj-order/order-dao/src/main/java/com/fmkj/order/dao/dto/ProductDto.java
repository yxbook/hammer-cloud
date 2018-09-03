package com.fmkj.order.dao.dto;

import com.fmkj.order.dao.domain.HcAccount;
import com.fmkj.order.dao.domain.ProductInfo;

/**
 * 返回给前台的对象
 * @Author: youxun
 * @Date: 2018/9/3 11:33
 * @Description:
 */
public class ProductDto extends ProductInfo{

    private HcAccount hcAccount;

    public HcAccount getHcAccount() {
        return hcAccount;
    }

    public void setHcAccount(HcAccount hcAccount) {
        this.hcAccount = hcAccount;
    }
}
