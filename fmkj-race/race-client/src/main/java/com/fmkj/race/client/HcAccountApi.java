package com.fmkj.race.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="FMKJ-USER", fallback = HcAccountApi.HcAccountApiFallBack.class)
public interface HcAccountApi {

    //作为客户端去调用race里面的selectPage方法、通过@GetMapping("/race/selectPage")匹配到
    //这里注意路径一定要写对
    @PutMapping("/hcPermiss/getToken")
    Boolean queryToken(String token);

    @Component
    public static class HcAccountApiFallBack implements HcAccountApi {

        @Override
        public Boolean queryToken(String token) {
            return null;
        }
    }
}
