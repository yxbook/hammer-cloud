package com.fmkj.user.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmkj.user.dao.domain.BaseBean;
import com.fmkj.user.dao.domain.HcPointsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 积分记录表 Mapper 接口
 * </p>
 *
 * @author youxun
 * @since 2018-09-17
 */
public interface HcPointsRecordMapper extends BaseMapper<HcPointsRecord> {

    HcPointsRecord getHcPointsRecord(Integer uid);

    Integer queryPoints(Integer uid);

    List<BaseBean> queryInvitingFriendsRankWeek(@Param("peopleNum")int peopleNum, @Param("dataNum")int dataNum);

    Integer queryUserScoresByUid(Integer uid);
}
