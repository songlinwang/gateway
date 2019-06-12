package com.wsl.meta;

import lombok.Data;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Data
public class User {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐加密
     */
    private String salt;
    /**
     * 角色码
     */
    private String roleCode;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除状态（0正常 1删除）
     */
    private String delFlag;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

}
