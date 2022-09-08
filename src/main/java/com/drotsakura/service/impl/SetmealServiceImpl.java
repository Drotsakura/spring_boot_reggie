package com.drotsakura.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drotsakura.common.CustomException;
import com.drotsakura.common.R;
import com.drotsakura.dao.SetmealDao;
import com.drotsakura.dto.SetmealDto;
import com.drotsakura.pojo.Setmeal;
import com.drotsakura.pojo.SetmealDish;
import com.drotsakura.service.SetmealDishService;
import com.drotsakura.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void saveWithSetmealDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList = setmealDishList.stream().map((items)->{
            items.setSetmealId(setmealDto.getId());
            return items;
        }).collect(Collectors.toList());
        
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public void deleteWithSetmealDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);

        long count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("商品正在销售，删除失败");
        }

        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(dishLambdaQueryWrapper);
    }

    @Override
    public R<List<SetmealDto>> setmealWithDish(String categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId)
                .eq(status != null,Setmeal::getStatus,status);

        List<SetmealDto> setmealDtoList = this.list(queryWrapper).stream().map(item -> {
            Long setmealId = item.getId();
            List<SetmealDish> setmealDishList = setmealDishService.query().eq("setmeal_id", setmealId).list();

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setSetmealDishes(setmealDishList);
            return setmealDto;
        }).collect(Collectors.toList());

        return R.success(setmealDtoList);
    }
}
