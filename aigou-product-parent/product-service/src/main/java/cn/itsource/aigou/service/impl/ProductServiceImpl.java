package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.*;
import cn.itsource.aigou.mapper.*;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.aigou.util.PageList;
import cn.itsource.aigou.util.StrUtils;
import cn.itsource.common.client.ElasticSearchClient;
import cn.itsource.common.client.FileClient;
import cn.itsource.common.client.GenerateStaticPageClient;
import cn.itsource.common.domain.ModelAndPath;
import cn.itsource.common.domain.ProductDoc;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
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
    @Autowired
    private ElasticSearchClient elasticSearchClient;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private GenerateStaticPageClient generateStaticPageClient;
    @Autowired
    private FileClient fileClient;

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
            if (product.getState()==1){
                //如果是上架状态需要同步更新到es中,也就是重新上架
                List<Long> list = new ArrayList<>();
                list.add(product.getId());
                onSale(list);
            }
            //不能确定以前是否有保存过详情，直接先删后增
            productExtMapper.delete(new QueryWrapper<ProductExt>().eq("productId", product.getId()));
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
            //删除前先把fastdfs中的删掉
            List<Product> products = baseMapper.selectBatchIds(idList);
            for (Product product : products) {
                List<String> mediasList = JSONArray.parseArray(product.getMedias(), String.class);
                for (String medias : mediasList) {
                    fileClient.delete(medias);
                }
            }
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

    /**
     * 批量上架
     * @param idList
     */
    @Override
    public void onSale(List<Long> idList) {
        //批量修改商品的上架时间和状态
        baseMapper.onSale(idList,new Date().getTime());
        //修改完后查询商品
        List<Product> products = baseMapper.selectBatchIds(idList);
        //把List<Product>转为List<ProductDos>
        List<ProductDoc> productDocs = productsToProductDocs(products);
        //保存到es中
        elasticSearchClient.saveBatch(productDocs);
        //静态化该商品页面
        for (Product product : products) {
            staticProductDetail(product);
        }
    }

    @Override
    public void offSale(List<Long> idList) {
        //批量修改商品的下架时间和状态
        baseMapper.offSale(idList,new Date().getTime());
        //从es中批量删除
        elasticSearchClient.deleteBatcheByIds(idList);
    }

    private List<ProductDoc> productsToProductDocs(List<Product> products) {
        List<ProductDoc> productDocs = new ArrayList<>();
        products.forEach(product -> {
            ProductDoc productDoc = productToProductDoc(product);
            productDocs.add(productDoc);
        });
        return productDocs;
    }

    private ProductDoc productToProductDoc(Product product) {
        ProductDoc productDoc = new ProductDoc();
        productDoc.setId(product.getId());
        productDoc.setProductTypeId(product.getProductTypeId());
        productDoc.setSaleCount(product.getSaleCount());
        productDoc.setOnSaleTime(product.getOnSaleTime());
        productDoc.setCommontCount(product.getCommentCount());
        productDoc.setViewCount(product.getViewCount());
        productDoc.setName(product.getName());
        productDoc.setSubName(product.getSubName());
        productDoc.setViewProperties(product.getViewProperties());
        productDoc.setSkuProperties(product.getSkuProperties());
        productDoc.setMedias(product.getMedias());
        productDoc.setBrandId(product.getBrandId());
        //将商品的标题，副标题，品牌名，类型名以空格间隔保存到all中
        Brand brand = brandMapper.selectById(product.getBrandId());
        ProductType productType = productTypeMapper.selectById(product.getProductTypeId());
        String all = new StringBuilder()
                .append(product.getName())
                .append(" ")
                .append(product.getSubName())
                .append(" ")
                .append(brand.getName())
                .append(" ")
                .append(productType.getName()).toString();
        productDoc.setAll(all);
        //查询所有的sku，比较每个sku的价格，得出最大值和最小值
        List<Sku> skus = skuMapper.selectList(new QueryWrapper<Sku>().eq("productId", product.getId()));
        Integer minPrice = skus.get(0).getPrice();
        Integer maxPrice = skus.get(0).getPrice();
        for (Sku sku : skus) {
            minPrice = minPrice<sku.getPrice()?minPrice:sku.getPrice();
            maxPrice = maxPrice>sku.getPrice()?maxPrice:sku.getPrice();
        }
        productDoc.setMinPrice(minPrice);
        productDoc.setMaxPrice(maxPrice);
        return productDoc;
    }

    private void staticProductDetail(Product product){
        try {
            Properties properties = Resources.getResourceAsProperties("product-detail.properties");
            //模板路径
            String templatePath = properties.getProperty("templatePath");
            //生成的文件的路径
            String targetPath = properties.getProperty("targetPath")+product.getId()+".html";
            //数据
            Map<String,Object> model = new HashMap<>();
            String staticRoot = properties.getProperty("staticRoot");
            model.put("staticRoot", staticRoot);
            model.put("product",product);
            List<Map<String, Object>> crumbs = this.getCrumb(product.getProductTypeId());
            model.put("crumbs",crumbs);
            ProductExt productExt = productExtMapper.selectOne(new QueryWrapper<ProductExt>().eq("productId", product.getId()));
            model.put("productExt",productExt);
            //显示属性
            List<Specification> viewProperties = JSONArray.parseArray(product.getViewProperties(), Specification.class);
            model.put("viewProperties", viewProperties);
            //SKU属性
            List<Specification> skuProperties = JSONArray.parseArray(product.getSkuProperties(), Specification.class);
            model.put("skuProperties", skuProperties);
            model.put("skuCount", skuProperties.size());
            //查询对应的sku
            List<Sku> skus = skuMapper.selectList(new QueryWrapper<Sku>().eq("productId", product.getId()));
            model.put("skus", JSONArray.toJSONString(skus));
            //调用公共的接口
            ModelAndPath param = new ModelAndPath();
            param.setTemplatePath(templatePath);
            param.setTargetPath(targetPath);
            param.setModel(model);
            generateStaticPageClient.generateStaticPage(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟进商品类型id查询同级和上级的所有
     * @param productTypeId
     * @return
     */
    @Override
    public List<Map<String, Object>> getCrumb(Long productTypeId) {
        //查询出商品
        ProductType productType = productTypeMapper.selectById(productTypeId);
        //获取到path,如.1.2.3.
        String path = productType.getPath();
        //去掉第一个点，并分割，最后一个不用
        List<Long> ids = StrUtils.splitStr2LongArr(path.substring(1), "\\.");
        //保存数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (Long id : ids) {
            Map<String, Object> map = new HashMap<>();
            ProductType crumbProductType = productTypeMapper.selectById(id);
            //查询除了当前以外的同级的
            List<ProductType> otherProductTypes = productTypeMapper.selectList(new QueryWrapper<ProductType>().eq("pid", crumbProductType.getPid()).ne("id", id));
            map.put("crumbProductType", crumbProductType);
            map.put("otherProductTypes", otherProductTypes);
            list.add(map);
        }
        return list;
    }
}
