package com.fmkj.order.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.order.client.RaceClient;
import com.fmkj.order.dao.domain.UserInfo;
import com.fmkj.order.server.service.HistryService;
import com.fmkj.race.dao.domain.HcAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/order")
public class HyistyController extends BaseController<UserInfo, HistryService> implements BaseApiService<UserInfo> {


    @Autowired
    HistryService hcAccountService;


    @Autowired
    private RaceClient raceClient;

    @GetMapping("/getRaceSelectPage")
    public String getRaceSelectPage(@RequestParam HashMap<String, Object> params){

        BaseResult<Page<HcAccount>> result = raceClient.queryRacePage(params);
        System.out.println("去调用竞锤服务的SelectPage接口：" + JSON.toJSONString(result));
        return  JSON.toJSONString(result);

    }


}
