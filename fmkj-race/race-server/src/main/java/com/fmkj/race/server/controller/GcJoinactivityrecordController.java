package com.fmkj.race.server.controller;

import com.alibaba.fastjson.JSON;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.race.dao.domain.GcJoinactivityrecord;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.GcJoinactivityrecordService;
import com.fmkj.race.server.rabbitmq.MessageProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：09:12 2018/9/5 0005
 * @ Description：用户参与活动控制层
 */
@RestController
@RequestMapping("/gcjoinactivityrecord")
@DependsOn("springContextHandler")
@Api(tags ={ "用户参加活动服务"},description = "用户参加活动接口-网关路径/api-race")
public class GcJoinactivityrecordController  extends BaseController<GcJoinactivityrecord,GcJoinactivityrecordService> implements BaseApiService<GcJoinactivityrecord> {


    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private GcJoinactivityrecordService gcJoinactivityrecordService;

    /**
     *  用户参加活动
     */
    @ApiOperation(value="用户参加活动 ", notes="用户参加活动")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "用户参加活动")
    @PostMapping("/ActivityRabbitMQ")
    public BaseResult ActivityRabbitMQ(@RequestBody GcJoinactivityrecord gcJoinactivityrecord){

        messageProducer.send(JSON.toJSONString(gcJoinactivityrecord));//生产消息
        return new BaseResult(BaseResultEnum.SUCCESS, true);

    }


    @ApiOperation(value="活动参与记录 ", notes="活动参与记录")
    @RaceLog(module= LogConstant.Gc_Join_Activity_Record, actionDesc = "活动参与记录")
    @PutMapping("/queryJoinActivityByAid")
    public BaseResult<HashMap<String,Object>> queryJoinActivityByAid(@RequestBody Map<String,Object> param) {
        List<HashMap<String, Object>> rets = gcJoinactivityrecordService.queryJoinActivityByAid((Integer) param.get("aid"));
        return new BaseResult(BaseResultEnum.SUCCESS,rets);
    }


}
