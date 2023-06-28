package com.gientech.iot.user.api.pojo.vo;

import com.gientech.iot.core.pojo.vo.BaseTreeVO;
import com.gientech.iot.user.api.enums.PermissionTypeEnum;
import com.gientech.iot.user.api.pojo.UserConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 动态路由信息出参
 * @author: 白剑民
 * @dateTime: 2023-04-24 17:03:45
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Schema(description = "动态路由信息")
public class RouterTreeResultVO extends BaseTreeVO {

    private static final long serialVersionUID = -8106549102452574307L;
    @Schema(description = "名称")
    private String name;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "是否隐藏")
    private Boolean hidden;

    @Schema(description = "重定向")
    private Boolean redirect;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "类型")
    private PermissionTypeEnum type;

    @Schema(description = "路由元数据")
    private PermissionMetaResultVO meta;

    /**
     * @description: 获取路径
     * @author: 白剑民
     * @date: 2023-04-25 15:28:40
     * @return: String
     * @version: 1.0
     */
    public String getPath() {
        String routerPath;
        // 一级
        if (0 == getParentId().intValue()) {
            routerPath = "/" + path;
        }
        // 非一级
        else {
            routerPath = path;
        }
        return routerPath;
    }

    /**
     * @description: 获取组件
     * @author: 白剑民
     * @date: 2023-04-25 15:28:37
     * @return: String
     * @version: 1.0
     */
    public String getComponent() {
        String component;
        // 一级目录
        if (0 == getParentId().intValue() && PermissionTypeEnum.DIR.equals(getType())) {
            component = UserConstants.RouterComponent.LAY_OUT;
        }
        // 非一级目录
        else if (PermissionTypeEnum.DIR.equals(getType())) {
            component = UserConstants.RouterComponent.PARENT_VIEW;
        }
        // 一级菜单
        else if (0 == getParentId().intValue() && PermissionTypeEnum.MENU.equals(getType())) {
            component = meta.getComponent();
        }
        // 非一级菜单
        else {
            component = meta.getComponent();
        }
        return component;
    }
}
