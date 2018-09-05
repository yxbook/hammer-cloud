package com.fmkj.race.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmkj.race.dao.domain.GcActivity;
import com.fmkj.race.dao.queryVo.GcBaseModel;

import java.util.HashMap;
import java.util.List;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：16:20 2018/8/29 0029
 * @ Description：活动持久层接口
 */
public interface GcActivityMapper extends BaseMapper<GcActivity> {


    /**
     * @author yangshengbin
     * @Description：活动广场分页查询所有活动,只查询活动中(status=2)
     * @date 2018/9/4 0004 14:58
     * @param gcBaseModel
     * @return
    */
    List<HashMap<String,Object>> queryAllActivityByPage(GcBaseModel gcBaseModel);



    /**
     * @author yangshengbin
     * @Description：传入活动id查询活动详情
     * @date 2018/9/4 0004 15:44
     * @param
     * @return
    */
    HashMap<String,Object> queryActivityById(GcBaseModel gcBaseModel);



    /**
     * @author yangshengbin
     * @Description：传入uid查询用户参与的活动    status：1进行中  2已锤到的  3.已结束
     * @date 2018/9/4 0004 17:14
     * @param gcBaseModel
     * @return
    */
    List<HashMap<String,Object>> queryMyJoinActivityByUid(GcBaseModel gcBaseModel);



    /**
     * @author yangshengbin
     * @Description：传入uid查询用户发起的活动   status：0:待审核 1:驳回 2:活动中 3：已结束 4：活动异常 5：活动失败
     * @date 2018/9/4 0004 17:55
     * @param gcBaseModel
     * @return
    */
    List<HashMap<String,Object>> queryMyStartActivityByUid(GcBaseModel gcBaseModel);
}
