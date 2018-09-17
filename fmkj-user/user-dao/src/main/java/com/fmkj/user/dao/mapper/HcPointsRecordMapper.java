package com.fmkj.user.dao.mapper;

import com.fmkj.user.dao.domain.HcPointsRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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


}
