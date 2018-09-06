package com.fmkj.race.dao.mapper;


import com.fmkj.race.dao.domain.HcAccount;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface HcAccountMapper extends BaseMapper<HcAccount> {



    /**
     * @author yangshengbin
     * @Description：查询最新一条中奖用户信息
     * @date 2018/9/4 0004 12:08
     * @param
     * @return
    */
    List<Map<String,Object>> queryOneNewNotice();
}