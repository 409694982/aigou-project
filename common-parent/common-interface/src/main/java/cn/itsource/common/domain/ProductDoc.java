package cn.itsource.common.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author zt
 * @version V1.0
 * @className ProductDoc
 * @description 保存在es中的商品类
 * 用来查询的：
 * 关键字：all
 * 商品类型ID
 * 商品品牌ID
 * 最低价格
 * 最高价格
 * 销量
 * 上架时间
 * 评论量
 * 访问量
 *
 * 用来展示的：
 * 标题
 * 副标题
 * 显示属性
 * sku属性
 * 媒体属性
 * @date 2019/5/23 18:41
 */
@Document(indexName = "aigou",type = "productDoc")
public class ProductDoc {

    @Id
    @Field(type = FieldType.Long,store = true)
    private Long id;
    @Field(type = FieldType.Text,store = true,analyzer = "ik_max_word")
    private String all; //关键字查询，包含商品标题，副标题，品牌名，类型名，中间用空格分割，好分词
    @Field(type = FieldType.Long,store = true)
    private Long productTypeId;
    @Field(type = FieldType.Long,store = true)
    private Long brandId;
    @Field(type = FieldType.Integer,store = true)
    private Integer minPrice;
    @Field(type = FieldType.Integer,store = true)
    private Integer maxPrice;
    @Field(type = FieldType.Integer,store = true)
    private Integer saleCount;  //销量
    @Field(type = FieldType.Long,store = true)
    private Long onSaleTime;
    @Field(type = FieldType.Integer,store = true)
    private Integer commontCount;   //评论量
    @Field(type = FieldType.Integer,store = true)
    private Integer viewCount;  //访问量
    @Field(type = FieldType.Keyword,store = true)
    private String name;
    @Field(type = FieldType.Keyword,store = true)
    private String subName;
    @Field(type = FieldType.Keyword,store = true)
    private String viewProperties;
    @Field(type = FieldType.Keyword,store = true)
    private String skuProperties;
    @Field(type = FieldType.Keyword,store = true)
    private String medias;  //媒体属性

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Long getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(Long onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public Integer getCommontCount() {
        return commontCount;
    }

    public void setCommontCount(Integer commontCount) {
        this.commontCount = commontCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getViewProperties() {
        return viewProperties;
    }

    public void setViewProperties(String viewProperties) {
        this.viewProperties = viewProperties;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    public String getMedias() {
        return medias;
    }

    public void setMedias(String medias) {
        this.medias = medias;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
