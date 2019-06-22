package com.yadan.saleticket.controller;

import com.yadan.saleticket.entity.EnumRespVO;
import com.yadan.saleticket.enums.CredentialTypeEnum;
import com.yadan.saleticket.enums.SexEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "所有通用接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    @ApiOperation("证件类型")
    @GetMapping("/credentialTypeEnum")
    public List<EnumRespVO> credentialTypeEnum() {
        return Arrays.stream(CredentialTypeEnum.values())
                .map(each -> new EnumRespVO(each.toString(), each.getCn()))
                .collect(Collectors.toList());
    }

    @ApiOperation("性别")
    @GetMapping("/sexEnum")
    public List<EnumRespVO> sexEnum() {
        return Arrays.stream(SexEnum.values())
                .map(each -> new EnumRespVO(each.toString(), each.getCn()))
                .collect(Collectors.toList());
    }
}
