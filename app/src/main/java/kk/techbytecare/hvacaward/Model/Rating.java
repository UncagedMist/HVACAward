package kk.techbytecare.hvacaward.Model;

public class Rating {
    private String userPhone;
    private String projectId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String projectId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.projectId = projectId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
