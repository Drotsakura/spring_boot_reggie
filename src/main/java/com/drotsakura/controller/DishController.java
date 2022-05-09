package com.drotsakura.controller;

import com.drotsakura.common.R;
import com.drotsakura.dto.DishDto;
import com.drotsakura.service.DishFlavorService;
import com.drotsakura.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    public R<String> save(@RequestBody DishDto dishDto){

        return null;
    }
}
