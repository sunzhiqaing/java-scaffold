package com.opc.scaffold.controller;

import com.opc.scaffold.common.exception.BizException;
import com.opc.scaffold.common.result.Result;
import com.opc.scaffold.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 认证接口：登录签发 JWT。
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.auth.username}")
    private String expectedUser;

    @Value("${app.auth.password}")
    private String expectedPass;

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "登录，返回 JWT token")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest req) {
        if (!expectedUser.equals(req.username()) || !expectedPass.equals(req.password())) {
            throw new BizException(401, "用户名或密码错误");
        }
        String token = jwtUtil.generate(req.username());
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("token", token);
        m.put("tokenType", "Bearer");
        m.put("username", req.username());
        m.put("expireHours", 24);
        return Result.success(m);
    }

    /** 登录请求体 */
    public record LoginRequest(String username, String password) {
    }
}
