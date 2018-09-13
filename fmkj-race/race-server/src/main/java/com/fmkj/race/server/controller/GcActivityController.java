package com.fmkj.race.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.race.dao.domain.*;
import com.fmkj.race.dao.queryVo.GcBaseModel;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.*;
import com.fmkj.race.server.util.CalendarTime;
import com.fmkj.race.server.util.GlobalConstants;
import com.fmkj.race.server.util.SensitiveWordTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：16:28 2018/8/29 0029
 * @ Description：活动控制层
 */
@RestController
@RequestMapping("/gcactivity")
@DependsOn("springContextHandler")
@Api(tags ={ "活动服务"},description = "活动服务接口-网关路径/api-race")
public class GcActivityController extends BaseController<GcActivity,GcActivityService> implements BaseApiService<GcActivity>{

    @Autowired
    private GcActivityService gcActivityService;//活动接口

    //session接口
    @Autowired
    private HcSessionService hcSessionService; //session接口

    @Autowired
    private BmListService bmListService;//用户黑白名单接口

    @Autowired
    private GcPimageService gcPimageService;//活动图片接口

    @Autowired
    private GcMessageService gcMessageService;//信息接口

    @Autowired
    private GcNoticeService gcNoticeService;//通知接口  ,@RequestParam MultipartFile[] file   @RequestParam Map<String,Object> map,



    @ApiOperation(value="发起活动", notes="用户发起活动")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "用户发起活动")
    @RequestMapping(value = "/startActivityByExample", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  BaseResult<Map<String,Object>> startActivityByExample(@RequestParam Map<String,Object> map,HttpServletRequest request) throws IOException {

        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");//获取文件

        //获取参数列表
        Integer startid = Integer.parseInt(map.get("startid").toString());//活动发起人id
        String name = (String) map.get("name");//活动名称
        String pname = (String) map.get("pname");//产品的名称
        String pdescribe = (String) map.get("pdescribe");//产品的描述详情
        Integer pnumber = Integer.parseInt(map.get("pnumber").toString());//产品的数量
        String jiage = map.get("price").toString();
        Double price = new Double(jiage);//产品价格
        Integer num = Integer.parseInt( map.get("num").toString());//参与活动人数
        Integer par = Integer.parseInt(map.get("par").toString());//票面值
        Integer typeid = Integer.parseInt( map.get("typeid").toString());//对应活动类型
        String yijia = map.get("premium").toString();
        Double premium = new Double(yijia );//产品的溢价率
        String type = (String) map.get("type");//活动类型

        //判断用户是否黑名单
        BmList bl = new BmList();
        bl.setStatus(2);
        bl.setUid(startid);
        EntityWrapper<BmList> wrapper = new EntityWrapper<BmList>();
        wrapper.setEntity(bl);
        List<BmList> list = bmListService.selectList(wrapper);
        if(list.size()>0) {
            return new BaseResult(BaseResultEnum.ERROR.status, "您已被拉入黑名单,黑名单用户不可发起活动!",null);
        }

        //判断活动名称是否包含敏感词汇
        if(name.trim()!=null && !"".equals(name.trim())) {
            String wordIsOk= SensitiveWordTest.replaceBadWord(name.trim(),2,"*");
            System.err.println("name="+name);
            System.err.println("wordIsOk="+wordIsOk);
            if(!name.trim().equals(wordIsOk.trim())) {
                return new BaseResult(BaseResultEnum.ERROR.status, "活动名称含有敏感词汇，请重新写一个吧!",null);
            }
        }

        //判断产品的描述详情是否包含敏感词汇
        if(pdescribe!=null && !"".equals(pdescribe)) {
            String wordIsOk= SensitiveWordTest.replaceBadWord(pdescribe.trim(),2,"*");
            if(!pdescribe.equals(wordIsOk.trim())) {
                return new BaseResult(BaseResultEnum.ERROR.status, "产品的描述详情含有敏感词汇，请重新写一个吧!",null);
            }
        }

        //判断产品名称是否包含敏感词汇
        if(pname!=null && !"".equals(pname)) {
            String wordIsOk= SensitiveWordTest.replaceBadWord(pname.trim(),2,"*");
            if(!pname.equals(wordIsOk.trim())) {
                return new BaseResult(BaseResultEnum.ERROR.status, "产品名称含有敏感词汇，请重新写一个吧!",null);
            }
        }

        GcActivity ga = new GcActivity();
        ga.setStartid(startid);
        ga.setName(name);
        ga.setPname(pname);
        ga.setTypeid(typeid);
        ga.setPnumber(pnumber);
        ga.setPrice(price);
        ga.setPremium(premium);
        EntityWrapper wrapper1 = new EntityWrapper();
        wrapper1.setEntity(ga);
        GcActivity activity1 = gcActivityService.selectOne(wrapper1);//判断相同活动是否存在
        if(activity1 != null) {
            return new BaseResult(BaseResultEnum.ERROR.status, "发起活动失败，该活动已存在!",null);
        }

        CalendarTime clt = new CalendarTime();
        Timestamp btime = clt.thisDate();//获取当前时间

        ga.setPdescribe(pdescribe);
        ga.setTime(btime);
        ga.setStatus(0);
        ga.setDelivergoodstatus(0);
        ga.setNum(num);
        ga.setCollectgoodstatus(0);
        boolean result = gcActivityService.insert(ga);
        if(!result) {
            return new BaseResult(BaseResultEnum.ERROR.status, "发起活动失败，填入信息格式有误!",null);
        }

        //插入信息
        GcMessage gcMessage = new GcMessage();
        gcMessage.setTime(btime);
        gcMessage.setMessage("您已发起了"+type+"溢价活动，系统审核通过后，活动完成系统将扣除相应手续费后的资产包发送到您的账户，详情请查询活动发起规则。");
        gcMessage.setType(0);
        gcMessageService.insert(gcMessage);

        //插入通知表
        EntityWrapper wrapper3= new EntityWrapper();
        wrapper3.setEntity(gcMessage);
        GcMessage gcMessage1 = gcMessageService.selectOne(wrapper3);
        GcNotice gn = new GcNotice();
        gn.setFlag(1);
        gn.setUid(startid);
        gn.setMid(gcMessage1.getId());
        gcNoticeService.insert(gn);

        //上传活动的文件
        if(files.size()> 0) {
            EntityWrapper wrapper2 = new EntityWrapper();
            wrapper2.setEntity(ga);
            GcActivity activity2 = gcActivityService.selectOne(wrapper2);
            Integer aid = activity2.getId();
            GlobalConstants globalConstants = new GlobalConstants();
            String url = globalConstants.getInterfaceUrl("activityImagePath");

            int i = 1;
            MultipartFile file = null;
            for (int j=0;j<files.size();j++){
                file = files.get(j);
                String fileName = globalConstants.uploadImage(file, url);
                GcPimage gp = new GcPimage();
                gp.setAid(aid);
                gp.setFlag(i++);
                gp.setImageurl(globalConstants.getInterfaceUrl("activityImageIpPath")+fileName);
                gcPimageService.insert(gp);
            }
        }
        return new BaseResult(BaseResultEnum.SUCCESS.status, "活动发起成功!",null);
    }




    //  活动广场分页查询所有活动,只查询活动中(status=2)
    @ApiOperation(value="活动广场分页查询所有活动", notes="活动广场分页查询所有活动")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "活动广场分页查询所有活动")
    @PutMapping("/queryAllActivityByPage")
    public BaseResult queryAllActivityByPage(@RequestBody GcBaseModel gcBaseModel){
        try {
            //活动广场分页查询所有活动,只查询活动中(status=2)
            List<HashMap<String, Object>> list = gcActivityService.queryAllActivityByPage(gcBaseModel);
            if(list.size()==0) {
                return new BaseResult(BaseResultEnum.ERROR.status, "暂无活动!",null);
            }
            return new BaseResult(BaseResultEnum.SUCCESS,list);
        } catch (Exception e) {
            throw new RuntimeException("活动广场分页查询所有活动异常：" + e.getMessage());
        }
    }




    @ApiOperation(value="传入活动id查询活动详情", notes="传入活动id查询活动详情")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "传入活动id查询活动详情")
    @PutMapping("/queryActivityById")
    public BaseResult<HashMap<String,Object>> queryActivityById(@RequestBody GcBaseModel gcBaseModel){

        HashMap<String, Object> map = gcActivityService.queryActivityById(gcBaseModel);
        return new BaseResult(BaseResultEnum.SUCCESS,map);
    }




    @ApiOperation(value="传入活动id查询活动产品的所有图片", notes="传入活动id查询活动产品的所有图片")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "传入活动id查询活动产品的所有图片")
    @PutMapping("/queryActivityImageById")
    public BaseResult queryActivityImageById(@RequestBody GcBaseModel gcBaseModel){

        List<GcPimage> list = gcPimageService.queryActivityImageById(gcBaseModel);
        return new BaseResult(BaseResultEnum.SUCCESS,list);

    }




    //传入uid查询用户参与的活动    status：1进行中  2已锤到的  3.已结束
    @ApiOperation(value="传入uid查询用户参与的活动", notes="传入uid查询用户参与的活动")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "传入uid查询用户参与的活动")
    @PutMapping("/queryMyJoinActivityByUid")
    public BaseResult queryMyJoinActivityByUid(@RequestBody GcBaseModel gcBaseModel){
        List<HashMap<String, Object>> list = gcActivityService.queryMyJoinActivityByUid(gcBaseModel);
        return new BaseResult(BaseResultEnum.SUCCESS,list);

    }




    /**
     *  传入uid查询用户发起的活动    status：0:待审核 1:驳回 2:活动中 3：已结束 4：活动异常 5：活动失败
     */
    @ApiOperation(value="传入uid查询用户发起的活动 ", notes="传入uid查询用户发起的活动 ")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "传入uid查询用户发起的活动 ")
    @PutMapping("/queryMyStartActivityByUid")
    public BaseResult queryMyStartActivityByUid(@RequestBody GcBaseModel gcBaseModel){
        List<HashMap<String, Object>> list = gcActivityService.queryMyStartActivityByUid(gcBaseModel);
        return new BaseResult(BaseResultEnum.SUCCESS,list);

    }





}
