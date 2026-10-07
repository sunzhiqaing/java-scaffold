package com.opc.scaffold.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opc.scaffold.common.result.Result;
import com.opc.scaffold.entity.User;
import com.opc.scaffold.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户 CRUD（MyBatis-Plus 常用操作演示）
 */
@RestController
@RequestMapping("/api/mysql/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 新增 */
    @PostMapping
    public Result<User> create(@RequestBody User user) {
        userService.save(user);
        return Result.success(user);
    }

    /** 按 ID 查询 */
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /** 列表（可按 name 模糊查询） */
    @GetMapping
    public Result<List<User>> list(@RequestParam(required = false) String name) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            qw.like(User::getName, name);
        }
        qw.orderByAsc(User::getId);
        return Result.success(userService.list(qw));
    }

    /** 更新 */
    @PutMapping
    public Result<User> update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success(user);
    }

    /** 删除（逻辑删除 deleted=1） */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }

    /** 分页查询 */
    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") long current,
                                   @RequestParam(defaultValue = "10") long size) {
        return Result.success(userService.page(new Page<>(current, size)));
    }
}
