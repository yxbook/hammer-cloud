package com.fmkj.race.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.race.dao.domain.GcNotice;
import com.fmkj.race.dao.queryVo.NoticeQueryPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description: GcNotice Service接口
* @Author: yangshengbin
* @CreateDate: 2018/8/30.
* @Version: 1.0
**/
public interface GcNoticeService extends BaseService<GcNotice> {


    /**
     * @author yangshengbin
     * @Description：查询用户所有通知信息
     * @date 2018/9/3 0003 16:43
     * @param
     * @return
    */
    List<Map<String,Object>> queryGcNoticeByUid(NoticeQueryPage  noticeQueryPage);



}