package com.lin.controller;

import com.lin.model.User;
import com.lin.model.UserReport;
import com.lin.model.vo.PublisherVideoVo;
import com.lin.service.UserService;
import com.lin.utils.JsonResult;
import com.lin.model.vo.UserVo;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lkmc2
 * @date 2019/1/31
 * @description 用户控制器
 */
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的Controller"})
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "avatar", value = "用户头像地址", required = true, dataType = "String", paramType = "query")
    })

    @PostMapping(value = "/uploadFace")
    public JsonResult uploadFace(String userId, String avatar) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }
        User user = new User();
        user.setId(userId);
        user.setFaceImage(avatar);
        // 更新用户信息
        userService.updateUserInfo(user);
        return JsonResult.ok("");
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/query")
    public JsonResult query(String userId, String fanId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        // 查询用户信息
        User userInfo = userService.queryUserInfo(userId);
        UserVo userVo = new UserVo();
        // 将用户信息复制到Vo对象
        BeanUtils.copyProperties(userInfo, userVo);

        // 设置该用户是否被关注
        userVo.setFollow(userService.queryIfFollow(userId, fanId));

        return JsonResult.ok(userVo);
    }

    @ApiOperation(value = "查询发布者信息", notes = "查询发布者信息的接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "loginUserId", value = "登陆用户id", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "publishUserId", value = "发布者id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/queryPublisher")
    public JsonResult queryPublisher(String loginUserId, String videoId, String publishUserId) {
        if (StringUtils.isBlank(publishUserId)) {
            return JsonResult.errorMsg("发布者id不能为空");
        }

        // 1.查询视频发布者的信息
        User userInfo = userService.queryUserInfo(publishUserId);
        UserVo publisher = new UserVo();
        // 将用户信息复制到Vo对象
        BeanUtils.copyProperties(userInfo, publisher);

        // 2.查询当前登陆者和视频的点赞关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

        // 发布者与视频VO
        PublisherVideoVo publisherVideoVo = new PublisherVideoVo();
        publisherVideoVo.setPublisher(publisher); // 设置视频发布者
        publisherVideoVo.setUserLikeVideo(userLikeVideo); // 设置用户是否给该视频点赞

        return JsonResult.ok(publisherVideoVo);
    }

    @ApiOperation(value = "关注用户", notes = "关注用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/beYourFans")
    public JsonResult beYourFans(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JsonResult.errorMsg("userId和fanId不能为空");
        }

        // 保存用户与粉丝的关系
        userService.saveUserFanRelation(userId, fanId);

        return JsonResult.ok("关注成功");
    }

    @ApiOperation(value = "取消关注用户", notes = "取消关注用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/dontBeYourFans")
    public JsonResult dontBeYourFans(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JsonResult.errorMsg("userId和fanId不能为空");
        }

        // 删除用户与粉丝的关系
        userService.deleteUserFanRelation(userId, fanId);

        return JsonResult.ok("取消关注成功");
    }

    @ApiOperation(value = "举报用户", notes = "举报用户的接口")
    @PostMapping(value = "/reportUser")
    public JsonResult reportUser(@RequestBody UserReport userReport) {
        // 保存举报信息
        userService.reportUser(userReport);
        return JsonResult.ok("举报成功…有你平台更美好");
    }

}
