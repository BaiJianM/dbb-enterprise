package top.dabaibai.user.biz.service;

import top.dabaibai.user.api.pojo.dto.OnlineSearchDTO;
import top.dabaibai.user.api.pojo.vo.OnlineSearchResultVO;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 用户信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface OnlineService {

    /**
     * @param dto 在线信息入参
     * @description: 在线信息分页
     * @author: 白剑民
     * @date: 2023-05-31 13:15:45
     * @return: PageResultVO<OnlineSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<OnlineSearchResultVO> page(OnlineSearchDTO dto);

    /**
     * @param onlineIds 在线信息id
     * @description: 强制退出
     * @author: 白剑民
     * @date: 2023-05-31 13:15:46
     * @return: void
     * @version: 1.0
     */
    void delete(List<Long> onlineIds);
}
