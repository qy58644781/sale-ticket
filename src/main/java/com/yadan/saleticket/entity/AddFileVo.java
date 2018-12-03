package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class AddFileVo {
    private String src;
    private String title;
    private Integer size;
    /**
     * base 64位内容
     */
    private String base64Content;

    public InputStream getStream() {
        String content = (this.base64Content.split(";")[1]).split(",")[1];
        ByteArrayInputStream in = null;
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(content);
            in = new ByteArrayInputStream(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
