package com.yadan.saleticket.controller.wxma;

import com.yadan.saleticket.base.security.annotation.CurrentUser;
import com.yadan.saleticket.entity.MailAddressReqVO;
import com.yadan.saleticket.entity.MailAddressVO;
import com.yadan.saleticket.model.MailAddress;
import com.yadan.saleticket.model.User;
import com.yadan.saleticket.service.MailAddressService;
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

@Api(description = "wx收件地址")
@RestController
@RequestMapping("/wx/mailAddress")
public class WxMailAddressController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailAddressService mailAddressService;

    @ApiOperation("添加收货地址")
    @PostMapping("/add")
    public MailAddressVO add(@ApiIgnore @CurrentUser User user,
                             @Valid @RequestBody MailAddressReqVO mailAddressVO) {
        MailAddress persist = mailAddressService.add(memberService.fromUser(user), mailAddressVO);
        return mailAddressService.from(persist);
    }

    @ApiOperation("更新收货地址")
    @PostMapping("/update")
    public MailAddressVO update(@ApiIgnore @CurrentUser User user,
                                @Valid @RequestBody MailAddressReqVO mailAddressVO) {
        MailAddress persist = mailAddressService.update(memberService.fromUser(user), mailAddressVO);
        return mailAddressService.from(persist);
    }

    @ApiOperation("删除收货地址")
    @PostMapping("/delete/{id}")
    public Long delete(@ApiIgnore @CurrentUser User user, @PathVariable Long id) {
        return mailAddressService.delete(memberService.fromUser(user), id);
    }

    @ApiOperation("添加收货地址")
    @GetMapping("/list")
    public List<MailAddressVO> list(@ApiIgnore @CurrentUser User user, Integer page, Integer size) {
        List<MailAddress> persists = mailAddressService.list(memberService.fromUser(user), page, size);
        if (CollectionUtils.isEmpty(persists)) {
            return null;
        }
        return persists.stream().map(mailAddressService::from).collect(Collectors.toList());
    }
}
