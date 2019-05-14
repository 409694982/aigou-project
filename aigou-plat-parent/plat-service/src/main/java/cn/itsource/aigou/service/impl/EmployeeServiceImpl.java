package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Employee;
import cn.itsource.aigou.mapper.EmployeeMapper;
import cn.itsource.aigou.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author zt
 * @version V1.0
 * @className EmployeeServiceImpl
 * @description 员工service实现类
 * @date 2019/5/14 10:02
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return employeeMapper.selectOne(new QueryWrapper<Employee>().eq("username", username).eq("password", password));
    }
}
