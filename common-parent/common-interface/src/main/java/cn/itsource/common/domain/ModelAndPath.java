package cn.itsource.common.domain;

/**
 * @author zt
 * @version V1.0
 * @className ModelAndPath
 * @description 用来封装生成静态页面时候的数据
 * @date 2019/5/17 19:46
 */
public class ModelAndPath {

    //保存的model数据
    private Object model;
    //模板路径
    private String templatePath;
    //保存路径
    private String targetPath;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public ModelAndPath() {}

    public ModelAndPath(Object model, String templatePath, String targetPath) {
        this.model = model;
        this.templatePath = templatePath;
        this.targetPath = targetPath;
    }
}
