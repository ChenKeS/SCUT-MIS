package com.scut.mis;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Component
public class AuthUtil {

    public String getCurrentRole() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 直接从请求头获取角色
            String role = attributes.getRequest().getHeader("X-Role");
            if (role != null && !role.isEmpty()) {
                return role;
            }
        }
        // 默认返回 ADMIN（方便测试）
        return "ADMIN";
    }

    public boolean isStudent() {
        return "STUDENT".equals(getCurrentRole());
    }

    public boolean isTeacher() {
        return "TEACHER".equals(getCurrentRole());
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getCurrentRole());
    }

    public boolean canModifyScore() {
        return isTeacher();
    }

    public boolean canModifyInfo() {
        return isAdmin();
    }

    public boolean canQuery() {
        return true;
    }
}