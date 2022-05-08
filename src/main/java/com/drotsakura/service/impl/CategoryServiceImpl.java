package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.common.CustomException;
import com.drotsakura.dao.CategoryDao;
import com.drotsakura.pojo.Category;
import com.drotsakura.pojo.Dish;
import com.drotsakura.pojo.Setmeal;
import com.drotsakura.service.CategoryService;
import com.drotsakura.service.DishService;
import com.drotsakura.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {
        //判断当前分类是否包含菜品或者套餐
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0){
            throw new CustomException("当前分类下关联了菜品，删除失败");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount > 0){
            throw new CustomException("当前分类下关联了套餐,删除失败");
        }

        super.removeById(id);
    }
}
