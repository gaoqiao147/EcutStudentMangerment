package com.ecut.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecut.mapper.LoginMapper;
import com.ecut.mapper.SubjectScoreMapper;
import com.ecut.model.LoginDO;
import com.ecut.model.SubjectStudentDO;
import com.ecut.service.SubjectStudentService;
import com.ecut.util.CommonResult;
import com.ecut.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.security.web.server.authentication.ServerX509AuthenticationConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhouwei
 * @since 2022-06-25
 */
@Slf4j
@RestController
@RequestMapping("/subject-student")
public class SubjectStudentController {
    @Resource
    SubjectScoreMapper subjectScoreMapper;
    @Resource
    SubjectStudentService subjectStudentService;
    @Resource
    LoginMapper loginMapper;

    /**
     * 处理图片上传
     *
     * @param id
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/file/upload/{id}")
    public JSONObject fileUpload(@PathVariable("id") Integer id, @RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) throws IOException {
        //上传路径保存设置
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        //保存的linux系统的目录
        String path = "E:\\study\\file\\";
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        //上传文件地址
        System.out.println("上传文件保存地址：" + realPath);
        //解决文件名字问题：我们使用uuid;
        String extName = FileUtil.extName(file.getOriginalFilename());
        String filename = "pg-" + UUID.randomUUID().toString().replaceAll("-", "") + StrUtil.DOT + extName;
        File newfile = new File(realPath, filename);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newfile);
//        String url = "http://1.117.87.146:8233/pictures/" + filename;
//        subjectScoreMapper.updatePic(id, url);
        //给editormd进行回调
        JSONObject res = new JSONObject();
//        res.put("url", url);
        res.put("success", 1);
        res.put("message", "upload success!");
        return res;
    }

    @GetMapping("file/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        String extName = FileUtil.extName(fileName);
        List<String> fileType = Arrays.asList("docx", "docx");
        if (fileType.contains(extName)) {
            response.setHeader("Content-Disposition", "attachment;filename" + fileName);
        }
        //获取文件
        String path = "E:\\study\\file\\";
        String fileFullPath = path + fileName;
        byte[] readBytes = FileUtil.readBytes(fileFullPath);
        ServletOutputStream os = response.getOutputStream();
        os.write(readBytes);
        os.flush();
        os.close();
    }

    @GetMapping("/file/export")
    public void exportFile(HttpServletResponse response) throws IOException {
        List<LoginDO> loginDOS = loginMapper.selectAll();
        //获取输出
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(loginDOS, true);
        //设置返回头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("User信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    @PostMapping("/file/import")
    public void importFile(MultipartFile file) throws IOException {
        //获取文件输入流
        InputStream inputStream = file.getInputStream();
        //读取文件输入流
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<LoginDO> loginDOS = reader.readAll(LoginDO.class);
        loginMapper.saveLogins(loginDOS);
    }



    @GetMapping("/redis-cache")
    public ResultVo redisCache() throws JsonProcessingException {
        List<SubjectStudentDO> list = subjectStudentService.allList();
        if (list.isEmpty()) {
            return CommonResult.fail();
        } else {
            return CommonResult.success(list);
        }
    }


}

