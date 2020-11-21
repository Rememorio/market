package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.util.ParamIllegalException;
import org.apache.log4j.Logger;
import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.service.MyStoreService;
import com.newbee.maggie.util.ParamNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/myStore")
public class MyStoreController {
    @Autowired
    private MyStoreService myStoreService;

    private final Logger logger = Logger.getLogger(MyStoreController.class);

    /**
     * 根据userId获取这个用户出售的商品
     * @param userIdMap userId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/getMyGoods", method = RequestMethod.POST)
    private Map<String, Object> myGoods(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException {
        logger.info("——————————执行请求我的商店列表——————————");
        Integer userId = userIdMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求我的商店列表");
        List<Commodities> cmsList = myStoreService.getCmsListByUserId(userId);
        //封装信息
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", 0);
        map.put("commodityList", cmsList);
        logger.info("返回信息：" + map);
        logger.info("————————————————————");
        return map;
    }

    // 外网地址
    private static final String SERVER_ADDRESS = "http://maggiemarket.design:8081";
    // 本地地址
    private static final String LOCAL_ADDRESS = "C:/xampp/tomcat/webapps";

    /**
     * 上传文件
     * @param request 请求体
     * @param file 图片文件
     * @return map
     * @throws IOException 读写异常
     * @throws ParamNotFoundException 参数缺失
     * @throws ParamIllegalException 参数不合法
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public Map<String, Object> uploadImg(HttpServletRequest request, @RequestParam(value = "img", required = false) MultipartFile file) throws IOException, ParamNotFoundException, ParamIllegalException {
        request.setCharacterEncoding("UTF-8");
        logger.info("——————————执行请求图片上传——————————");
        String userId = request.getParameter("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId:" + userId);
        if (file.isEmpty()) {
            throw new ProtocolException("img文件为空");
        }
        Map<String, Object> map = new HashMap<>();
        if (!file.isEmpty()) {
            logger.info("成功获取照片");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            assert fileName != null;
            type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            logger.info("图片初始名称为：" + fileName + " 类型为：" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JEPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    //String realPath = request.getSession().getServletContext().getRealPath("/");
                    String realPath = LOCAL_ADDRESS + "/picture/upload";
                    // 自定义的文件名称
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss");
                    String uploadTime = ft.format(date);
                    String trueFileName = "userId=" + userId + "_uploadTime=" + uploadTime + "_systemTime=" + System.currentTimeMillis() + "." + type;
                    // 设置存放图片文件的路径
                    path = realPath + "/" + trueFileName;
                    logger.info("存放图片文件的路径:" + path);
                    file.transferTo(new File(path));
                    logger.info("文件成功上传到指定目录下");
                    map.put("errorCode", 0);
                    // 生成服务器端的地址
                    String address = SERVER_ADDRESS + "/picture/upload/" + trueFileName;
                    map.put("url", address);
                    logger.info("返回信息：" + map);
                    logger.info("————————————————————");
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
     * @param urlMap url数组
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/deleteImgs", method = RequestMethod.POST)
    public Map<String, Object> deleteImgs(@RequestBody Map<String, Object> urlMap) throws ParamNotFoundException  {
        logger.info("执行图片删除");
        List<String> urlList = (List<String>) urlMap.get("url");
        if (urlList == null) {
            throw new ParamNotFoundException("url数组为空");
        }
        int successCount = 0;
        int failureCount = 0;
        for (String url : urlList) {
            String urlAddress = url.replaceFirst(SERVER_ADDRESS, LOCAL_ADDRESS);
            System.out.println(urlAddress);
            File file = new File(urlAddress);
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                if (file.delete()) {
                    logger.info("删除" + urlAddress + "成功");
                    successCount++;
                } else {
                    logger.info("删除" + urlAddress + "失败");
                    failureCount++;
                }
            } else {
                logger.info("删除" + urlAddress + "时图片不存在");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", 0);
        map.put("successCount", successCount);
        map.put("failureCount", failureCount);
        logger.info("返回信息：" + map);
        logger.info("————————————————————");
        return map;
    }

    /**
     * 发布商品
     * @param cmMap 商品信息
     * @return map
     * @throws ParamNotFoundException 参数缺失
     * @throws ParamIllegalException 参数不合法
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Map<String, Object> publishCommodity(@RequestBody Map<String, Object> cmMap) throws ParamNotFoundException, ParamIllegalException {
        logger.info("——————————执行请求发布商品——————————");
        String name = (String) cmMap.get("name");
        if (name == null) {
            throw new ParamNotFoundException("name参数为空");
        }
        Integer classify = (Integer) cmMap.get("classify");
        if (classify == null) {
            throw new ParamNotFoundException("classify参数为空");
        }
        String details = (String) cmMap.get("details");
        if (details == null) {
            throw new ParamNotFoundException("details参数为空");
        }
        String priceTemp = (String) cmMap.get("price");
        if (priceTemp == null) {
            throw new ParamNotFoundException("price参数为空");
        }
        Double price = Double.valueOf(priceTemp);
        if (price.equals(0.0) || price < 0) {
            throw new ParamIllegalException("price参数必须为正数");
        }
        Integer userId = (Integer) cmMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer address = (Integer) cmMap.get("address");
        if (address == null) {
            throw new ParamNotFoundException("address参数为空");
        }
        List<String> pictureUrls = (List<String>) cmMap.get("pictureUrls");
        if (pictureUrls == null) {
            throw new ParamNotFoundException("pictureUrls参数为空");
        }
        Integer isNew = (Integer) cmMap.get("isNew");
        if (isNew == null) {
            throw new ParamNotFoundException("isNew参数为空");
        }
        logger.info("userId = " + userId + "正在请求发布商品");
        Commodity commodity = new Commodity(name, classify, details, price, userId, address, pictureUrls, isNew);
        Map<String, Object> map = new HashMap<>();
        if (myStoreService.addCommodity(commodity)) {
            map.put("errorCode", 0);
            map.put("cmId", commodity.getCmId());
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 修改商品
     * @param cmMap 商品信息
     * @return map
     * @throws ParamNotFoundException 参数缺失
     * @throws ParamIllegalException 参数不合法
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modifyCommodity(@RequestBody Map<String, Object> cmMap) throws ParamNotFoundException, ParamIllegalException {
        logger.info("——————————执行请求修改商品——————————");
        Integer cmId = (Integer) cmMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        String name = (String) cmMap.get("name");
        if (name == null) {
            throw new ParamNotFoundException("name参数为空");
        }
        Integer classify = (Integer) cmMap.get("classify");
        if (classify == null) {
            throw new ParamNotFoundException("classify参数为空");
        }
        String details = (String) cmMap.get("details");
        if (details == null) {
            throw new ParamNotFoundException("details参数为空");
        }
        String priceTemp = (String) cmMap.get("price");
        if (priceTemp == null) {
            throw new ParamNotFoundException("price参数为空");
        }
        Double price = Double.valueOf(priceTemp);
        if (price.equals(0.0) || price < 0) {
            throw new ParamIllegalException("price参数必须为正数");
        }
        Integer userId = (Integer) cmMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer address = (Integer) cmMap.get("address");
        if (address == null) {
            throw new ParamNotFoundException("address参数为空");
        }
        List<String> pictureUrls = (List<String>) cmMap.get("pictureUrls");
        if (pictureUrls == null) {
            throw new ParamNotFoundException("pictureUrls参数为空");
        }
        Integer isNew = (Integer) cmMap.get("isNew");
        if (isNew == null) {
            throw new ParamNotFoundException("isNew参数为空");
        }
        logger.info("userId = " + userId + "正在请求修改商品");
        Commodity commodity = new Commodity(cmId, name, classify, details, price, userId, address, pictureUrls, isNew);
        Map<String, Object> map = new HashMap<>();
        if (myStoreService.updateCommodity(commodity)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 下架商品
     * @param idMap cmId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     * @throws ParamIllegalException 参数不合法
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public Map<String, Object> withdrawCommodity(@RequestBody Map<String, Object> idMap) throws ParamNotFoundException, ParamIllegalException {
        logger.info("——————————执行下架商品请求——————————");
        Integer cmId = (Integer) idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Integer userId = (Integer) idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在下架cmId = " + cmId + "的商品");
        //判断是不是这个用户的商品
        Integer realUserId = myStoreService.getUserIdByCmId(cmId);
        if (!userId.equals(realUserId)) {
            throw new ParamIllegalException("该用户不是这件商品的卖家，无权删除");
        }
        //删除商品
        Map<String, Object> map = new HashMap<>();
        if (myStoreService.deleteCommodity(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }
}
