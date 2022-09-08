package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.common.R;
import com.drotsakura.dao.DishDao;
import com.drotsakura.dto.DishDto;
import com.drotsakura.pojo.Dish;
import com.drotsakura.pojo.DishFlavor;
import com.drotsakura.service.DishFlavorService;
import com.drotsakura.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithDishFavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((items)->{
            items.setDishId(dishId);
            return items;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getWithDishFavor(Long id) {
        Dish dish =  super.getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavorList);

        return dishDto;
    }

    @Override
    public void updateWithDishFavor(DishDto dishDto) {
        super.updateById(dishDto);
        Long dishId = dishDto.getId();

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((items)->{
            items.setDishId(dishId);
            return items;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavorList);

    }

    @Override
    public R<List<DishDto>> dishWithDishFlavor(String categoryId, Integer status) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId)
                .eq(status != null,Dish::getStatus,status);

        /*List<Dish> dishList = dishService.list(queryWrapper);*/
        List<DishDto> dishDtoList = this.list(queryWrapper).stream().map(item -> {
            Long dishId = item.getId();
            List<DishFlavor> dishFlavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishId));
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
