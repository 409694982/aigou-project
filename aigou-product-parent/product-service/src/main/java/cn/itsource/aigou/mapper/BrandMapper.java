package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.Brand;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 品牌信息 Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2019-05-16
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 自定义的高级查询和分页
     * @param page
     * @param wrapper
     * @return
     */
    IPage<Brand> selectByQuery(Page<Brand> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
