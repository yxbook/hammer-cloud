package com.fmkj.race.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.validator.LengthValidator;
import com.fmkj.common.validator.NotNullValidator;
import com.fmkj.race.dao.domain.HcAccount;
import com.fmkj.race.dao.domain.HcSession;
import com.fmkj.race.server.service.HcAccountService;
import com.fmkj.race.server.service.HcSessionService;
import com.fmkj.race.server.util.CalendarTime;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/race")
//@Api(tags ={ "用户信息"},description = "HcAccountController")
public class HcAccountController extends BaseController<HcAccount, HcAccountService> implements BaseApiService<HcAccount> {

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
    * @Description:  用户通过电话号码和密码进行登录
    * @Param:  params
    * @return:  
    * @Author: 杨胜彬 
    * @Date: 2018/8/27 0027 
    */
//    @PostMapping(value = "loginByPassword")
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
//        map.put("password",pwd);
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
        System.err.println("uid="+uid);
        //创建hcsession对象存放session值
        HcSession hcSession = new HcSession();
        hcSession.setToken(token);
        hcSession.setBtime(btime);
        hcSession.setEtime(etime);
        hcSession.setLtime(ltime);
        hcSession.setUid(uid);

        // 更新hc_session表，
        EntityWrapper<HcSession> wrapper = new EntityWrapper<HcSession>();
        wrapper.setEntity(hcSession);
        int row = hcSessionService.selectCount(wrapper);
        boolean isupdate = false;
        if (row>0) {
            isupdate = hcSessionService.updateById(hcSession);//更新session表
            if (!isupdate) {
                System.err.println("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",false);
            }
        }else {
            boolean result = hcSessionService.insert(hcSession);
            if(!result) {
                System.err.println("error:hc_session表更新失败或用户状态更改失败");
                return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",false);
            }
        }

        // 登录成功把用户的登录状态字段改为authlock=1
        account.get(0).setAuthlock(true);
        boolean flg = hcAccountService.updateBatchById(account);
        if (!flg) {
            System.err.println("error:hc_session表更新失败或用户状态更改失败");
            return new BaseResult(BaseResultEnum.ERROR.status,"系统错误，请重新登录!",false);
        }

        map.put("token", token);
        map.put("uid", account.get(0).getId()+"");
        return new BaseResult(BaseResultEnum.SUCCESS.status,"用户名密码正确，登录成功!",map);
    }

}
