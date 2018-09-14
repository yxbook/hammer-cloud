package com.fmkj.race.server.util;

import com.fmkj.common.util.PropertiesUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @ Author     ：yangshengbin
 * @ Date       ：09:48 2018/8/30 0030
 * @ Description：获取文件路径工具类
 */
public class GlobalConstants {
    /**
     * @author yangshengbin
     * @Description：获取文件路径
     * @date 2018/8/30 0030 09:49
     * @param key
     * @return java.lang.String
    */
    public  String getInterfaceUrl(String key) {
        String path = PropertiesUtil.getInstance("interface_url").get(key);//活动图片路径
        return path;
    }


    /**
     * @author yangshengbin
     * @Description：生成文件路径
     * @date 2018/8/30 0030 11:17
     * @param
     * @return
    */
    public  String uploadImage(MultipartFile file, String url) throws IOException {
        if (file != null && url != null) {
            new File(url).mkdirs();//创建文件夹(已存在则无效)
            UUID uuid = UUID.randomUUID();
            // 获取上传的文件的名称
            String filename = file.getOriginalFilename();
            //获取后缀
            String prefix=filename.substring(filename.lastIndexOf(".")+1);

            String originalFilename =uuid.toString().replaceAll("-", "");
            System.err.println("originalFilename="+originalFilename);
            Long time = new Date().getTime();//获取当前时间为标识
            String newFileName = time.toString()+originalFilename+ "." + prefix;;//新的文件名
            File targetFile = new File(url,newFileName); //封装上传文件位置的全路径
            file.transferTo(targetFile);//把本地文件上传到 封装上传文件位置的全路径 下
            return newFileName;//返回新的文件路径名+文件名
        }

        return null;
    }



}
