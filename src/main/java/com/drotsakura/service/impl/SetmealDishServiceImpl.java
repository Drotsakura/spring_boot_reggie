package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.dao.SetmealDishDao;
import com.drotsakura.dto.SetmealDto;
import com.drotsakura.pojo.SetmealDish;
import com.drotsakura.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishDao, SetmealDish> implements SetmealDishService {

}
