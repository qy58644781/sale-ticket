package com.yadan.saleticket.controller.admin;


import com.yadan.saleticket.base.tools.Json;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddUserVo;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.user.User;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public Page<User> users(STPageRequest pageRequest, String filter) {
        Map<String, String> parse = Json.String2Object(filter, HashMap.class);
        Page<User> users;
        if (MapUtils.isNotEmpty(parse)) {
            users = userRepository.findAllByFilterAndPageRequest(pageRequest, parse, User.class);
        } else {
            users = userRepository.findAll(pageRequest.genPageRequest());
        }
        return users;
    }

    @GetMapping("/{id}")
    public User user(@PathVariable("id") Long id) {
        return userRepository.findOne(id);
    }

    @PostMapping("/merge")
    public User merge(@RequestBody AddUserVo user) {
        System.out.println(user);
        InputStream is = user.getPortal().getStream();
        try {
            Workbook wb = new XSSFWorkbook(is);
            String sheetName = wb.getSheetAt(0).getSheetName();
            System.out.println(sheetName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
//        return userRepository.merge(user);
    }

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
        Set<Long> result = new HashSet<>();
        String[] idArr = ids.split(",");
        if (ArrayUtils.isNotEmpty(idArr)) {
            for (String id : idArr) {
                if (StringUtils.isNotEmpty(id)) {
                    userRepository.delete(Long.valueOf(id));
                    result.add(Long.valueOf(id));
                }
            }
        }
        return result;
    }

    @GetMapping("/sexEnums")
    public List<Map> hallEnums() {
        List<Map> result = new ArrayList<>();
        for (SexEnum each : SexEnum.values()) {
            Map map = new HashMap();
            map.put("id", each);
            map.put("name", each.getVal());
            result.add(map);
        }
        return result;
    }

}
