package com.fmkj.user.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.user.dao.mapper.HcUserimageMapper;
import com.fmkj.user.dao.domain.HcUserimage;
import com.fmkj.user.server.service.HcUserimageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}