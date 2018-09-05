package com.fmkj.race.server.controller;

import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.race.dao.domain.GcActivitytype;
import com.fmkj.race.dao.queryVo.GcBaseModel;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.GcActivitytypeService;
import com.fmkj.race.server.util.TokenStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：16:30 2018/9/4 0004
 * @ Description：活动类型控制器
 */
@RestController
@RequestMapping("/gcactivitytype")
@DependsOn("springContextHandler")
@Api(tags ={ "活动类型服务"},description = "活动类型服务接口-网关路径/api-race")
public class GcActivitytypeController {

    @Autowired
    private GcActivitytypeService gcActivitytypeService;


    @ApiOperation(value="查询所有活动类型 ", notes="查询所有活动类型 ")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "查询所有活动类型 ")
    @PutMapping("/queryAllActivityType")
    public BaseResult queryAllActivityType(@RequestParam String token){

        if (token==null||"".equals(token)){
            return new BaseResult(BaseResultEnum.ERROR.status, "无token信息传入!",null);
        }
        TokenStatus tokenStatus = new TokenStatus();
        Boolean flag = tokenStatus.getStatus(token);
        if (!flag) {// token验证不通过
            return new BaseResult(BaseResultEnum.ERROR.status, "您的token过期或不存在!",null);
        }
        List<GcActivitytype> list = gcActivitytypeService.queryAllActivityType();
        return new BaseResult(BaseResultEnum.SUCCESS,list);

    }



}
