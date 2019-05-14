package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author zt
 * @version V1.0
 * @className IEmployeeService
 * @description 员工service接口
 * @date 2019/5/14 10:01
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 传入包含username和password的map集合
     * @param params
     * @return
     */
    Employee login(Map<String, String> params);
}
