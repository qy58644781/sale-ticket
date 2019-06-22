package com.yadan.saleticket.controller.wxma;

import com.yadan.saleticket.base.security.annotation.CurrentUser;
import com.yadan.saleticket.entity.BuyTicketUserInfoReqVO;
import com.yadan.saleticket.entity.BuyTicketUserInfoVO;
import com.yadan.saleticket.model.BuyTicketUserInfo;
import com.yadan.saleticket.model.User;
import com.yadan.saleticket.service.BuyTicketUserInfoService;
import com.yadan.saleticket.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "wx常用购票人")
@RestController
@RequestMapping("/wx/buyTicketUserInfo")
public class WxBuyTicketUserInfoController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BuyTicketUserInfoService buyTicketUserInfoService;

    @ApiOperation("添加常用购票人")
    @PostMapping("/add")
    public BuyTicketUserInfoVO add(@ApiIgnore @CurrentUser User user,
                                   @Valid @RequestBody BuyTicketUserInfoReqVO buyTicketUserInfoReqVO) {
        BuyTicketUserInfo info = buyTicketUserInfoService.add(memberService.fromUser(user), buyTicketUserInfoReqVO);
        return buyTicketUserInfoService.from(info);
    }

    @ApiOperation("更新常用购票人")
    @PostMapping("/update")
    public BuyTicketUserInfoVO update(@ApiIgnore @CurrentUser User user,
                                      @Valid @RequestBody BuyTicketUserInfoReqVO buyTicketUserInfoReqVO) {
        BuyTicketUserInfo info = buyTicketUserInfoService.update(memberService.fromUser(user), buyTicketUserInfoReqVO);
        return buyTicketUserInfoService.from(info);
    }

    @ApiOperation("删除常用购票人")
    @PostMapping("/delete/{id}")
    public Long delete(@ApiIgnore @CurrentUser User user, @PathVariable Long id) {
        return buyTicketUserInfoService.delete(memberService.fromUser(user), id);
    }

    @ApiOperation("添加常用购票人")
    @GetMapping("/list")
    public List<BuyTicketUserInfoVO> list(@ApiIgnore @CurrentUser User user, Integer page, Integer size) {
        List<BuyTicketUserInfo> infos = buyTicketUserInfoService.list(memberService.fromUser(user), page, size);
        if (CollectionUtils.isEmpty(infos)) {
            return null;
        }
        return infos.stream().map(buyTicketUserInfoService::from).collect(Collectors.toList());
    }
}
