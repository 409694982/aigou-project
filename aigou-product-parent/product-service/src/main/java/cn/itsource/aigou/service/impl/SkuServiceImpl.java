package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Sku;
import cn.itsource.aigou.mapper.SkuMapper;
import cn.itsource.aigou.service.ISkuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * SKU 服务实现类
 * </p>
 *
 * @author zt
 * @since 2019-05-21
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Override
    public Integer loadAvailableStock(Map<String, Object> param) {
        String skuIndex = (String) param.get("skuIndex");
        Long productId = (Long) param.get("productId");
        Sku sku = baseMapper.selectOne(new QueryWrapper<Sku>().eq("productId", productId).eq("skuIndex", skuIndex));
        return sku.getAvailableStock();
    }
}
