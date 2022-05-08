package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.dao.DishDao;
import com.drotsakura.pojo.Dish;
import com.drotsakura.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {

}
