package com.newbee.maggie.controller;

import com.newbee.maggie.util.ParamIllegalException;
import org.apache.log4j.Logger;
import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.service.MyStoreService;
import com.newbee.maggie.util.ParamNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/myStore")
public class MyStoreController {
    @Autowired
    private MyStoreService myStoreService;

    private Logger logger = Logger.getLogger(MyStoreController.class);

    /**
     * 根据userId获取这个用户出售的商品
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/getMyGoods", method = RequestMethod.POST)
    private Map<String, Object> myGoods(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException {
        logger.info("执行请求我的商店列表");
        Integer userId = userIdMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求我的商店列表");
        List<Commodities> cmsList = new ArrayList<Commodities>();
        cmsList = myStoreService.getCmsListByUserId(userId);
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmsList);
        logger.info("返回信息：" + map);
        return map;
    }

    // 外网地址
    private static final String SERVERADDRESS = "http://maggiemarket.design:8081/upload";
    // 本地地址
    private static final String LOCALADDRESS = "C:/xampp/tomcat/webapps/upload";

    /**
     * 上传文件
     * @param request
     * @param file
     * @return
     * @throws IOException
     * @throws ParamNotFoundException
     * @throws ParamIllegalException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public Map<String, Object> uploadImg(HttpServletRequest request, @RequestParam(value = "img", required = false) MultipartFile file) throws IOException, ParamNotFoundException, ParamIllegalException {
        request.setCharacterEncoding("UTF-8");
        logger.info("执行图片上传");
        String userId = request.getParameter("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId:" + userId);
        if (file.isEmpty()) {
            throw new ProtocolException("img文件为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            logger.info("成功获取照片");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            logger.info("图片初始名称为：" + fileName + " 类型为：" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    //String realPath = request.getSession().getServletContext().getRealPath("/");
                    String realPath = LOCALADDRESS;
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + "_userId=" + userId + "&filename=" + fileName;
                    // 设置存放图片文件的路径
                    path = realPath + "/" + trueFileName;
                    logger.info("存放图片文件的路径:" + path);
                    file.transferTo(new File(path));
                    logger.info("文件成功上传到指定目录下");
                    map.put("errorCode", 0);
                    // 生成服务器端的地址
                    String address = SERVERADDRESS + "/" +trueFileName;
                    map.put("url", address);
                    logger.info("返回信息：" + map);
                    return map;
                } else {
                    logger.info("不是我们想要的文件类型,请按要求重新上传");
                    throw new ParamIllegalException("不是我们想要的文件类型（gif, png, jpg）,请按要求重新上传");
                }
            } else {
                logger.info("文件类型为空");
                throw new ParamIllegalException("文件后缀名为空");
            }
        } else {
            logger.info("没有找到相对应的文件");
            throw new ParamNotFoundException("img文件为空");
        }
    }

    /**
     * 删除文件
     * @param urlMap
     * @return
     * @throws ParamNotFoundException
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/deleteImgs", method = RequestMethod.POST)
    public Map<String, Object> deleteImgs(@RequestBody Map<String, Object> urlMap) throws ParamNotFoundException, FileNotFoundException {
        logger.info("执行图片删除");
        List<String> urlList = (List<String>) urlMap.get("url");
        if (urlList == null) {
            throw new ParamNotFoundException("url数组为空");
        }
        int successCount = 0;
        int failureCount = 0;
        for (String url: urlList) {
            String urlAddress = url.replaceFirst(SERVERADDRESS, LOCALADDRESS);
            System.out.println(urlAddress);
            File file = new File(urlAddress);
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                if(file.delete()) {
                    logger.info("删除" + urlAddress + "成功");
                    successCount++;
                } else {
                    logger.info("删除" + urlAddress + "失败");
                    failureCount++;
                }
            } else {
                throw new FileNotFoundException(url + "不存在");
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("successCount", successCount);
        map.put("failureCount", failureCount);
        logger.info("返回信息：" + map);
        return map;
    }

//    @RequestMapping(value = "/publish", method = RequestMethod.POST)
//    public Map<String, Object> publishCommodity(@RequestBody Map<String, Object> cmMap) throws ParamNotFoundException, ParamIllegalException {
//
//    }
}
