package com.fmkj.race.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="FMKJ-USER", fallback = BmListApi.HcTokenClientFallBack.class)
public interface BmListApi {

    @PostMapping("/bmList/isBlacker")
    Boolean isActivityBlack(Integer id, Integer status);

    @Component
    public static class HcTokenClientFallBack implements BmListApi {

        @Override
        public Boolean isActivityBlack(Integer id, Integer status) {
            return null;
        }
    }
}
