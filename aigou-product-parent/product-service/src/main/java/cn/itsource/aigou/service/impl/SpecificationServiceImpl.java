package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.mapper.SpecificationMapper;
import cn.itsource.aigou.service.ISpecificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements ISpecificationService {

    @Override
    public List<Specification> getByProductTypeId(Long productTypeId) {
        return baseMapper.selectList(new QueryWrapper<Specification>().eq("productTypeId", productTypeId));
    }

    @Override
    public void saveProperties(List<Specification> properties) {
        //先删除原来有的
        baseMapper.delete(new QueryWrapper<Specification>().eq("productTypeId", properties.get(0).getProductTypeId()));
        //再保存
        super.saveBatch(properties);
    }
}
