package com.fmkj.race.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.StringUtils;
import com.fmkj.race.dao.domain.GcAddress;
import com.fmkj.race.server.annotation.RaceLog;
import com.fmkj.race.server.service.GcAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;


/**
 * @ Author     ：yangshengbin
 * @ Date       ：10:25 2018/9/3 0003
 * @ Description：用户地址控制层
 */
@RestController
@RequestMapping("/gcaddress")
@DependsOn("springContextHandler")
@Api(tags ={ "用户地址"},description = "用户地址接口-网关路径/api-race")
public class GcAddressController extends BaseController<GcAddress,GcAddressService> implements BaseApiService<GcAddress> {


    @Autowired
    private GcAddressService gcAddressService;//用户地址接口




    /**
     * @author yangshengbin
     * @Description： 新增用户地址
     * @date 2018/9/3 0003 10:29
     * @param  gcAddress
     * @return
    */
    @ApiOperation(value="新增用户地址", notes="新增用户地址")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "新增用户地址")
    @PostMapping("/insertNewAddress")
    public BaseResult insertNewAddress(@RequestBody GcAddress gcAddress) {
        try {
            if(StringUtils.isNull(gcAddress) || StringUtils.isNull(gcAddress.getUid())){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "UID不能为空", "UID不能为空");
            }
            gcAddress.setStatus(0);
            gcAddress.setLock(0);
            gcAddressService.insert(gcAddress);
            return new BaseResult(BaseResultEnum.SUCCESS,"数据添加成功");
        }
        catch (Exception e) {
            throw new RuntimeException("新增异常：" + e.getMessage());
        }
    }



    /**
     * @author yangshengbin
     * @Description：传入地址id删除地址
     * @date 2018/9/3 0003 10:55
     * @param gcAddress
     * @return
    */
    @ApiOperation(value="传入地址id删除地址", notes="传入地址id删除地址")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "传入地址id删除地址")
    @PostMapping("/deleteAddressById")
    public BaseResult deleteAddressById(@RequestBody GcAddress gcAddress){
        try {
            if( gcAddress.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            gcAddressService.deleteById(gcAddress.getId());
            return new BaseResult(BaseResultEnum.SUCCESS,"数据删除成功");
        } catch (Exception e) {
            throw new RuntimeException("删除异常：" + e.getMessage());
        }

    }


    @ApiOperation(value="修改地址", notes="修改地址")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "修改地址")
    @PostMapping("/updateAddressById")
    public BaseResult updateAddressById(@RequestBody GcAddress gcAddress){
        try {
            if( gcAddress.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            return super.updateById(gcAddress);
        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }



    @ApiOperation(value="修改默认地址", notes="修改默认地址")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "修改默认地址")
    @PostMapping("/updateAddressByStatus")
    public BaseResult updateAddressByStatus(@RequestBody GcAddress gcAddress){
        try {
            if( gcAddress.getId() == null){
                return new BaseResult(BaseResultEnum.BLANK.getStatus(), "ID不能为空", "ID不能为空");
            }
            /***********将用户所有地址重置为0************/
            GcAddress gcAddress1 = new GcAddress();
            gcAddress1.setStatus(0);
            EntityWrapper<GcAddress> wrapper = new EntityWrapper<GcAddress>();
            GcAddress gcAddress2 = new GcAddress();
            gcAddress2.setStatus(gcAddress.getUid());
            wrapper.setEntity(gcAddress2);
            try {
                gcAddressService.update(gcAddress1,wrapper);
            } catch (Exception e) {
                throw new RuntimeException("修改异常：" + e.getMessage());
            }
            /***********将用户所有地址重置为0************/

            //修改默认地址为1
            gcAddress.setStatus(1);
            return super.updateById(gcAddress);


        } catch (Exception e) {
            throw new RuntimeException("修改异常：" + e.getMessage());
        }
    }





    /**
     * @author yangshengbin
     * @Description：查询用户地址
     * @date 2018/9/3 0003 14:16
     * @param gcAddress
     * @return
     */
    @ApiOperation(value="查询用户所有收货地址", notes="查询用户所有收货地址")
    @RaceLog(module= LogConstant.Gc_Activity, actionDesc = "查询用户所有收货地址")
    @PutMapping("/queryAllAddressByUid")
    public BaseResult<Page<GcAddress>> queryAllAddressByUid(@RequestBody GcAddress gcAddress){
        try {
            Page<GcAddress> tPage =new Page<GcAddress>();
            GcAddress gcAddress1 = new GcAddress();
            gcAddress1.setUid(gcAddress.getUid());
            EntityWrapper<GcAddress> entityWrapper = new EntityWrapper<GcAddress>();
            entityWrapper.setEntity(gcAddress1);
            return new BaseResult(BaseResultEnum.SUCCESS,service.selectPage(tPage, entityWrapper));
        } catch (Exception e) {
            throw new RuntimeException("查询商品列表异常：" + e.getMessage());
        }
    }

}
