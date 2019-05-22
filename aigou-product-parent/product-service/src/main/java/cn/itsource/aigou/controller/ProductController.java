package cn.itsource.aigou.controller;

import cn.itsource.aigou.domain.Sku;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    public IProductService productService;

    /**
    * 保存和修改公用的
    * @param product  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/product",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Product product){
        try {
            if(product.getId()!=null){
                productService.updateById(product);
            }else{
                productService.save(product);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param ids
    * @return
    */
    @RequestMapping(value="/product/{ids}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("ids") String ids){
        try {
            String[] idstr = ids.split(",");
            List<String> list = Arrays.asList(idstr);
            productService.removeByIds(list);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/product/{id}",method = RequestMethod.GET)
    public Product get(@PathVariable("id") Long id)
    {
        return productService.getById(id);
    }

    @GetMapping("/product/ext/{id}")
    public Product getExtById(@PathVariable("id") Long id){
        return productService.getExtById(id);
    }

    /**
    * 查看所有信息
    * @return
    */
    @RequestMapping(value = "/product/list",method = RequestMethod.GET)
    public List<Product> list(){
        return productService.list();
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/product/page",method = RequestMethod.POST)
    public PageList<Product> page(@RequestBody ProductQuery query)
    {
        return productService.getByQuery(query);
    }

    /**
     * 通过商品id获取显示属性的json字符串
     * @param id
     * @return
     */
    @GetMapping("/product/viewProperties/{id}")
    public List<Specification> getViewProperties(@PathVariable("id") Long id){
        return productService.getViewPropertiesById(id);
    }

    @PostMapping("/product/viewProperties")
    public AjaxResult saveViewProperties(@RequestBody Map<String, Object> map){
        try {
            List<Specification> viewProperties = (List<Specification>) map.get("viewProperties");
            Long productId = ((Integer) map.get("productId")).longValue();
            productService.saveViewProperties(viewProperties,productId);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("新增错误，原因："+e.getMessage());
        }
    }

    @GetMapping("/product/skuProperties/{id}")
    public Map<String, Object> getSkuProperties(@PathVariable("id") Long id){
        return productService.getSkuPropertiesById(id);
    }

    @PostMapping("/product/skuProperties")
    public AjaxResult saveSkuProperties(@RequestBody Map<String, Object> map){
        try {
            List<Specification> skuProperties = (List<Specification>) map.get("skuProperties");
            Long productId = ((Integer) map.get("productId")).longValue();
            List<Map<String, Object>> skus = (List<Map<String, Object>>) map.get("skus");
            productService.saveSkuProperties(skuProperties,productId,skus);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("新增错误，原因："+e.getMessage());
        }
    }
}
