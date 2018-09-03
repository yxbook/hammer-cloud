package com.fmkj.race.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.mapper.GcPimageMapper;
import com.fmkj.race.dao.domain.GcPimage;
import com.fmkj.race.server.service.GcPimageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: GcPimage Service实现
* @Author: ru
* @CreateDate: 2018/9/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class GcPimageServiceImpl extends BaseServiceImpl<GcPimageMapper, GcPimage> implements GcPimageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcPimageServiceImpl.class);

}