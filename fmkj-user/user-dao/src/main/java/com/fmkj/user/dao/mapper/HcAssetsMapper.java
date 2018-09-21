package com.fmkj.user.dao.mapper;

import com.fmkj.user.dao.domain.HcAssets;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author youxun
 * @since 2018-09-21
 */
public interface HcAssetsMapper extends BaseMapper<HcAssets> {

    int updateAssetsContent(HcAssets assets);
}
