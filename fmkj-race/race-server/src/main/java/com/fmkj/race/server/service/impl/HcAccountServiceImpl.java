package com.fmkj.race.server.service.impl;


import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.domain.HcAccount;
import com.fmkj.race.dao.mapper.HcAccountMapper;
import com.fmkj.race.server.service.HcAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: youxun
 * @Version: 1.0
 **/
@Service
@Transactional
public class HcAccountServiceImpl extends BaseServiceImpl<HcAccountMapper, HcAccount> implements HcAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HcAccountServiceImpl.class);


}