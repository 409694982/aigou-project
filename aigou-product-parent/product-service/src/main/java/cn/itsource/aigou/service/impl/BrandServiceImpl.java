package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.mapper.BrandMapper;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.aigou.service.IBrandService;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author zt
 * @since 2019-05-16
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Override
    public PageList<Brand> getByQuery(BrandQuery query) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<Brand>();
        if (!StringUtils.isEmpty(query.getKeyword())){
            wrapper.like("b.name", query.getKeyword()).or().like("englishName", query.getKeyword());
        }
        IPage<Brand> page = baseMapper.selectByQuery(new Page<Brand>(query.getPage(), query.getSize()), wrapper);
        return new PageList<Brand>(page.getTotal(),page.getRecords());
    }
}
