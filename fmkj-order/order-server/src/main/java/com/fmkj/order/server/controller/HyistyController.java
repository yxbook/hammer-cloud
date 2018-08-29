package com.fmkj.order.server.controller;

import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.order.client.RaceClient;
import com.fmkj.order.dao.domain.UserInfo;
import com.fmkj.order.server.annotation.OrderLog;
import com.fmkj.order.server.service.HistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/order")
@DependsOn("springContextHolder")
public class HyistyController extends BaseController<UserInfo, HistryService> implements BaseApiService<UserInfo> {


    @Autowired
    HistryService hcAccountService;


    @Autowired
    private RaceClient raceClient;


    @OrderLog(module= LogConstant.HC_ACCOUNT, actionDesc = "查询服务无")
    @GetMapping("/getRaceSelectPage")
    public String getRaceSelectPage(@RequestParam HashMap<String, Object> params){
        String msg = "asdasdads";
       /* try {
            int i = 2/0;
        } catch (Exception e){
            throw new RuntimeException("出现异常啦啦啦");
        }

*/
        throw new RuntimeException("出现异常啦啦啦");

       /* OperateLog operateLog = new OperateLog();
        operateLog.setContent("asdasdasd");
        raceLogService.insert(operateLog);*/
       // return msg;

        /*BaseResult<Page<HcAccount>> result = raceClient.queryRacePage(params);
        System.out.println("去调用竞锤服务的SelectPage接口：" + JSON.toJSONString(result));
        return  JSON.toJSONString(result);*/

    }


}
