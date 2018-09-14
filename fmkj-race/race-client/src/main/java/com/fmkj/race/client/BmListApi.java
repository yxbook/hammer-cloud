package com.fmkj.race.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;

@FeignClient(name="FMKJ-USER", fallback = BmListApi.HcTokenClientFallBack.class)
public interface BmListApi {

    @PostMapping("/bmList/isBlacker")
    Boolean isActivityBlack(HashMap<String, Object> params);

    @Component
    public static class HcTokenClientFallBack implements BmListApi {

        @Override
        public Boolean isActivityBlack(HashMap<String, Object> params) {
            return null;
        }
    }
}
