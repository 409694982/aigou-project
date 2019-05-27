package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.query.ProductQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
public interface ProductMapper extends BaseMapper<Product> {

    IPage<Product> selectByQuery(Page<Product> page, @Param("query") ProductQuery query);

    Product selectExtById(@Param("id") Long id);

    void onSale(@Param("idList") List<Long> idList, @Param("onSaleTime") Long onSaleTime);

    void offSale(@Param("idList") List<Long> idList, @Param("offSaleTime") long time);
}
