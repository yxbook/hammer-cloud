package com.fmkj.race.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.mapper.GcAddressMapper;
import com.fmkj.race.dao.domain.GcAddress;
import com.fmkj.race.server.service.GcAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: GcAddress Service实现
* @Author: yangshengbin
* @CreateDate: 2018/9/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class GcAddressServiceImpl extends BaseServiceImpl<GcAddressMapper, GcAddress> implements GcAddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcAddressServiceImpl.class);

}