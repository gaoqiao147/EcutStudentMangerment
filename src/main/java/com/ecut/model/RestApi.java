package com.ecut.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *@Author: 周伟
 *@CreateTime: 2023-02-08  09:07
 *@Version: 1.0
 */
@Data
@ApiModel(description = "rest-api请求实体类")
public class RestApi {

    @NotBlank(message = "数据源名称不能为空")
    private String dataSourceName;

    @ApiModelProperty(value = "请求协议")
    private String requestProtocol;

    @ApiModelProperty(value = "请求域名")
    private String requestDomainName;

    @ApiModelProperty(value = "请求认证方式")
    private String requestAuthentication;

    @ApiModelProperty(value = "token模式(url、cookie、header)")
    private String tokenMode;

    @ApiModelProperty(value = "token名称")
    private String tokenName;

    @ApiModelProperty(value = "token前缀")
    private String tokenPrefix;

    @ApiModelProperty(value = "token获取方式(固定值、接口)")
    private String tokenAcquisitionMode;

    @ApiModelProperty(value = "token请求方式")
    private String tokenRequestMode;

    @ApiModelProperty(value = "token传参方式")
    private String parameterPassingMode;

    @ApiModelProperty(value = "token传参格式")
    private String parameterPassingFormat;

    @ApiModelProperty(value = "json传递的参数")
    private String tokenJson;

    @ApiModelProperty(value = "地名地址信息")
    private String placeInfo;

    @ApiModelProperty(value = "token值获取路径")
    private String tokenGetPath;
}
