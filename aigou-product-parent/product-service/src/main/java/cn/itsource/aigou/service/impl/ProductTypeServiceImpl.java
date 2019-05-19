package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.ProductType;
import cn.itsource.aigou.mapper.ProductTypeMapper;
import cn.itsource.aigou.service.IProductTypeService;
import cn.itsource.common.client.GenerateStaticPageClient;
import cn.itsource.common.client.RedisClient;
import cn.itsource.common.domain.ModelAndPath;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author zt
 * @since 2019-05-16
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    //redis的key
    private static final String KEY = "productTypes";
    private static final String LIST = "productTypeslist";

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private GenerateStaticPageClient generateStaticPageClient;

    @Override
    public void generateStaticPage() {
        try {
            //先加载商品类型树界面的配置文件，生成商品类别的静态页面
            Properties properties = Resources.getResourceAsProperties("product-type.properties");
            String templatePath = properties.getProperty("templatePath");
            String targetPath = properties.getProperty("targetPath");
            List<ProductType> list = tree();
            ModelAndPath modelAndPath = new ModelAndPath(list, templatePath, targetPath);
            generateStaticPageClient.generateStaticPage(modelAndPath);
            //再加载主页的配置文件，生成主页的静态页面
            properties = Resources.getResourceAsProperties("home.properties");
            templatePath = properties.getProperty("templatePath");
            targetPath = properties.getProperty("targetPath");
            String staticRoot = properties.getProperty("staticRoot");
            Map<String, Object> model = new HashMap<>();
            model.put("staticRoot", staticRoot);
            modelAndPath.setTargetPath(targetPath);
            modelAndPath.setTemplatePath(templatePath);
            modelAndPath.setModel(model);
            generateStaticPageClient.generateStaticPage(modelAndPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductType> tree() {
        //从redis中取出
        String jsonStr = redisClient.get(KEY);
        if (StringUtils.isEmpty(jsonStr)){
            //如果为空，就去数据库里面查
            List<ProductType> productTypes = loadTree();
            //转换成json放入redis中
            redisClient.set(KEY, JSONArray.toJSONString(productTypes));
            return productTypes;
        }else {
            //不为空就转换
            return JSONArray.parseArray(jsonStr, ProductType.class);
        }
    }

    private List<ProductType> loadTree() {
        List<ProductType> productTypes = baseMapper.selectList(null);
        Map<Long, ProductType> map = new HashMap<>();
        //把查询到的所有都保存在map中
        productTypes.forEach(productType -> {
            map.put(productType.getId(), productType);
        });
        //用来保存数据
        List<ProductType> list = new ArrayList<>();
        productTypes.forEach(productType -> {
            //如果是顶级类型就直接保存到创建的list中
            if (productType.getPid()==0){
                list.add(productType);
            }else {
                //如果是子类就保存到他父类的children集合中
                map.get(productType.getPid()).getChildren().add(productType);
            }
        });
        return list;
    }

    @Override
    public boolean save(ProductType entity) {
        boolean result = super.save(entity);
        sychornizedOperate();
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        sychornizedOperate();
        return result;
    }

    @Override
    public boolean updateById(ProductType entity) {
        boolean result = super.updateById(entity);
        sychornizedOperate();
        return result;
    }

    private void updateRedis(){
        //查询类型树
        List<ProductType> productTypes = tree();
        List<ProductType> list = getList();
        //转成json
        String jsonString = JSONArray.toJSONString(productTypes);
        String listStr = JSONArray.toJSONString(list);
        redisClient.set(KEY, jsonString);
        redisClient.set(LIST, listStr);
    }

    private void sychornizedOperate(){
        updateRedis();
        generateStaticPage();
    }


    @Override
    public Long[] getIdsById(Long id) {
        List<ProductType> productTypeList = getList();
        //key为当前productType对象的id，value为他的pid,这要就能快速通过id找到父id
        Map<Long, Long> idMap = new HashMap<>();
        productTypeList.forEach(productType -> {
            idMap.put(productType.getId(), productType.getPid());
        });
        //用来保存数据
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        Long pid = (Long) idMap.get(id);
        ids.add(pid);
        while (pid!=0){
            pid = (Long) idMap.get(pid);
            if (pid!=0){
                ids.add(pid);
            }
        }
        Long[] idArr = new Long[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            idArr[ids.size()-1-i] = ids.get(i);
        }
        return idArr;
    }

    private List<ProductType> getList(){
        String listStr = redisClient.get(LIST);
        List<ProductType> productTypeList;
        if (StringUtils.isEmpty(listStr)){
            productTypeList = baseMapper.selectList(null);
            redisClient.set(LIST, JSONArray.toJSONString(productTypeList));
        }else {
            productTypeList = JSONArray.parseArray(listStr, ProductType.class);
        }
        return productTypeList;
    }
}
