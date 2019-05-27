package cn.itsource.common.repository;

import cn.itsource.common.domain.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zt
 * @version V1.0
 * @className ProductDocRepository
 * @description
 * @date 2019/5/23 19:25
 */
public interface ProductDocRepository extends ElasticsearchRepository<ProductDoc,Long> {
}
