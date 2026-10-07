package com.opc.scaffold.controller;

import com.opc.scaffold.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 五种数据结构常用操作演示（String/Hash/List/Set/ZSet）
 */
@Tag(name = "Redis 演示")
@RestController
@RequestMapping("/api/redis")
public class RedisController {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Operation(summary = "String：写入+读取，支持过期秒数")
    @PostMapping("/string")
    public Result<Object> string(@RequestParam String key, @RequestParam String value,
                                 @RequestParam(defaultValue = "0") long ttlSeconds) {
        if (ttlSeconds > 0) {
            redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
        return Result.success(redisTemplate.opsForValue().get(key));
    }

    @Operation(summary = "Hash：写入字段+读取全部字段")
    @PostMapping("/hash")
    public Result<Map<Object, Object>> hash(@RequestParam String key, @RequestParam String field,
                                            @RequestParam String value) {
        redisTemplate.opsForHash().put(key, field, value);
        return Result.success(redisTemplate.opsForHash().entries(key));
    }

    @Operation(summary = "List：右端写入+范围读取")
    @PostMapping("/list")
    public Result<List<Object>> list(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForList().rightPush(key, value);
        return Result.success(redisTemplate.opsForList().range(key, 0, -1));
    }

    @Operation(summary = "Set：添加元素+读取全部（自动去重）")
    @PostMapping("/set")
    public Result<Set<Object>> set(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForSet().add(key, value);
        return Result.success(redisTemplate.opsForSet().members(key));
    }

    @Operation(summary = "ZSet：按 score 写入+有序读取")
    @PostMapping("/zset")
    public Result<Set<Object>> zset(@RequestParam String key, @RequestParam double score,
                                    @RequestParam String value) {
        redisTemplate.opsForZSet().add(key, value, score);
        return Result.success(redisTemplate.opsForZSet().range(key, 0, -1));
    }

    @Operation(summary = "删除 key")
    @DeleteMapping("/{key}")
    public Result<Boolean> delete(@PathVariable String key) {
        return Result.success(redisTemplate.delete(key));
    }
}
