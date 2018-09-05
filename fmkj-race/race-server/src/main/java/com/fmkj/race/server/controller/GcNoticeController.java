package com.fmkj.race.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.race.dao.domain.GcMessage;
import com.fmkj.race.dao.domain.GcNotice;
import com.fmkj.race.dao.queryVo.NoticeQueryPage;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.GcMessageService;
import com.fmkj.race.server.service.GcNoticeService;
import com.fmkj.race.server.util.TokenStatus;
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
 * @ Date       ：15:00 2018/9/3 0003
 * @ Description：用户通知信息控制层
 */
@RestController
@RequestMapping("/gcnotice")
@DependsOn("springContextHandler")
@Api(tags ={ "用户通知信息"},description = "用户通知信息接口-网关路径/api-race")
public class GcNoticeController extends BaseController<GcNotice,GcNoticeService> implements BaseApiService<GcNotice> {

    @Autowired
    private GcNoticeService gcNoticeService;

    @Autowired
    private GcMessageService gcMessageService;



    /**
     * @author yangshengbin
     * @Description：传入通知id修改用户通知已读状态
     * @date 2018/9/3 0003 15:10
     * @param gcNotice,token
     * @return
    */
    @ApiOperation(value="修改用户通知已读状态", notes="修改用户通知已读状态")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "修改用户通知已读状态")
    @PostMapping("/updateNoticeById")
    public BaseResult updateNoticeById(@RequestBody GcNotice gcNotice, @RequestParam String token){

        if (token==null||"".equals(token)){
            return new BaseResult(BaseResultEnum.ERROR.status, "无token信息传入!",null);
        }
        TokenStatus tokenStatus = new TokenStatus();
        Boolean flag = tokenStatus.getStatus(token);
        if (!flag) {// token验证不通过
            return new BaseResult(BaseResultEnum.ERROR.status, "您的token过期或不存在!",null);
        }

        try {
            if( gcNotice.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            gcNotice.setFlag(0);
            return super.updateById(gcNotice);
        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }





    @ApiOperation(value="分页查询用户所有通知信息", notes="分页查询用户所有通知信息")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "分页查询用户所有通知信息")
    @PutMapping("/queryGcNoticeByUid")
    public BaseResult<Page<GcNotice>> queryGcNoticeByUid(@RequestBody NoticeQueryPage noticeQueryPage, @RequestParam String token){

        if (token==null||"".equals(token)){
            return new BaseResult(BaseResultEnum.ERROR.status, "无token信息传入!",null);
        }
        TokenStatus tokenStatus = new TokenStatus();
        Boolean flag = tokenStatus.getStatus(token);
        if (!flag) {// token验证不通过
            return new BaseResult(BaseResultEnum.ERROR.status, "您的token过期或不存在!",null);
        }

        try {

            List<Map<String,Object>> map = gcNoticeService.queryGcNoticeByUid(noticeQueryPage);
            return new BaseResult(BaseResultEnum.SUCCESS,map);
        } catch (Exception e) {
            throw new RuntimeException("查询用户所有通知信息异常：" + e.getMessage());
        }
    }



    private EntityWrapper<GcNotice> transWrapper(NoticeQueryPage noticeQueryPage) {
        EntityWrapper<GcNotice> entityWrapper = new EntityWrapper<GcNotice>();

        if(StringUtils.isNotNull(noticeQueryPage.getUid())){
            entityWrapper.eq("uid", noticeQueryPage.getUid());
        }
        // 降序字段
        if(StringUtils.isNotEmpty(noticeQueryPage.getOrderBy())) {
            entityWrapper.orderBy(noticeQueryPage.getOrderBy(),false);
        }
        // 升序字段
        if(StringUtils.isNotEmpty(noticeQueryPage.getOrderByAsc())) {
            entityWrapper.orderBy(noticeQueryPage.getOrderByAsc(),true);
        }
        return entityWrapper;
    }




    @ApiOperation(value="用户删除通知信息", notes="用户删除通知信息")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "用户删除通知信息")
    @PutMapping("/deleteGcNoticeById")
    public BaseResult deleteGcNoticeById(@RequestBody GcNotice gcNotice, @RequestParam String token){

        if (token==null||"".equals(token)){
            return new BaseResult(BaseResultEnum.ERROR.status, "无token信息传入!",null);
        }
        TokenStatus tokenStatus = new TokenStatus();
        Boolean flag = tokenStatus.getStatus(token);
        if (!flag) {// token验证不通过
            return new BaseResult(BaseResultEnum.ERROR.status, "您的token过期或不存在!",null);
        }

        try {
            if(StringUtils.isNull(gcNotice) || gcNotice.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }

            //获取GcNotice的mid
            GcNotice gcNotice1 = service.selectById(gcNotice.getId());
            Integer mid = gcNotice1.getMid();
            GcMessage gcMessage = new GcMessage();
            gcMessage.setId(mid);
            service.deleteById(gcNotice.getId());
            gcMessageService.deleteById(gcMessage.getId());
            return new BaseResult(BaseResultEnum.SUCCESS,"数据删除成功");
        } catch (Exception e) {
            throw new RuntimeException("删除通知信息异常：" + e.getMessage());
        }
    }



}




