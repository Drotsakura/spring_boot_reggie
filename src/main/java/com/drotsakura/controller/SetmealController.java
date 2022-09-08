package com.drotsakura.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drotsakura.common.R;
import com.drotsakura.dto.SetmealDto;
import com.drotsakura.pojo.Category;
import com.drotsakura.pojo.Setmeal;
import com.drotsakura.pojo.SetmealDish;
import com.drotsakura.service.CategoryService;
import com.drotsakura.service.SetmealDishService;
import com.drotsakura.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        return R.success("套餐添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName,name)
                        .orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);
        Page<SetmealDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<SetmealDto> setmealDtoList = pageInfo.getRecords().stream().map((items) -> {
            Long categoryId = items.getCategoryId();
            Category category = categoryService.getById(categoryId);
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(items, setmealDto);
            setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(setmealDtoList);
        return R.success(dtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithSetmealDish(ids);
        return R.success("删除套餐成功");
    }

    @GetMapping("/list")
    public R<List<SetmealDto>> list(String categoryId,Integer status){
        return setmealService.setmealWithDish(categoryId,status);
    }
}
