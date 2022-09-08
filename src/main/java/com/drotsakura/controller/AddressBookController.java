package com.drotsakura.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.drotsakura.common.BaseContext;
import com.drotsakura.common.R;
import com.drotsakura.pojo.AddressBook;
import com.drotsakura.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        Long userId = BaseContext.getId();
        log.debug("userId-address{}",userId);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null,AddressBook::getUserId,userId);

        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
        return R.success(addressBookList);
    }

    @PutMapping("/default")
    public R<String> defaultAddress(@RequestBody AddressBook addressBook){
        //取消全部默认设置
        Long userId = BaseContext.getId();
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AddressBook::getIsDefault,0).eq(AddressBook::getUserId,userId);
        addressBookService.update(updateWrapper);

        //重新设置默认设置
        Long addressBookId = addressBook.getId();
        addressBookService.update().eq("id",addressBookId).set("is_default",1).update();
        log.info("{}",addressBook.getId());
        return R.success("默认地址设置完毕");
    }

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        addressBookService.save(addressBook);
        return R.success("地址新增成功");
    }
}
