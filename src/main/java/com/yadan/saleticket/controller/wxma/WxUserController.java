package com.yadan.saleticket.controller.wxma;

import com.yadan.saleticket.base.security.annotation.CurrentUser;
import com.yadan.saleticket.entity.IdAuthenReqVO;
import com.yadan.saleticket.model.User;
import com.yadan.saleticket.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(description = "wx用户模块")
@RestController
@RequestMapping("/wx/user/")
public class WxUserController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("实名认证")
    @PostMapping("/identityAuthentication")
    public boolean identityAuthentication(@ApiIgnore @CurrentUser User user,
                                          @Valid @RequestBody IdAuthenReqVO idAuthenReqVO) {
        memberService.identityAuthentication(memberService.fromUser(user), idAuthenReqVO.getRealname(), idAuthenReqVO.getCredentialTypeEnum(), idAuthenReqVO.getCredentialNo());
        return true;
    }

}
