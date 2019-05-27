package cn.itsource.common.client;

import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.common.domain.ProductDoc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("COMMON")
public interface ElasticSearchClient {

    /**
     * 保存一个
     * @param productDoc
     * @return
     */
    @PostMapping("/es/save")
    AjaxResult save(@RequestBody ProductDoc productDoc);

    /**
     * 保存多个
     * @param productDocs
     * @return
     */
    @PostMapping("/es/saveBatch")
    AjaxResult saveBatch(@RequestBody List<ProductDoc> productDocs);

    /**
     * 删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/es/delete")
    AjaxResult delete(@RequestParam("id") Long id);

    /**
     * 删除多个
     * @param productDocs
     * @return
     */
    @DeleteMapping("/es/deleteBatch")
    AjaxResult deleteBatch(@RequestBody List<ProductDoc> productDocs);

    /**
     * 批量删除
     * @param idList
     */
    @DeleteMapping("/es/deleteBatchByIds")
    AjaxResult deleteBatcheByIds(List<Long> idList);
}
