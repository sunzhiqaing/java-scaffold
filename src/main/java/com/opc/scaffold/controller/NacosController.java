package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Nacos 演示：配置动态刷新(@RefreshScope) + 服务发现(DiscoveryClient)
 */
@Tag(name = "Nacos 演示")
@RestController
@RequestMapping("/api/nacos")
@RefreshScope
public class NacosController {

    /** 来自 Nacos 配置中心 scaffold.config（@RefreshScope 支持动态刷新） */
    @Value("${scaffold.demo.message:default-nacos}")
    private String message;

    @Value("${scaffold.demo.app:unknown}")
    private String app;

    private final DiscoveryClient discoveryClient;
    private final ConfigurableEnvironment environment;

    public NacosController(DiscoveryClient discoveryClient, ConfigurableEnvironment environment) {
        this.discoveryClient = discoveryClient;
        this.environment = environment;
    }

    /** 读取 Nacos 动态配置值 */
    @Operation(summary = "读取 Nacos 动态配置值")
    @GetMapping("/config")
    public Result<Map<String, String>> config() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("scaffold.demo.message(@Value)", message);
        m.put("scaffold.demo.app(@Value)", app);
        m.put("scaffold.demo.message(Environment)", environment.getProperty("scaffold.demo.message", "MISSING"));
        m.put("scaffold.demo.app(Environment)", environment.getProperty("scaffold.demo.app", "MISSING"));
        // 遍历 property source，定位 scaffold.* 实际所在
        for (PropertySource<?> ps : environment.getPropertySources()) {
            if (!(ps instanceof EnumerablePropertySource<?> eps)) continue;
            for (String n : eps.getPropertyNames()) {
                if (n.startsWith("scaffold.")) {
                    m.put("PS[" + ps.getName() + "]." + n, String.valueOf(eps.getProperty(n)));
                }
            }
        }
        return Result.success(m);
    }

    /** 服务发现：Nacos 中注册的服务列表 */
    @Operation(summary = "查看 Nacos 已注册服务列表")
    @GetMapping("/services")
    public Result<List<String>> services() {
        return Result.success(discoveryClient.getServices());
    }

    /** 查看本服务注册信息（服务名） */
    @Operation(summary = "查看本服务注册名")
    @GetMapping("/self")
    public Result<String> self() {
        return Result.success(discoveryClient.getServices().isEmpty()
                ? "无" : String.join(",", discoveryClient.getServices()));
    }
}
