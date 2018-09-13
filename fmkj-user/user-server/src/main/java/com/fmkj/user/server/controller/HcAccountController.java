package com.fmkj.user.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.validator.LengthValidator;
import com.fmkj.common.validator.NotNullValidator;
import com.fmkj.user.dao.domain.HcAccount;
import com.fmkj.user.dao.domain.HcSession;
import com.fmkj.user.server.annotation.UserLog;
import com.fmkj.user.server.service.HcAccountService;
import com.fmkj.user.server.service.HcSessionService;
import com.fmkj.user.server.util.CalendarTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
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
@DependsOn("springContextHoldler")
@Api(tags ={ "用户信息"},description = "用户信息接口-网关路径/api-user")
public class HcAccountController extends BaseController<HcAccount, HcAccountService> implements BaseApiService<HcAccount> {


//    使用快捷键CTRL+K,就会弹出提交的界面，点击Commit and Push即可
//    点击快捷键Ctrl+T，就会弹出更新的界面，点击OK即可
//    在idea中添加try/catch的快捷键  ctrl+alt+t
    // 切换大小写  ctrl+sh+u
    //IDEA中自动生成get/set的方式:快捷键为：alt+insert

    //用户表接口
    @Autowired
    private HcAccountService hcAccountService;

    //session接口
    @Autowired
    private HcSessionService hcSessionService;

    //Swagger API文档，启动类加入注解@EnableSwagger2
    //访问http://localhost:8080/swagger-ui.html即可、这里先不需要
    @ApiOperation(value="查询HcAccount用户信息", notes="分页查询用户信息")
    @GetMapping(value = "selectPage")
    public BaseResult<Page<HcAccount>> selectPage(@RequestParam Map<String, Object> params) {
        Query<HcAccount> query = new Query<HcAccount>(params);
        // 参数校验
        ComplexResult validatResult = FluentValidator.checkAll()
                .on((String) params.get("telephone"), new LengthValidator(1, 13, "电话号码"))
                .on((String) params.get("email"), new NotNullValidator("邮箱"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!validatResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, validatResult.getErrors().get(0).getErrorMsg());
        }

        Page<HcAccount> tPage =new Page<HcAccount>(query.getPageNo(),query.getPageSize());
        tPage.setSearchCount(true);

        Page<HcAccount> result = hcAccountService.selectPage(tPage, query.getEntityWrapper());

        /*List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        Page<HcAccount> result = hcAccountService.selectPage(tPage, new EntityWrapper<HcAccount>().in("id", ids));*/


        return new BaseResult<Page<HcAccount>>(BaseResultEnum.SUCCESS, result);
    }




    /**
     * @author yangshengbin
     * @Description：用户通过电话号码和密码进行登录
     * @date 2018/8/29 0029 15:39
     * @param params
     * @return com.fmkj.common.base.BaseResult<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/loginByPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResult<Map<String,Object>> loginByPassword(@RequestBody Map<String, Object> params){

        Map<String, Object> map = new HashMap<String, Object>();
        String pwd = null;
        String telephone = null;
        try {
            pwd = (String) params.get("password");
            telephone = (String) params.get("telephone");
        } catch (Exception e) {
            return new BaseResult(BaseResultEnum.ERROR.status,"电话号码或密码传入有误!",null);
        }
        pwd = DigestUtils.md5Hex(pwd);
        if (pwd == null || "".equals(pwd) || telephone == null || "".equals(telephone)) {
            return new BaseResult(BaseResultEnum.ERROR.status,"你的用户名或密码为空!",null);
        }
        //判断用户电话号码和密码是否匹配
        HcAccount hca = new HcAccount();
        map.put("telephone",telephone);
        map.put("password",pwd);
        List<HcAccount> account = hcAccountService.selectByMap(map);
        if (account.size()<=0||account == null) {
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
        if (row>0) {
            EntityWrapper<HcSession> wra = new EntityWrapper<HcSession>();
            wra.setEntity(session);
            isupdate = hcSessionService.update(hcSession, wra);  //update第一个参数为修改的参数,第二个为条件
            if (!isupdate) {
                System.err.println("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
            }
        }else {
            boolean result = hcSessionService.insert(hcSession);
            if(!result) {
                System.err.println("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
            }
        }

        // 登录成功把用户的登录状态字段改为authlock=1
        account.get(0).setAuthlock(true);
        boolean flg = hcAccountService.updateBatchById(account);
        if (!flg) {
            System.err.println("error:hc_session表更新失败或用户状态更改失败");
            return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",null);
        }

        map.put("token", token);
        map.put("uid", account.get(0).getId()+"");
        return new BaseResult(BaseResultEnum.SUCCESS.status,"用户名密码正确，登录成功!",map);
    }




    @ApiOperation(value="查询最新一条中奖用户信息", notes="查询最新一条中奖用户信息")
    @UserLog(module= LogConstant.Gc_Activity, actionDesc = "查询最新一条中奖用户信息")
    @PutMapping("/queryOneNewNotice")
    public BaseResult queryOneNewNotice(){
        try {
            List<Map<String,Object>> map = hcAccountService.queryOneNewNotice();
            return new BaseResult(BaseResultEnum.SUCCESS,map);
        } catch (Exception e) {
            throw new RuntimeException("查询最新一条中奖用户信息异常：" + e.getMessage());
        }
    }



}
