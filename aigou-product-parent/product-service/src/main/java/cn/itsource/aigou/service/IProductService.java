package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.Sku;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
public interface IProductService extends IService<Product> {

    PageList<Product> getByQuery(ProductQuery query);

    Product getExtById(Long id);

    List<Specification> getViewPropertiesById(Long id);

    void saveViewProperties(List<Specification> viewProperties, Long productId);

    Map<String, Object> getSkuPropertiesById(Long id);

    void saveSkuProperties(List<Specification> skuProperties, Long productId, List<Map<String, Object>> skus);
}
