package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Specification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
public interface ISpecificationService extends IService<Specification> {

    /**
     * 通过类型id获取属性
     * @param productTypeId
     * @return
     */
    List<Specification> getByProductTypeId(Long productTypeId);

    /**
     * 保存属性
     * @param properties
     */
    void saveProperties(List<Specification> properties);
}
