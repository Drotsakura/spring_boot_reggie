package com.drotsakura.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drotsakura.common.R;
import com.drotsakura.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
