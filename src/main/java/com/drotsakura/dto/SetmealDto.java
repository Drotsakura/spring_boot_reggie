package com.drotsakura.dto;

import com.drotsakura.pojo.Setmeal;
import com.drotsakura.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
