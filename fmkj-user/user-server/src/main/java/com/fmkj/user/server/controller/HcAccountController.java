package com.fmkj.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.DateUtil;
import com.fmkj.common.util.SensitiveWordUtil;
import com.fmkj.common.util.StringUtils;
import com.fmkj.user.dao.domain.*;
import com.fmkj.user.server.annotation.UserLog;
import com.fmkj.user.server.service.HcAccountService;
import com.fmkj.user.server.service.HcPointsRecordService;
import com.fmkj.user.server.service.HcRcodeService;
import com.fmkj.user.server.service.HcSessionService;
import com.fmkj.user.server.util.ALiSmsUtil;
import com.fmkj.user.server.util.CalendarTime;
import com.fmkj.user.server.util.JDWXUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/hcAccount")
@DependsOn("springContextUtil")
@Api(tags ={ "用户信息"},description = "用户信息接口-网关路径/api-user")
public class HcAccountController extends BaseController<HcAccount, HcAccountService> implements BaseApiService<HcAccount> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HcAccountController.class);

    //用户表接口
    @Autowired
    private HcAccountService hcAccountService;

    @Autowired
    private HcSessionService hcSessionService;

    @Autowired
    private HcPointsRecordService hcPointsRecordService;

    @Autowired
    private HcRcodeService hcRcodeService;

    private static HashMap<String, String> codeMap = new HashMap<String, String>();

    /**
     * @author yangshengbin
     * @Description：用户通过电话号码和密码进行登录
     * @date 2018/8/29 0029 15:39
     * @return com.fmkj.common.base.BaseResult<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @PostMapping(value = "/loginByPassword")
    public BaseResult<Map<String,Object>> loginByPassword(@RequestBody HcAccount hcAccount){
        String pwd = hcAccount.getPassword();
        String telephone = hcAccount.getTelephone();
        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(telephone)) {
            return new BaseResult(BaseResultEnum.ERROR.status,"用户名或密码为空!",null);
        }
        pwd = DigestUtils.md5Hex(pwd);
        //判断用户电话号码和密码是否匹配
        HcAccount hca = new HcAccount();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("telephone",telephone);
        map.put("password",pwd);
        List<HcAccount> account = hcAccountService.selectByMap(map);
        if (account.size()<=0 || StringUtils.isNull(account)) {
            return new BaseResult(BaseResultEnum.ERROR.status,"用户名或密码错误!",null);
        }

        //创建token及生存时间
        String token = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        CalendarTime clt = new CalendarTime();
        Timestamp btime = clt.thisDate();//获取当前时间
        Timestamp etime = clt.dateStartEed(30);// 获取30天后的时间
        Timestamp ltime = clt.thisDate();//当前时间

        Integer uid = account.get(0).getId();
        //创建hcsession对象存放session值
        HcSession hcSession = new HcSession();
        hcSession.setToken(token);
        hcSession.setBtime(btime);
        hcSession.setEtime(etime);
        hcSession.setLtime(ltime);

        // 更新hc_session表，
        HcSession session = new HcSession();
        session.setUid(uid);
        EntityWrapper<HcSession> wrapper = new EntityWrapper<HcSession>();
        wrapper.setEntity(session);
        int row = hcSessionService.selectCount(wrapper); //根据用户id查询是否存在记录
        boolean isupdate = false;
        if (row > 0) {
            EntityWrapper<HcSession> wra = new EntityWrapper<HcSession>();
            wra.setEntity(session);
            isupdate = hcSessionService.update(hcSession, wra);  //update第一个参数为修改的参数,第二个为条件
            if (!isupdate) {
                LOGGER.error("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
            }
        }else {
            hcSession.setUid(uid);
            boolean result = hcSessionService.insert(hcSession);
            if(!result) {
                LOGGER.error("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
            }
        }

        // 登录成功把用户的登录状态字段改为authlock=1
        account.get(0).setAuthlock(true);
        boolean flg = hcAccountService.updateBatchById(account);
        if (!flg) {
            LOGGER.error("error:hc_session表更新失败或用户状态更改失败");
            return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
        }

        map.put("token", token);
        map.put("uid", account.get(0).getId()+"");
        return new BaseResult(BaseResultEnum.SUCCESS.status,"用户名密码正确，登录成功!",map);
    }




    @ApiOperation(value="查询最新一条中奖用户信息", notes="查询最新一条中奖用户信息")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "查询最新一条中奖用户信息")
    @PutMapping("/queryOneNewNotice")
    public BaseResult queryOneNewNotice(){
        try {
            List<Map<String,Object>> map = hcAccountService.queryOneNewNotice();
            return new BaseResult(BaseResultEnum.SUCCESS,map);
        } catch (Exception e) {
            throw new RuntimeException("查询最新一条中奖用户信息异常：" + e.getMessage());
        }
    }


    //更改用户p能量
    @ApiOperation(value = "更改用户p能量", notes = "更改用户p能量")
    @UserLog(module = LogConstant.HC_ACCOUNT, actionDesc = "更改用户p能量")
    @PostMapping("/updateUserP")
    public Boolean updateUserP(Integer id, double par) {
        HcAccount account = hcAccountService.selectById(id);
        if (Double.doubleToLongBits(account.getMyP()) < Double.doubleToLongBits(par)) {
            return false;
        }
        double newMyp = account.getMyP() - par;//用户新的p能量
        account.setMyP(newMyp);
        boolean result = hcAccountService.updateById(account);
        return result;
    }

    @ApiOperation(value="发放p能量", notes="发放p能量")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "发放p能量")
    @PostMapping("/grantUserP")
    public Boolean grantUserP(Integer id, double starterP) {
        HcAccount account = hcAccountService.selectById(id);
        Double allP = null;
        if (account != null) {
            Double myP = account.getMyP();
            allP = myP + starterP;
            account.setMyP(allP);
            hcAccountService.updateById(account);
            return true;
        }
        return false;
    }

    @ApiOperation(value="根据ID获取用户", notes="根据ID获取用户")
    @GetMapping("/getAccountById")
    public HcAccount getAccountById(Integer id){
        HcAccount hc = hcAccountService.selectById(id);
        return hc;

    }

    @ApiOperation(value="用户验证码信息发送", notes="参数：telephone")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "用户验证码信息发送")
    @PostMapping("/sendDycode")
    public BaseResult sendDycode(@RequestBody HcAccount hcAccount) {
        String telephone = hcAccount.getTelephone();
        if(StringUtils.isEmpty(telephone)){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "电话号码不能为空", false);
        }
        // 发送短信验证码
        new Thread() {
            public void run() {
                try {
                    ALiSmsUtil.sendSMS(telephone,codeMap);
                } catch (Exception e) {
                    e.getMessage();
                }
            };
        }.start();
        return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "验证码发送成功!", true);
    }


    /**
     * 用户通过电话号码和短信动态码进行登录 - hsy
     *
     * @param req
     * @return
     */
    @ApiOperation(value="用户通过电话号码和短信动态码进行登录", notes="参数：telephone， dycode")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "用户通过电话号码和短信动态码进行登录")
    @PostMapping("/loginByTelephone")
    public BaseResult loginByTelephone(@RequestBody HcAccount ha) {
        String telephone = ha.getTelephone();
        if (StringUtils.isEmpty(telephone)) {
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "请填写正确的电话号码！", false);
        }
        String dycode = ha.getDycode();
        if (StringUtils.isEmpty(dycode)) {
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "请填写正确的验证码！", false);
        }
        String code = codeMap.get(telephone + "code");
        if (StringUtils.isEmpty(code) || !code.equals(ha.getDycode())) {
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "验证码错误！", false);
        }
        String codetime = codeMap.get(telephone + "time");
        long time = (new Date().getTime() - Long.parseLong(codetime)) / 1000;
        if (time >= 90) {
            System.gc();
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "验证码已过期，请重新获取！", false);
        }
        // 获取该电话号码的用户
        HashMap<String, Object> params = new HashMap<>();
        params.put("telephone", telephone);
        List<HcAccount> list = hcAccountService.selectByMap(params);
        if (list == null || list.size() == 0) {
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "验证码正确，电话号码不存在！", false);
        }
        // 获取token
        String token = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Timestamp btime = DateUtil.getNowInMillis(0L);// 获取当前时间戳
        Timestamp etime = DateUtil.getNowInMillis(24L * 3600L * 1000L * 30L);// 获取30天后的时间戳
        Timestamp ltime = DateUtil.getNowInMillis(0L);//
        HcSession hcSession = new HcSession();
        hcSession.setToken(token);
        hcSession.setBtime(btime);
        hcSession.setEtime(etime);
        hcSession.setLtime(ltime);
        hcSession.setUid(list.get(0).getId());
        // 更新hc_session表
        EntityWrapper<HcSession> wra = new EntityWrapper<HcSession>(hcSession);
        boolean isupdate = hcSessionService.update(hcSession, wra);
        // 登录成功把用户的登录状态字段改为authlock=1
        HcAccount hcAccount = list.get(0);
        hcAccount.setAuthlock(true);
        boolean flg = hcAccountService.updateById(hcAccount);
        if (!isupdate || !flg) {
            LOGGER.error("error:hc_session表更新失败或用户状态更改失败");
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "系统错误，请重新登录!", false);

        }
        System.gc();
        return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "登录成功!", true);
    }

    /**
     * 通过传入的用户字段和用户唯一id修改用户信息 - hsy
     */
    @ApiOperation(value="修改昵称", notes="参数：id， nickname")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "修改昵称")
    @PostMapping("/updateAccoutById")
    public BaseResult updateAccoutById(@RequestBody HcAccount ha) {
        if (StringUtils.isNull(ha) || StringUtils.isNull(ha.getId())) {
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", false);
        }
        String nickname = ha.getNickname();
        if (StringUtils.isNull(nickname)) {
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "昵称不能为空", false);
        }
        String wordIsOk= SensitiveWordUtil.replaceBadWord(nickname.trim(),2,"*");
        if(!nickname.equals(wordIsOk.trim())) {
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "昵称含有敏感词汇，请重新写一个吧!", false);
        }
        hcAccountService.updateById(ha);
        return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "修改成功!", true);
    }

     /**
     * 用户头像上传
     *
     * @throws IOException
     */
    @ApiOperation(value="用户头像上传", notes="参数：id， nickname")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "用户头像上传")
    @PostMapping("/uploadUserHead")
    public BaseResult uploadUserHead(@RequestBody HcUserhead hcUserhead){


        return null;
    }

    /**
     * 用户身份证号码与姓名验证 -hsy
     */
    @ApiOperation(value="用户身份证号码与姓名验证", notes="参数：id, cardnum， name")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "用户身份证号码与姓名验证")
    @PostMapping("/queryCarIdisOk")
    public BaseResult queryCarIdisOk(@RequestBody HcAccount ha) {
        if(StringUtils.isNull(ha) || StringUtils.isNull(ha.getId())){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空!", false);
        }
        if(StringUtils.isNull(ha.getCardnum())){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "身份证号码不能为空!", false);
        }
        if(StringUtils.isNull(ha.getName())){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "姓名不能为空!", false);
        }

        boolean updateUser = hcAccountService.updateById(ha);boolean result = JDWXUtil.cardRealName(ha);
        if(!result) {
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "身份认证失败!", false);
        }
        ha.setCardStatus(1);
        if (!updateUser) {
            return new BaseResult(BaseResultEnum.ERROR.getStatus(), "系统错误，请重试!", false);
        }
        return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "身份认证成功!", true);
    }


    /**
     * 用户签到
     *
     * @param ha
     */
    @ApiOperation(value="用户签到", notes="参数：id")
    @UserLog(module= LogConstant.HC_ACCOUNT, actionDesc = "用户签到")
    @PostMapping("/signIn")
    public BaseResult signIn(@RequestBody HcAccount hcAccount) {
        if(StringUtils.isNull(hcAccount) || StringUtils.isNull(hcAccount.getId())){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "用户ID不能为空!", false);
        }
        HcPointsRecord hcPointsRecord =  hcPointsRecordService.getHcPointsRecord(hcAccount.getId());
        if (StringUtils.isNull(hcPointsRecord)) {
            // 如果不存在今天签到的记录，那么就可以签到，插入签到记录
            HcPointsRecord record = new HcPointsRecord();
            record.setPointsId(1);
            record.setPointsNum(1D);
            record.setUid(hcAccount.getId());
            record.setTime(DateUtil.getNowInMillis(0L));
            hcPointsRecordService.insert(record);
            return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "签到成功!", true);
        }
        // 今天已经签到过了，不能在签到
        return new BaseResult(BaseResultEnum.BLANK.getStatus(), "您今天已经签到过了!", false);
    }

    /**
     * 查询邀请好友（推广）周排行榜
     *
     * @return
     */
    @ApiOperation(value="查询邀请好友（推广）周排行榜", notes="参数：id, cardnum， name")
    @PutMapping("/queryRankInWeek")
    public BaseResult queryRankInWeek(@RequestBody HcAccount hc) {
        if(StringUtils.isNull(hc) || StringUtils.isNull(hc.getId())){
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "用户ID不能为空!", false);
        }
        // 查询邀请码
        HcRcode hcRcode = new HcRcode();
        hcRcode.setUid(hc.getId());
        EntityWrapper<HcRcode> wrapper = new EntityWrapper<>(hcRcode);
        HcRcode rcode = hcRcodeService.selectOne(wrapper);
        if (StringUtils.isNull(rcode)) {
            return new BaseResult(BaseResultEnum.BLANK.getStatus(), "用户没有邀请码!", false);
        }
        // 查询邀请人来注册获得的总积分
        Integer pointNum = hcPointsRecordService.queryPoints(hc.getId());
        if (pointNum == null) {
            pointNum = 0;
        }

        // 查询邀请人来注册获得的总能量
        HcAccount hcAccount = new HcAccount();
        hcAccount.setRid(hc.getId());
        /*
        List<HcAccount> queryUserByExample = hcaccountService.queryUserByExample(hcAccount);
        // 每邀请到一个人得到1P，getPs就是获得的总能量
        Integer getAllP = queryUserByExample.size() * 1;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("points", points);
        hashMap.put("getAllP", getAllP);
        hashMap.put("getAllUser", queryUserByExample.size());
        hashMap.put("reode", rcode);
        // 查询用户的排名(邀请人数大于0)
        List<BaseBean> queryInvitingFriendsRankWeek1 = hcpointsrecordService.queryInvitingFriendsRankWeek(0,hcaccountService.queryAccountNum());
        if (queryInvitingFriendsRankWeek1 == null || queryInvitingFriendsRankWeek1.size() == 0) {//
            hashMap.put("position", "你本周未邀请人");// 你本周未邀请人
        }
        for (int i = 0; i < queryInvitingFriendsRankWeek1.size(); i++) {
            BaseBean BaseBean = queryInvitingFriendsRankWeek1.get(i);
            if (BaseBean.getUid() == hc.getId()) {
                hashMap.put("position", i + 1);
                break;
            }
            if (i == queryInvitingFriendsRankWeek1.size() - 1) {
                hashMap.put("position", "你本周未邀请人");
            }
        }

        // ·榜
        List<BaseBean> queryInvitingFriendsRankWeek = hcpointsrecordService.queryInvitingFriendsRankWeek(0,10);

        System.out.println("rank=="+queryInvitingFriendsRankWeek);
        HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
        hashMap2.put("rankCode", "200");
        hashMap2.put("rankMsg", queryInvitingFriendsRankWeek);
        map.put("rank", hashMap2);

        HashMap<String, Object> hashMap3 = new HashMap<String, Object>();
        hashMap3.put("myDataCode", "200");
        hashMap3.put("myDataMsg", hashMap);
        map.put("myData", hashMap3);*/
        return null;
    }

}
