package com.drotsakura.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drotsakura.common.R;
import com.drotsakura.dto.DishDto;
import com.drotsakura.pojo.Category;
import com.drotsakura.pojo.Dish;
import com.drotsakura.service.CategoryService;
import com.drotsakura.service.DishFlavorService;
import com.drotsakura.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithDishFavor(dishDto);
        return R.success("菜品分类添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name)
                .orderByAsc(Dish::getSort);

        dishService.page(dishPage,queryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> dishInfo = dishPage.getRecords();
        List<DishDto> dishDtoList = dishInfo.stream().map((items) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(items, dishDto);

            Long categoryId = items.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getWithDishFavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithDishFavor(dishDto);

        return R.success("菜品修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(String categoryId,Integer status){
        return dishService.dishWithDishFlavor(categoryId,status);
    }
}
