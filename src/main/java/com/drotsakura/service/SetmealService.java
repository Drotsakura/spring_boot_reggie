package com.drotsakura.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drotsakura.common.R;
import com.drotsakura.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<com.drotsakura.pojo.Setmeal> {
    void saveWithSetmealDish(SetmealDto setmealDto);

    void deleteWithSetmealDish(List<Long> ids);

    R<List<SetmealDto>> setmealWithDish(String categoryId, Integer status);
}
