package com.fmkj.user.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.common.util.PropertiesUtil;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcUserimage;
import com.fmkj.user.dao.domain.UserRealInfo;
import com.fmkj.user.dao.mapper.HcAccountMapper;
import com.fmkj.user.dao.mapper.HcUserimageMapper;
import com.fmkj.user.server.service.HcAccountService;
import com.fmkj.user.server.service.HcUserimageService;
import com.fmkj.user.server.util.JDWXUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
* @Description: HcUserimage Service实现
* @Author: youxun
* @CreateDate: 2018/9/18.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class HcUserimageServiceImpl extends BaseServiceImpl<HcUserimageMapper, HcUserimage> implements HcUserimageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HcUserimageServiceImpl.class);

    @Autowired
    private HcUserimageMapper hcUserimageMapper;
    @Autowired
    private HcAccountMapper hcAccountMapper;

    public BaseResult saveUserRealInfo(Integer id, String name, String cardnum){
        //1.更新hc_account
        try {
            HcAccount account = new HcAccount();
            account.setId(id);
            account.setCardnum(cardnum);
            account.setName(name);
            account.setCardStatus(1);
            //1.1 验证用户名和省份证号码是否正确
            boolean result = JDWXUtil.cardRealName(account);
            LOGGER.debug("验证用户名和省份证号码是否正确:"+result);
            if(result) {
                boolean updateResult =hcAccountMapper.updateById(account)>0?true:false;
                LOGGER.debug("更新用户名和省份证号码是否正确:"+updateResult);

                if(updateResult){
                    return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "保存用户的实名信息成功!", true);
                }else{
                    return new BaseResult(BaseResultEnum.ERROR.getStatus(), "更新用户的姓名、身份证号以及状态失败!", false);
                }
            }else{
                return new BaseResult(BaseResultEnum.ERROR.getStatus(), "验证用户名和省份证号码失败!", false);
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}