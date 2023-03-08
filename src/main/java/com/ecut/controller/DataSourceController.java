package com.ecut.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecut.model.RestApi;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: 周伟
 * @CreateTime: 2023-02-08  09:04
 * @Version: 1.0
 */
@RestController
@RequestMapping("/data")
public class DataSourceController {


    @PostMapping("/file/import-xml")
    public Object importXmlFile(MultipartFile multipartFile, @RequestParam(value = "xpath") String xpath) throws DocumentException, IOException {
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        fileName += RandomUtil.randomString(5);
        File file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        SAXReader sax = new SAXReader();
        Document document = sax.read(file);
        if (StrUtil.isNotEmpty(xpath)) {
            List<Node> nodes = document.selectNodes(xpath);
            HashMap map = new HashMap(16);
            JSONArray jsonArray = new JSONArray();
            List<Object> listAll = new ArrayList<>();
            for (Node node : nodes) {
                JSONObject jsonObject = new JSONObject();
                //分组
                if (map.containsKey(node.getName()) || node.equals(nodes.get(nodes.size() - 1))) {
                    listAll.add(jsonArray);
                    jsonArray = new JSONArray();
                    map.clear();
                }
                map.put(node.getName(), node.getText());
                jsonObject.put(node.getName(), node.getText());
                jsonArray.add(jsonObject);
            }
            return JSONUtil.toJsonStr(listAll);
        }
        return getNodes(file);
    }

    @PostMapping("/file/import-csv")
    public Object importCsvFile(MultipartFile multipartFile) {
        List<Object> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        try (CSVReader csvReader = new CSVReaderBuilder(
                new BufferedReader(
                        new InputStreamReader(multipartFile.getInputStream(), "utf-8"))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            String[] strings;
            StringBuilder stringBuilder = new StringBuilder();
            String[] s = new String[100];
            while (iterator.hasNext()) {
                if (i == 0) {
                    strings = iterator.next();
                    for (int size = 0; size < strings.length; size++) {
                        stringBuilder.append(strings[size]).append(StrUtil.COMMA);
                        if (size == strings.length - 1) {
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        }
                    }
                    String string = stringBuilder.toString();
                    s = string.split(",");
                }
                //去除第一行的表头，从第二行开始
                if (i >= 1) {
                    String[] strings1 = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    for (int j = 0; j < s.length; j++) {
                        jsonObject.put(s[j], strings1[j]);
                        jsonArray.add(jsonObject);
                        if (j == s.length - 1) {
                            list.add(jsonArray);
                            jsonArray = new JSONArray();
                            strings1 = new String[s.length];
                        }
                    }
                }
                i++;
            }
            return JSONUtil.toJsonStr(list);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CSV文件读取异常");
            return list;
        }
    }

    /**
     * 从指定节点开始,递归遍历所有子节点
     *
     * @author chenleixing
     */
    public Object getNodes(File file) {
        try {
            List list = new ArrayList<>();
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //读取文件 转换成Document
            Document document = reader.read(file);
            //获取根节点元素对象
            Element root = document.getRootElement();
            //获取根节点的名称
            System.out.println("Root:" + root.getName());
            //遍历子节点
            JSONArray jsonArray = new JSONArray();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element element = it.next();
                JSONObject jsonObject = new JSONObject();
                for (Iterator<Element> it1 = element.elementIterator(); it1.hasNext(); ) {
                    Element element1 = it1.next();
                    for (Iterator<Element> it2 = element1.elementIterator(); it2.hasNext(); ) {
                        Element element2 = it2.next();
                        jsonObject.put(element2.getName(), element2.getText());
                    }
                    jsonObject.put(element1.getName(), element1.getText());
                }
                jsonArray.add(jsonObject);
            }
            list.add(jsonArray);
            return JSONUtil.toJsonStr(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/rest-api")
    public Object importRestApi(RestApi restApi){
        String body = HttpRequest.get(restApi.getRequestDomainName())
                .bearerAuth(restApi.getTokenName())
                .execute().body();
        return body;
    }
}
