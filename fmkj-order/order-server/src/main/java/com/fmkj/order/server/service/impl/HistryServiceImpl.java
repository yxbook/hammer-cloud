package com.fmkj.order.server.service.impl;


import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.order.dao.domain.UserInfo;
import com.fmkj.order.dao.mapper.UserMapper;
import com.fmkj.order.server.service.HistryService;
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
@BaseService
public class HistryServiceImpl extends BaseServiceImpl<UserMapper, UserInfo> implements HistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistryServiceImpl.class);


}