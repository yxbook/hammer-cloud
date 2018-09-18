package com.fmkj.user.server.service.impl;


import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcPointsRecord;
import com.fmkj.user.dao.mapper.HcAccountMapper;
import com.fmkj.user.dao.mapper.HcPointsRecordMapper;
import com.fmkj.user.server.service.HcAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: youxun
 * @Version: 1.0
 **/
@Service
@Transactional
public class HcAccountServiceImpl extends BaseServiceImpl<HcAccountMapper, HcAccount> implements HcAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HcAccountServiceImpl.class);

    @Autowired
    private HcAccountMapper hcAccountMapper;

    @Autowired
    private HcPointsRecordMapper hcPointsRecordMapper;

    /**
     * @author yangshengbin
     * @Description：查询最新一条中奖用户信息
     * @date 2018/9/4 0004 10:48
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String, Object>> queryOneNewNotice() {
        return hcAccountMapper.queryOneNewNotice();
    }

    @Override
    public boolean bindEmail(HcAccount ha) {
        int result = hcAccountMapper.updateById(ha);
        if(result > 0){
            HcPointsRecord hcp = new HcPointsRecord();
            hcp.setUid(ha.getId());
            hcp.setPointsId(9);
            hcp.setPointsNum(10d);
            hcPointsRecordMapper.insert(hcp);
            return true;
        }
        return false;
    }

    @Override
    public HcAccount queryUserTaskMessage(Integer uid) {
        return hcAccountMapper.queryUserTaskMessage(uid);
    }

}