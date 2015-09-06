package org.iflab.icampus.model;

/**
 * 描述黄页各个部门下分支详情的类
 *
 * @date 2015/9/6
 * @time 22:26
 */
public class YellowPageDepartBranch {
    private String branchName;
    private String telephoneNumber;

    public YellowPageDepartBranch() {
    }

    public YellowPageDepartBranch(String branchName, String telephoneNumber) {
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
