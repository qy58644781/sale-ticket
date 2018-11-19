package com.yadan.saleticket.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "global.sms")
    public class Sms {

        private Boolean turnOn;

        @Getter
        @Setter
        @Component
        @ConfigurationProperties(prefix = "global.sms.submail")
        public class Submail {
            private String path;
            private String appid;
            private String project;
            private String signature;
            private String signtype;
        }
    }
}
