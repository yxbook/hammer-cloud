package com.fmkj.race.server.controller;

import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.race.dao.domain.GcJoinactivityrecord;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.GcJoinactivityrecordService;
import com.fmkj.race.server.rabbitmq.MessageProducer;
import com.fmkj.race.server.util.TokenStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    /**
     *  用户参加活动
     */
    @ApiOperation(value="用户参加活动 ", notes="用户参加活动")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "用户参加活动")
    @PostMapping("/ActivityRabbitMQ")
    public BaseResult<HashMap<String, Object>> ActivityRabbitMQ(@RequestBody GcJoinactivityrecord gcJoinactivityrecord, @RequestParam String token){

        HashMap<String, Object> map = new HashMap<String, Object>();

        if (token==null||"".equals(token)){
            return new BaseResult(BaseResultEnum.ERROR.status, "无token信息传入!",null);
        }
        TokenStatus tokenStatus = new TokenStatus();
        Boolean flag = tokenStatus.getStatus(token);
        if (!flag) {// token验证不通过
            return new BaseResult(BaseResultEnum.ERROR.status, "您的token过期或不存在!",null);
        }
        map=messageProducer.send(gcJoinactivityrecord);//生产消息
        return new BaseResult(BaseResultEnum.SUCCESS,map);

    }




}
