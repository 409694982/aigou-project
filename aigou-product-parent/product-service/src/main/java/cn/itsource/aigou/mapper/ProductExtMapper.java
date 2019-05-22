package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.ProductExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 商品扩展 Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
public interface ProductExtMapper extends BaseMapper<ProductExt> {

    void deleteByProductIds(@Param("idList") Collection<? extends Serializable> idList);
}
