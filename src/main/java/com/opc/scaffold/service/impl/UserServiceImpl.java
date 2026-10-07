package com.opc.scaffold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opc.scaffold.entity.User;
import com.opc.scaffold.mapper.UserMapper;
import com.opc.scaffold.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 Service 实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
