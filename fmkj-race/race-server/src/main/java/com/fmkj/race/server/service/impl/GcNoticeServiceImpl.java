package com.fmkj.race.server.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fmkj.common.annotation.BaseService;
import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.domain.GcActivitytype;
import com.fmkj.race.dao.domain.GcMessage;
import com.fmkj.race.dao.dto.NoticeQueryDto;
import com.fmkj.race.dao.mapper.GcActivityMapper;
import com.fmkj.race.dao.mapper.GcActivitytypeMapper;
import com.fmkj.race.dao.mapper.GcMessageMapper;
import com.fmkj.race.dao.mapper.GcNoticeMapper;
import com.fmkj.race.dao.domain.GcNotice;
import com.fmkj.race.dao.queryVo.NoticeQueryPage;
import com.fmkj.race.server.service.GcNoticeService;
import com.fmkj.race.server.util.CalendarTime;
import com.fmkj.race.server.util.SpringContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    @Autowired
    private GcMessageMapper gcMessageMapper;

    @Autowired
    private GcActivitytypeMapper gcActivitytypeMapper;

    /**
     * @author yangshengbin
     * @Description：查询用户所有通知信息
     * @date 2018/9/3 0003 16:44
     * @param
     * @return java.util.List
    */
    public  List<NoticeQueryDto> queryGcNoticeByUid(Pagination tPage, NoticeQueryPage noticeQueryPage) {
        return gcNoticeMapper.queryGcNoticeByUid(tPage,noticeQueryPage);
    }




    /**查询最新一条中奖用户信息
     * @author yangshengbin
     * @Description：
     * @date 2018/9/4 0004 10:48
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    */
    public List<Map<String, Object>> queryOneNewNotice() {

        return gcNoticeMapper.queryOneNewNotice();
    }

    /**
     * 发起活动插入信息
     * @param startid
     * @return
     */
    @Override
    public boolean addNoticeAndMessage(Integer startid,Integer typeid) {

        CalendarTime clt = new CalendarTime();
        Timestamp btime = clt.thisDate();//获取当前时间

        //查询活动类型
        GcActivitytype gcActivitytype = null;
        try {
            gcActivitytype = gcActivitytypeMapper.selectById(typeid);
        } catch (Exception e) {
            throw new RuntimeException("查询活动类型异常：" + e.getMessage());
        }
        String type = gcActivitytype.getType();


        GcMessage gcMessage = new GcMessage();
        gcMessage.setTime(btime);
        gcMessage.setMessage("您已发起了"+type+"溢价活动，系统审核通过后，活动完成系统将扣除相应手续费后的资产包发送到您的账户，详情请查询活动发起规则。");
        gcMessage.setType(0);
        try {
            gcMessageMapper.insert(gcMessage);
        } catch (Exception e) {
            throw new RuntimeException("插入通知信息(GcMessage)异常：" + e.getMessage());
        }

        GcMessage gcMessage1 = null;
        try {
            gcMessage1 = gcMessageMapper.selectOne(gcMessage);
        } catch (Exception e) {
            throw new RuntimeException("查询通知信息(GcMessage)异常：" + e.getMessage());
        }

        GcNotice gn = new GcNotice();
        gn.setFlag(1);
        gn.setUid(startid);
        gn.setMid(gcMessage1.getId());
        try {
            gcNoticeMapper.insert(gn);
        } catch (Exception e) {
            throw new RuntimeException("查询通知信息(GcNotice)异常：" + e.getMessage());
        }
        return true;
    }
}