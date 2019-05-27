package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.mapper.BrandMapper;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.aigou.service.IBrandService;
import cn.itsource.aigou.util.LetterUtil;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Override
    public boolean save(Brand brand) {
        //创建时间为当前时间
        brand.setCreateTime(new Date().getTime());
        String firstLetter = LetterUtil.getFirstLetter(brand.getName());
        brand.setFirstLetter(firstLetter);
        return super.save(brand);
    }

    @Override
    public boolean updateById(Brand brand) {
        //修改时间为当前时间
        brand.setUpdateTime(new Date().getTime());
        String firstLetter = LetterUtil.getFirstLetter(brand.getName());
        brand.setFirstLetter(firstLetter);
        return super.updateById(brand);
    }

    /**
     * 通过类型id查询所有品牌
     * @param productTypeId
     * @return
     */
    @Override
    public Map<String, Object> loadBrandsByProductTypeId(Long productTypeId) {
        List<Brand> brands = baseMapper.selectList(new QueryWrapper<Brand>().eq("product_type_id", productTypeId));
        //使用Set来去重，TreeSet对字符串有自然排序
        TreeSet<String> letters = new TreeSet<>();
        for (Brand brand : brands) {
            letters.add(brand.getFirstLetter());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("brands", brands);
        map.put("letters", letters);
        return map;
    }
}
