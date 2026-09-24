package com.opc.scaffold.controller;

import com.opc.scaffold.common.exception.BizException;
import com.opc.scaffold.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查 & 脚手架功能演示
 */
@Tag(name = "系统接口")
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "java-scaffold");
        data.put("time", System.currentTimeMillis());
        return Result.success(data);
    }

    @Operation(summary = "演示统一返回体")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, OPC!");
    }

    @Operation(summary = "演示业务异常")
    @GetMapping("/error/{code}")
    public Result<Void> triggerError(@PathVariable Integer code) {
        throw new BizException(code, "业务异常演示，错误码 " + code);
    }
}
