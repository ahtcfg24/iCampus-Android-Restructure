package org.iflab.icampus.model;

/**
 * 描述黄页各个部门详情的类
 *
 * @date 2015/9/6
 * @time 22:26
 */
public class YellowPageDepartDetails {
    private String branchName;
    private String telephoneNumber;

    public YellowPageDepartDetails() {
    }

    public YellowPageDepartDetails(String branchName, String telephoneNumber) {
        this.branchName = branchName;
        this.telephoneNumber = telephoneNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
