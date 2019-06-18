package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.WxMaJscode2Session;

public interface WxMaJscode2SessionRepository extends ExtJpaRepository<WxMaJscode2Session, Long> {

    WxMaJscode2Session findWxMaJscode2SessionByOpenId(String openId);

    WxMaJscode2Session findWxMaJscode2SessionByOpenIdAndSessionKey(String openId, String sessionKey);
}
