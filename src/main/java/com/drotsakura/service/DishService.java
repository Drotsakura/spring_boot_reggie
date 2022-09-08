package com.drotsakura.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drotsakura.common.R;
import com.drotsakura.dto.DishDto;
import com.drotsakura.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithDishFavor(DishDto dishDto);

    DishDto getWithDishFavor(Long id);

    void updateWithDishFavor(DishDto dishDto);

    R<List<DishDto>> dishWithDishFlavor(String categoryId, Integer status);
}
