package com.fmkj.race.dao.mapper;

import com.fmkj.race.dao.domain.GcJoinactivityrecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmkj.race.dao.queryVo.GcBaseModel;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 参与活动的记录表 Mapper 接口
 * </p>
 *
 * @author yangshengbin
 * @since 2018-09-05
 */
public interface GcJoinactivityrecordMapper extends BaseMapper<GcJoinactivityrecord> {
    /**
     * 活动参与记录
     * @author ru
     * @param aid
     * @return
     */
    List<HashMap<String,Object>> queryJoinActivityByAid(Integer aid);

}
