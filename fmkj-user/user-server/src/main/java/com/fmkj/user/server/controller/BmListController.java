package com.fmkj.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.user.dao.domain.BmList;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcSession;
import com.fmkj.user.server.service.BmListService;
import com.fmkj.user.server.service.HcAccountService;
import com.fmkj.user.server.service.HcSessionService;
import com.fmkj.user.server.util.CalendarTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 *  权限、token接口写在这里
 */
@RestController
@RequestMapping("/bmList")
public class BmListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmListController.class);

    @Autowired
    private BmListService bmListService;

    /**
     * 判断用户是否黑名单
     */
    @PostMapping(value = "isBlacker")
    public Boolean isBlacker(@RequestBody BmList bmList) {

        EntityWrapper<BmList> wrapper = new EntityWrapper<BmList>(bmList);

        List<BmList> list = bmListService.selectList(wrapper);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

}
