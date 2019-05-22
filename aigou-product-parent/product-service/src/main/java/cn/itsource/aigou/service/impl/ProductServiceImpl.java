package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.ProductExt;
import cn.itsource.aigou.domain.Sku;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.mapper.ProductExtMapper;
import cn.itsource.aigou.mapper.ProductMapper;
import cn.itsource.aigou.mapper.SkuMapper;
import cn.itsource.aigou.mapper.SpecificationMapper;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.aigou.util.PageList;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author zt
 * @since 2019-05-20
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductExtMapper productExtMapper;
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public PageList<Product> getByQuery(ProductQuery query) {
        Page<Product> page = new Page<>(query.getPage(),query.getSize());
        IPage<Product> iPage = baseMapper.selectByQuery(page,query);
        return new PageList<Product>(iPage.getTotal(),iPage.getRecords());
    }

    @Override
    public Product getExtById(Long id) {
        return baseMapper.selectExtById(id);
    }

    @Override
    @Transactional
    public boolean save(Product product) {
        try {
            product.setCreateTime(new Date().getTime());
            super.save(product);
            ProductExt productExt = new ProductExt();
            productExt.setDescription(product.getDescription());
            productExt.setRichContent(product.getContent());
            productExt.setProductId(product.getId());
            productExtMapper.insert(productExt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateById(Product product) {
        try {
            product.setUpdateTime(new Date().getTime());
            super.updateById(product);
            ProductExt productExt = new ProductExt();
            productExt.setDescription(product.getDescription());
            productExt.setRichContent(product.getContent());
            productExt.setProductId(product.getId());
            productExtMapper.insert(productExt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        try {
            super.removeByIds(idList);
            productExtMapper.deleteByProductIds(idList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Specification> getViewPropertiesById(Long id) {
        Product product = baseMapper.selectById(id);
        String viewProperties = product.getViewProperties();
        //如果为空就表示是初次点击，还未保存过数据
        if (StringUtils.isEmpty(viewProperties)){
            List<Specification> specifications = specificationMapper.selectList(new QueryWrapper<Specification>().eq("productTypeId", product.getProductTypeId()).eq("isSku", 0));
            return specifications;
        }
        List<Specification> specifications = JSONArray.parseArray(viewProperties, Specification.class);
        return specifications;
    }

    @Override
    @Transactional
    public void saveViewProperties(List<Specification> viewProperties, Long productId) {
        Product product = baseMapper.selectById(productId);
        String jsonString = JSONArray.toJSONString(viewProperties);
        product.setViewProperties(jsonString);
        baseMapper.updateById(product);
    }

    @Override
    public Map<String, Object> getSkuPropertiesById(Long id) {
        Map<String, Object> map = new HashMap<>();
        Product product = baseMapper.selectById(id);
        String skuProperties = product.getSkuProperties();
        List<Specification> specifications;
        if (StringUtils.isEmpty(skuProperties)){
            specifications = specificationMapper.selectList(new QueryWrapper<Specification>().eq("productTypeId", product.getProductTypeId()).eq("isSku", 1));
        }else {
            specifications = JSONArray.parseArray(skuProperties, Specification.class);
        }
        map.put("skuProperties", specifications);
        List<Sku> skus = skuMapper.selectList(new QueryWrapper<Sku>().eq("productId", id));
        map.put("skus", skus);
        return map;
    }

    @Override
    public void saveSkuProperties(List<Specification> skuProperties, Long productId, List<Map<String, Object>> skus) {
        //修改商品的sku属性
        Product product = baseMapper.selectById(productId);
        String jsonString = JSONArray.toJSONString(skuProperties);
        product.setSkuProperties(jsonString);
        baseMapper.updateById(product);
        //删除sku表中的当前商品所对应的sku
        skuMapper.delete(new QueryWrapper<Sku>().eq("productId", productId));
        //再重新添加
        List<Sku> skuList = new ArrayList<>();
        for (Map<String, Object> skuMap : skus) {
            Sku sku = new Sku();
            sku.setProductId(productId);
            sku.setAvailableStock(Integer.valueOf(skuMap.get("availableStock").toString()));
            sku.setCreateTime(new Date().getTime());
            sku.setPrice(Integer.valueOf(skuMap.get("price").toString()));
            sku.setSkuIndex((String)skuMap.get("sku_index"));
            //获取除了sku_index price,availableStock之外的所有属性
            //name    sku_properties
            String name = "";
            Map<String, String> sku_properties = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : skuMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                //排除sku_index,price,availableStock
                if (key.equals("price") || key.equals("sku_index") || key.equals("availableStock")) {
                    continue;
                }
                name += value;
                sku_properties.put(key, value);
            }
            sku.setSkuName(name);
            sku.setSkuProperties(JSONObject.toJSONString(sku_properties));
            //保存
            skuMapper.insert(sku);
        }
    }
}
