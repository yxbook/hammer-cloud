package com.fmkj.user.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmkj.user.dao.domain.HcAccount;

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