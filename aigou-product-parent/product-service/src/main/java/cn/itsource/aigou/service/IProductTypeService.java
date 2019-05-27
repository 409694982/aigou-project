package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.ProductType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务类
 * </p>
 *
 * @author zt
 * @since 2019-05-16
 */
public interface IProductTypeService extends IService<ProductType> {

    /**
     * 查询树形商品目录
     * @return
     */
    List<ProductType> tree();

    /**
     * 生成静态化页面
     */
    void generateStaticPage();

    /**
     * 下拉级联选择类型的时候回填使用
     * @param id
     * @return
     */
    Long[] getIdsById(Long id);

}
