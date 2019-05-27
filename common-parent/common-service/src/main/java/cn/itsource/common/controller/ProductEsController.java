package cn.itsource.common.controller;

import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.aigou.util.PageList;
import cn.itsource.common.domain.ProductDoc;
import cn.itsource.common.query.ProductQuery;
import cn.itsource.common.repository.ProductDocRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zt
 * @version V1.0
 * @className ProductEsController
 * @description 商品模块es的接口
 * @date 2019/5/23 19:24
 */
@RestController
public class ProductEsController {

    @Autowired
    private ProductDocRepository productDocRepository;

    /**
     * 保存一个
     * @param productDoc
     * @return
     */
    @PostMapping("/es/save")
    public AjaxResult save(@RequestBody ProductDoc productDoc){
        try {
            productDocRepository.save(productDoc);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("新增失败，原因是："+e.getMessage());
        }
    };

    /**
     * 保存多个
     * @param productDocs
     * @return
     */
    @PostMapping("/es/saveBatch")
    public AjaxResult saveBatch(@RequestBody List<ProductDoc> productDocs){
        try {
            productDocRepository.saveAll(productDocs);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("新增失败，原因是："+e.getMessage());
        }
    };

    /**
     * 删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/es/delete")
    public AjaxResult delete(@RequestParam("id") Long id){
        try {
            productDocRepository.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败，原因是："+e.getMessage());
        }
    };

    /**
     * 删除多个
     * @param productDocs
     * @return
     */
    @DeleteMapping("/es/deleteBatch")
    public AjaxResult deleteBatch(@RequestBody List<ProductDoc> productDocs){
        try {
            productDocRepository.deleteAll(productDocs);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败，原因是："+e.getMessage());
        }
    };

    /**
     * 通过id集合删除多个
     * @param ids
     * @return
     */
    @DeleteMapping("/es/deleteBatchByIds")
    public AjaxResult deleteBatchByIds(@RequestBody List<Long> ids){
        try {
            for (Long id : ids) {
                //判断es中是否有这个id，如果没有直接删除会报错
                if (productDocRepository.existsById(id)){
                    productDocRepository.deleteById(id);
                }
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败，原因是："+e.getMessage());
        }
    }

    @PostMapping("/es/products")
    public PageList<ProductDoc> loadProducts(@RequestBody ProductQuery query){
        //查询的参数：all   productTypeId  brandId  minPrice  maxPrice
        //排序的字段: saleCount  onSaleTime  commontCount  viewCount       列名 sortField = xl,xp,pl,jg,rq   排序规则 sortRule = asc  desc
        //特殊字段    price排序   降序则使用maxPrice    升序minPrice
        //分页字段 page size
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //查询条件  --  all关键字   match匹配
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(query.getKeyword())){
            boolQueryBuilder.must(new MatchQueryBuilder("all",query.getKeyword()));
        }
        //过滤   -- termQuery  productTypeId   brandId
        List<QueryBuilder> filter = boolQueryBuilder.filter();
        if (query.getBrandId()!=null){
            filter.add(new TermQueryBuilder("brandId",query.getBrandId()));
        }
        if (query.getProductTypeId()!=null){
            filter.add(new TermQueryBuilder("productTypeId",query.getProductTypeId()));
        }
        //最小价格和最大价格过滤
        if (query.getMinPrice()!=null){
            filter.add(new RangeQueryBuilder("maxPrice").gte(query.getMinPrice()));
        }
        if (query.getMaxPrice()!=null){
            filter.add(new RangeQueryBuilder("minPrice").lte(query.getMaxPrice()));
        }
        //把match条件和filter添加进去
        builder.withQuery(boolQueryBuilder);
        //排序   --  saleCount  onSaleTime  commontCount  viewCount   price
        String order = "saleCount";//默认按照销量
        SortOrder sortOrder = SortOrder.DESC;//默认降序排序
        if ("asc".equals(query.getSortRule())){
            sortOrder = SortOrder.ASC;
        }
        String sortField = query.getSortField();
        if ("xp".equals(sortField)){
            order = "onSaleTime";
        }else if ("pl".equals(sortField)){
            order = "commontCount";
        }else if ("rq".equals(sortField)){
            order = "viewCount";
        }else if ("jg".equals(sortField)){
            if (sortOrder == SortOrder.DESC){
                order = "maxPrice";
            }else {
                order = "minPrice";
            }
        }
        //把排序添加进去
        builder.withSort(new FieldSortBuilder(order).order(sortOrder));
        //分页   --  page size  es中页数从0开始
        builder.withPageable(PageRequest.of(query.getPage()-1, query.getSize()));
        Page<ProductDoc> page = productDocRepository.search(builder.build());
        return new PageList<ProductDoc>(page.getTotalElements(),page.getContent());
    }
}
