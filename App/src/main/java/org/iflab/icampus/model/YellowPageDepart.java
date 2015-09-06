package org.iflab.icampus.model;

/**
 * 描述黄页目录各个部门的类
 *
 * @date 2015/9/6
 * @time 18:08
 */
public class YellowPageDepart {
    private String name;
    private String depart;

    public YellowPageDepart(String name, String depart) {
        this.name = name;
        this.depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }


}
