package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Sku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * SKU 服务类
 * </p>
 *
 * @author zt
 * @since 2019-05-21
 */
public interface ISkuService extends IService<Sku> {

    Integer loadAvailableStock(Map<String, Object> param);
}
