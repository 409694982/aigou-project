package cn.itsource.aigou.controller;

import cn.itsource.aigou.service.ISpecificationService;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.query.SpecificationQuery;
import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpecificationController {
    @Autowired
    public ISpecificationService specificationService;

    /**
    * 保存和修改公用的
    * @param specification  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/specification",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Specification specification){
        try {
            if(specification.getId()!=null){
                specificationService.updateById(specification);
            }else{
                specificationService.save(specification);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/specification/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            specificationService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/specification/{id}",method = RequestMethod.GET)
    public Specification get(@PathVariable("id") Long id)
    {
        return specificationService.getById(id);
    }


    /**
    * 查看所有信息
    * @return
    */
    @RequestMapping(value = "/specification/list",method = RequestMethod.GET)
    public List<Specification> list(){
        return specificationService.list();
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/specification/page",method = RequestMethod.POST)
    public PageList<Specification> page(@RequestBody SpecificationQuery query)
    {
        IPage<Specification> specificationIPage = specificationService.page(new Page<Specification>(query.getPage(), query.getSize()));
        return new PageList<Specification>(specificationIPage.getTotal(),specificationIPage.getRecords());
    }

    //获取
    @RequestMapping(value = "/specification/productTypeId/{id}",method = RequestMethod.GET)
    public List<Specification> getByProductTypeId(@PathVariable("id") Long productTypeId) {
        return specificationService.getByProductTypeId(productTypeId);
    }

    /**
     * 保存属性
     * @param properties
     * @return
     */
    @PostMapping("/specification/save")
    public AjaxResult saveProperties(@RequestBody List<Specification> properties){
        try {
            for (Specification property : properties) {
                System.out.println(property);
            }
            specificationService.saveProperties(properties);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存失败，原因是："+e.getMessage());
        }
    }
}
