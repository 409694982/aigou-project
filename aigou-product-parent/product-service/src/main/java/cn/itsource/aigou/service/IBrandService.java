package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author zt
 * @since 2019-05-16
 */
public interface IBrandService extends IService<Brand> {

    /**
     * 高级查询加分页
     * @param query
     * @return
     */
    PageList<Brand> getByQuery(BrandQuery query);
}
