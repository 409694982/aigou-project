package cn.itsource.common;

import cn.itsource.common.domain.ProductDoc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zt
 * @version V1.0
 * @className ESTest
 * @description ES的测试类
 * @date 2019/5/23 19:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApplication.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void test() throws Exception {
        elasticsearchTemplate.deleteIndex("aigou");
        elasticsearchTemplate.createIndex("aigou");
        elasticsearchTemplate.putMapping(ProductDoc.class);
    }
}
