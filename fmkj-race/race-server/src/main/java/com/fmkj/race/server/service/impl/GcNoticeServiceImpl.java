package com.fmkj.race.server.service.impl;

import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.mapper.GcActivityMapper;
import com.fmkj.race.dao.mapper.GcNoticeMapper;
import com.fmkj.race.dao.domain.GcNotice;
import com.fmkj.race.dao.queryVo.NoticeQueryPage;
import com.fmkj.race.server.service.GcNoticeService;
import com.fmkj.race.server.util.SpringContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description: GcNotice Service实现
* @Author: yangshengbin
* @CreateDate: 2018/8/30.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class GcNoticeServiceImpl extends BaseServiceImpl<GcNoticeMapper, GcNotice> implements GcNoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcNoticeServiceImpl.class);

   /* @Autowired
    private GcNoticeMapper gcNoticeMapper;*/

    GcNoticeMapper gcNoticeMapper = SpringContextHandler.getBean(GcNoticeMapper.class);

    /**
     * @author yangshengbin
     * @Description：查询用户所有通知信息
     * @date 2018/9/3 0003 16:44
     * @param
     * @return java.util.List
    */
    public List<Map<String,Object>> queryGcNoticeByUid(NoticeQueryPage  noticeQueryPage) {
        return gcNoticeMapper.queryGcNoticeByUid(noticeQueryPage);
    }




}