package com.fmkj.race.server.service.impl;


import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.domain.HcAccount;
import com.fmkj.race.dao.mapper.GcNoticeMapper;
import com.fmkj.race.dao.mapper.HcAccountMapper;
import com.fmkj.race.server.service.HcAccountService;
import com.fmkj.race.server.util.SpringContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    HcAccountMapper hcAccountMapper = SpringContextHandler.getBean(HcAccountMapper.class);

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

}