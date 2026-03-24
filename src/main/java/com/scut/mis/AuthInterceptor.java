package com.scut.mis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthUtil authUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        String className = handlerMethod.getBeanType().getSimpleName();

        String httpMethod = request.getMethod();

        // 1. 检查成绩修改权限
        if (className.contains("CourseSelectionController") &&
                (methodName.equals("updateScore") ||
                        (httpMethod.equals("PUT") && request.getRequestURI().contains("/score")))) {

            if (!authUtil.canModifyScore()) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"权限不足：只有教师可以修改成绩\"}");
                return false;
            }
            return true;
        }

        // 2. 检查学生角色：只能查询
        if (authUtil.isStudent()) {
            if (!"GET".equalsIgnoreCase(httpMethod)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"权限不足：学生只能查询信息\"}");
                return false;
            }
            return true;
        }

        // 3. 检查教师角色
        if (authUtil.isTeacher()) {
            boolean isScoreUpdate = className.contains("CourseSelectionController") &&
                    (methodName.equals("updateScore") || (httpMethod.equals("PUT") && request.getRequestURI().contains("/score")));

            if (!"GET".equalsIgnoreCase(httpMethod) && !isScoreUpdate) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"权限不足：教师只能修改成绩\"}");
                return false;
            }
            return true;
        }

        // 4. 管理员权限检查：不能修改成绩
        if (authUtil.isAdmin()) {
            if (className.contains("CourseSelectionController") &&
                    (methodName.equals("updateScore") ||
                            (httpMethod.equals("PUT") && request.getRequestURI().contains("/score")))) {

                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"权限不足：管理员不能修改成绩\"}");
                return false;
            }
            return true;
        }

        return true;
    }
}