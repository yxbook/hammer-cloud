package com.fmkj.user.server.service.impl;


import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.common.comenum.PointEnum;
import com.fmkj.common.util.DateUtil;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcPointsRecord;
import com.fmkj.user.dao.domain.HcUserhead;
import com.fmkj.user.dao.dto.HcAccountDto;
import com.fmkj.user.dao.mapper.HcAccountMapper;
import com.fmkj.user.dao.mapper.HcPointsRecordMapper;
import com.fmkj.user.dao.mapper.HcUserheadMapper;
import com.fmkj.user.server.service.HcAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private HcAccountMapper hcAccountMapper;

    @Autowired
    private HcPointsRecordMapper hcPointsRecordMapper;

    @Autowired
    private HcUserheadMapper hcUserheadMapper;

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

    @Override
    public boolean bindEmail(HcAccount ha) {
        int result = hcAccountMapper.updateById(ha);
        if(result > 0){
            HcPointsRecord hcp = new HcPointsRecord();
            hcp.setUid(ha.getId());
            hcp.setPointsId(PointEnum.BIND_EMAIL.pointId);
            hcp.setPointsNum(PointEnum.BIND_EMAIL.pointNum);
            hcPointsRecordMapper.insert(hcp);
            return true;
        }
        return false;
    }

    @Override
    public HcAccount queryUserTaskMessage(Integer uid) {
        return hcAccountMapper.queryUserTaskMessage(uid);
    }

    @Override
    public List<HcAccount> queryAllFriends(Integer accountId) {
        return hcAccountMapper.selectAllFriends(accountId);
    }

    @Override
    public void uploadUserHead(HcAccount hcAccount, String fileName, String path) {
        int update = hcAccountMapper.updateById(hcAccount);
        if(update > 0){
            HcUserhead hcUserhead = new HcUserhead();
            hcUserhead.setName(fileName);
            hcUserhead.setUrl(path);
            hcUserhead.setUid(hcAccount.getId());
            hcUserhead.setTime(new Date());
            int row = hcUserheadMapper.insert(hcUserhead);
            if(row > 0){
                HcPointsRecord record = new HcPointsRecord();
                record.setPointsId(PointEnum.UPLOAD_HEAD.pointId);
                record.setPointsNum(PointEnum.UPLOAD_HEAD.pointNum);
                record.setUid(hcAccount.getId());
                record.setTime(DateUtil.getNowInMillis(0L));
                hcPointsRecordMapper.insert(record);
            }
        }
    }

    @Override
    public HcAccountDto selectAccountById(Integer id) {
        return hcAccountMapper.selectAccountById(id);
    }

}