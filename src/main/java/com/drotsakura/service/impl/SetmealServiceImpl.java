package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.dao.SetmealDao;
import com.drotsakura.pojo.Setmeal;
import com.drotsakura.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
}
