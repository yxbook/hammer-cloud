package com.fmkj.race.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="FMKJ-USER", fallback = BmListApi.HcTokenClientFallBack.class)
public interface BmListApi {

    @GetMapping("/bmList/isBlacker")
    Boolean isActivityBlack(@RequestParam("uid") Integer uid, @RequestParam("status") Integer status);

    @Component
    public static class HcTokenClientFallBack implements BmListApi {
        @Override
        public Boolean isActivityBlack(Integer uid, Integer status) {
            return false;
        }
    }
}
