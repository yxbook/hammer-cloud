package com.fmkj.race.server.api;

import com.fmkj.user.dao.domain.HcAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="FMKJ-USER", fallback = HcAccountApi.HcAccountApiFallBack.class)
public interface HcAccountApi {

    @PostMapping("/hcAccount/updateUserP")
    public Boolean updateUserP(@RequestParam("id") Integer id, @RequestParam("par") double par);

    @PostMapping("/hcAccount/grantUserP")
    public Boolean grantUserP(@RequestParam("id") Integer id, @RequestParam("par") double par);

    @PutMapping("/hcAccount/getAccountById")
    public HcAccount selectHcAccountById(@RequestParam("id") Integer id);

    @Component
    public static class HcAccountApiFallBack implements HcAccountApi {


        @Override
        public Boolean updateUserP(Integer id,  double par) {
            return null;
        }

        @Override
        public Boolean grantUserP(Integer id, double par) {
            return null;
        }

        @Override
        public HcAccount selectHcAccountById(Integer id) {
            return null;
        }
    }
}
