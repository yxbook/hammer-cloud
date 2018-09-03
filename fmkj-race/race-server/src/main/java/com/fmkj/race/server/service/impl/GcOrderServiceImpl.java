package com.fmkj.race.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.mapper.GcOrderMapper;
import com.fmkj.race.dao.domain.GcOrder;
import com.fmkj.race.server.service.GcOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: GcOrder Service实现
* @Author: ru
* @CreateDate: 2018/9/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class GcOrderServiceImpl extends BaseServiceImpl<GcOrderMapper, GcOrder> implements GcOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcOrderServiceImpl.class);

}