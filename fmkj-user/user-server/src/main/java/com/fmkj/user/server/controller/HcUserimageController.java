package com.fmkj.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fmkj.common.base.BaseApiService;
import com.fmkj.common.base.BaseController;
import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.fmkj.common.constant.LogConstant;
import com.fmkj.common.util.PropertiesUtil;
import com.fmkj.common.util.StringUtils;
import com.fmkj.user.dao.domain.HcUserimage;
import com.fmkj.user.server.annotation.UserLog;
import com.fmkj.user.server.service.HcUserimageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/hcUserimage")
@DependsOn("springContextUtil")
@Api(tags ={ "用户实名认证"},description = "用户实名认证接口-网关路径/api-user")
public class HcUserimageController extends BaseController<HcUserimage, HcUserimageService> implements BaseApiService<HcUserimage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HcUserimageController.class);

    @Autowired
    private HcUserimageService hcUserimageService;

    @ApiOperation(value="实名认证",httpMethod = "POST" ,notes="参数：id,cardnum,name,url,fullPhoto,reversePhoto")
    @UserLog(module= LogConstant.HC_CERT, actionDesc = "保存用户的实名信息")
    @PostMapping("/saveUserRealInfo")
    public BaseResult saveUserRealInfo(@PathParam(value = "id") Integer id,
                                       @PathParam(value = "name") String name,
                                       @PathParam(value = "cardnum") String cardnum) {
        if(StringUtils.isNull(id)){
            return  new BaseResult(BaseResultEnum.BLANK.status, "用户ID不能为空", false);
        }
        if(StringUtils.isNull(name)){
            return  new BaseResult(BaseResultEnum.BLANK.status, "用户姓名不能为空", false);
        }
        if(StringUtils.isNull(cardnum)){
            return  new BaseResult(BaseResultEnum.BLANK.status, "用户证件号码不能为空", false);
        }

        return hcUserimageService.saveUserRealInfo(id,name,cardnum);
    }


    @ApiOperation(value="上传照片",httpMethod = "POST" ,notes="参数：uid,file")
    @UserLog(module= LogConstant.HC_CERT, actionDesc = "上传身份证照片的正反面")
    @PostMapping("/uploadCardImage")
    public BaseResult uploadCardImage(@PathParam(value = "uid") Integer uid,
                                      @PathParam(value="status") Integer status,
                                      @RequestParam MultipartFile file) {
        try{
            if(StringUtils.isNull(uid)){
                return  new BaseResult(BaseResultEnum.BLANK.status, "用户ID不能为空", false);
            }
            if(StringUtils.isNull(status)){
                return  new BaseResult(BaseResultEnum.BLANK.status, "status不能为空", false);
            }
            HcUserimage userimage = new HcUserimage();
            userimage.setUid(uid);
            userimage.setUrl(PropertiesUtil.getInstance("user").get("userCodeImageIpPath"));

            String url=PropertiesUtil.getInstance("user").get("userCodeImagePath");

            String newFileName=PropertiesUtil.uploadImage(file,url);
            if(status ==1){
                userimage.setFullPhoto(PropertiesUtil.getInstance("user").get("userCodeImageIpPath")+newFileName);
            }else{
                userimage.setReversePhoto(PropertiesUtil.getInstance("user").get("userCodeImageIpPath")+newFileName);
            }

            HcUserimage hm = new HcUserimage();
            hm.setUid(uid);
            EntityWrapper<HcUserimage> wrapper = new EntityWrapper<>(hm);
            HcUserimage hcUserimage = hcUserimageService.selectOne(wrapper);
            boolean result = false;
            if(StringUtils.isNull(hcUserimage)){
                result = hcUserimageService.insert(userimage);
            }else{
                userimage.setId(hcUserimage.getId());
                result = hcUserimageService.updateById(userimage);
            }
            if(result){
                return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "上传成功!", true);
            }else {
                return new BaseResult(BaseResultEnum.SUCCESS.getStatus(), "上传失败!", false);
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

}
