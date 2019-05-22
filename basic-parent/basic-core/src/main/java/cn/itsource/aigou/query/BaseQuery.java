package cn.itsource.aigou.query;

/**
 * @author zt
 * @version V1.0
 * @className BaseQuery
 * @description Query的顶级父类
 * @date 2019/5/16 19:09
 */
public class BaseQuery {

    private Long page=1L;
    private Long size=10L;
    private String keyword;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
