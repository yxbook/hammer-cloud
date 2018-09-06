package com.fmkj.race.server.service.impl;

import com.fmkj.common.base.BaseServiceImpl;
import com.fmkj.race.dao.domain.GcActivity;
import com.fmkj.race.dao.mapper.GcActivityMapper;
import com.fmkj.race.dao.queryVo.GcBaseModel;
import com.fmkj.race.server.service.GcActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：16:17 2018/8/29 0029
 * @ Description：活动业务接口实现类
 */
@Service
@Transactional
public class GcActivityServiceImpl extends BaseServiceImpl<GcActivityMapper,GcActivity> implements GcActivityService{


    @Autowired
    private GcActivityMapper gcActivityMapper;

    @Override
    /**
     * @author yangshengbin
     * @Description：活动广场分页查询所有活动,只查询活动中(status=2)
     * @date 2018/9/4 0004 14:56
     * @param gcBaseModel
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
    */
    public List<HashMap<String, Object>> queryAllActivityByPage(GcBaseModel gcBaseModel) {
        List<HashMap<String, Object>> list = gcActivityMapper.queryAllActivityByPage(gcBaseModel);
        return list;
    }


    /**
     * @author yangshengbin
     * @Description：传入活动id查询活动详情
     * @date 2018/9/4 0004 15:43
     * @param gcBaseModel
     * @return java.util.HashMap<java.lang.String,java.lang.Object>
    */
    public HashMap<String, Object> queryActivityById(GcBaseModel gcBaseModel) {
        HashMap<String, Object> map = gcActivityMapper.queryActivityById(gcBaseModel);
        return map;
    }


    /**
     * @author yangshengbin
     * @Description：传入uid查询用户参与的活动    status：1进行中  2已锤到的  3.已结束
     * @date 2018/9/4 0004 17:14
     * @param gcBaseModel
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
    */
    public List<HashMap<String, Object>> queryMyJoinActivityByUid(GcBaseModel gcBaseModel) {
        return gcActivityMapper.queryMyJoinActivityByUid(gcBaseModel);
    }




    /**
     * @author yangshengbin
     * @Description：传入uid查询用户发起的活动   status：0:待审核 1:驳回 2:活动中 3：已结束 4：活动异常 5：活动失败
     * @date 2018/9/4 0004 17:54
     * @param gcBaseModel
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
    */
    public List<HashMap<String, Object>> queryMyStartActivityByUid(GcBaseModel gcBaseModel) {
        return null;
    }
}
