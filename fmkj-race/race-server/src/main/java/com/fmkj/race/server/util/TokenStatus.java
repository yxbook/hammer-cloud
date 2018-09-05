
package com.fmkj.race.server.util;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.race.dao.domain.HcAccount;
import com.fmkj.race.dao.domain.HcSession;
import com.fmkj.race.server.service.HcAccountService;
import com.fmkj.race.server.service.HcSessionService;
import com.fmkj.race.server.service.RaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


/**
 * @program: fmkj
 * @description: Token验证工具类
 * @author: 杨胜彬
 * @create: 2018-08-27 11:53
 **/
@RestController
public class TokenStatus {


    HcSessionService hcSessionService = SpringContextHandler.getBean(HcSessionService.class);

    HcAccountService hcAccountService = SpringContextHandler.getBean(HcAccountService.class);

    /**
     * @Description:  获取token状态
     * @Param:  token,map,hcsessionService
     * @return:  Boolean
     * @Author: 杨胜彬
     * @Date: 2018/8/27 0027
     */
    public  Boolean getStatus(String token) {
        HcSession session = new HcSession();
        session.setToken(token);
        EntityWrapper<HcSession> wrapper = new EntityWrapper<HcSession>();
        wrapper.setEntity(session);
        HcSession one = null;
        try {
            one = hcSessionService.selectOne(wrapper);
        } catch (Exception e) {
            throw new RuntimeException("查询token异常：" + e.getMessage());
        }
        if (one == null) {
            System.err.println("token不存在");
            return false;// token不存在
        }
        Date etime = one.getEtime();
        CalendarTime clt = new CalendarTime();
        if (etime.getTime() - clt.thisDate().getTime() <= 0) {
            HcAccount hcAccount = new HcAccount();
            hcAccount.setAuthlock(false);

            HcAccount hc = new HcAccount();
            hc.setId(one.getUid());
            EntityWrapper<HcAccount> wra = new EntityWrapper<HcAccount>();
            wra.setEntity(hc);
            hcAccountService.update(hcAccount,wra);//把用户登录状态设为0

            Integer id = one.getId();
            HcSession hcSession1 = new HcSession();
            hcSession1.setToken("token expired");

            HcSession hess = new HcSession();
            hess.setId(id);
            EntityWrapper<HcSession> wrahe = new EntityWrapper<HcSession>();
            wrahe.setEntity(hess);
            hcSessionService.update(hcSession1,wrahe);// 将token设置为空
            System.err.println("token过期");
            return false;// 过期
        }
        return true;
    }
}
