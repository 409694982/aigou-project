package cn.itsource.aigou.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zt
 * @version V1.0
 * @className PageList
 * @description 封装分页的工具类
 * @date 2019/5/11 21:34
 */
public class PageList<T> {

    private Long total;
    private List<T> rows = new ArrayList<T>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageList() {
    }

    public PageList(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
