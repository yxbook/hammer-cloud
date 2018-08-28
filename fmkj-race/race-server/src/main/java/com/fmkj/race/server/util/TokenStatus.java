/*
package com.fmkj.race.server.util;


import com.fmkj.race.server.service.HcSessionService;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * @program: fmkj
 * @description: Token验证工具类
 * @author: 杨胜彬
 * @create: 2018-08-27 11:53
 **//*

public class TokenStatus {



    */
/**
     * @Description:  获取token状态
     * @Param:  token,map,hcsessionService
     * @return:  Boolean
     * @Author: 杨胜彬
     * @Date: 2018/8/27 0027
     *//*

    public static Boolean getStatus(String token,HcSessionService hcSessionService) {
        Map<String,Object> map = new HashMap<>();
        if (token == null || "".equals(token)) {
            map.put("message", "token为空");
            map.put("code", "700");
            return false;
        }

        boolean flag = hcSessionService.userIsExpire(token);
        if (!flag) {
            map.put("message", "你的token不存在或过期,请重新登录");
            map.put("code", "700");
            return false;
        }
        return true;
    }
}
*/
