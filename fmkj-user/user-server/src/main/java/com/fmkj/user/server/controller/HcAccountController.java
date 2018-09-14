package com.fmkj.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcSession;
import com.fmkj.user.server.annotation.UserLog;
import com.fmkj.user.server.service.HcAccountService;
import com.fmkj.user.server.service.HcSessionService;
import com.fmkj.user.server.util.CalendarTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                System.err.println("error:hc_session表更新失败或用户状态更改失败");
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
}
