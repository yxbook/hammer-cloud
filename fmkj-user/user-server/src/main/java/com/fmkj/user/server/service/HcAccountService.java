package com.fmkj.user.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.user.dao.domain.BaseBean;
import com.fmkj.user.dao.domain.HcAccount;

import java.util.List;
import java.util.Map;

/**
* @Description:
* @Author: youxun
* @Version: 1.0
**/
public interface HcAccountService extends BaseService<HcAccount> {


    /**
     * @author yangshengbin
     * @Description：查询最新一条中奖用户信息
     * @date 2018/9/5 0005 10:08
     * @param
     * @return 
    */
    List<Map<String, Object>> queryOneNewNotice();

    boolean bindEmail(HcAccount ha);

    HcAccount queryUserTaskMessage(Integer uid);

    List<HcAccount> queryAllFriends(Integer accountId);
}