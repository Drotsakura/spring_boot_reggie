package com.drotsakura.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drotsakura.pojo.Employee;
import com.drotsakura.common.R;
import com.drotsakura.service.EmployeeService;
import com.mysql.cj.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static com.drotsakura.common.R.success;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行m5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询结返回失败
        if (emp == null){
            return R.error("登录失败");
        }

        //密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //查看员工状态，如果为已禁用状态，则返回员工已禁用
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //登录成功， 将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return success("退出成功");
    }

    //添加员工
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest httpServletRequest){
        //设置默认密码：123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置时间相关信息
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //其他信息
        //Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
        //employee.setCreateUser(id);
        //employee.setUpdateUser(id);

        //保持用户数据到数据库
        employeeService.save(employee);
        return success("员工创建成功");
    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(Long page,Long pageSize,String name){
        //创建分页对象
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //组装条件，执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        //HttpSession session = httpServletRequest.getSession();
        //Long id = (Long) session.getAttribute("employee");

        //设置更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        //设置更新用户
        //employee.setUpdateUser(id);
        //完成数据更新
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("员工不存在");
    }
}
