package com.gientech.iot.demo.api.interfaces;

import com.gientech.iot.demo.api.pojo.vo.TestValidateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/2/20 17:02
 */
@FeignClient(contextId = "IAuthMobileService", name = "gientech-demo", path = "/demo")
public interface IAuthMobileService {
    @PostMapping("/authMobile")
    String authMobile(@RequestBody TestValidateVO mobile);
}
