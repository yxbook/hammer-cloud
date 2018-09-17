package com.fmkj.user.server.service;

import com.fmkj.common.base.BaseService;
import com.fmkj.user.dao.domain.HcPointsRecord;

/**
* @Description: HcPointsRecord Service接口
* @Author: youxun
* @CreateDate: 2018/9/17.
* @Version: 1.0
**/
public interface HcPointsRecordService extends BaseService<HcPointsRecord> {

    public HcPointsRecord getHcPointsRecord(Integer id);

    public Integer queryPoints(Integer id);
}