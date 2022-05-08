package com.drotsakura.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drotsakura.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DishDao extends BaseMapper<Dish> {
}
