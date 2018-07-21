package kk.techbytecare.hvacaward.Model;

public class UserDetails {
    private String userPhone;
    private boolean isWindow;
    private boolean isSplit;
    private boolean isDuet;
    private boolean isChiller;
    private boolean isVRV;
    private boolean isInstall;
    private boolean isService;
    private String count;
    private String AccountHolder;
    private String AccountNo;
    private String IFSC;
    private String BankName;
    private String BankBranch;

    public UserDetails() {
    }

    public UserDetails(String userPhone, boolean isWindow, boolean isSplit, boolean isDuet, boolean isChiller, boolean isVRV, boolean isInstall, boolean isService, String count, String accountHolder, String accountNo, String IFSC, String bankName, String bankBranch) {
        this.userPhone = userPhone;
        this.isWindow = isWindow;
        this.isSplit = isSplit;
        this.isDuet = isDuet;
        this.isChiller = isChiller;
        this.isVRV = isVRV;
        this.isInstall = isInstall;
        this.isService = isService;
        this.count = count;
        AccountHolder = accountHolder;
        AccountNo = accountNo;
        this.IFSC = IFSC;
        BankName = bankName;
        BankBranch = bankBranch;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isWindow() {
        return isWindow;
    }

    public void setWindow(boolean window) {
        isWindow = window;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean split) {
        isSplit = split;
    }

    public boolean isDuet() {
        return isDuet;
    }

    public void setDuet(boolean duet) {
        isDuet = duet;
    }

    public boolean isChiller() {
        return isChiller;
    }

    public void setChiller(boolean chiller) {
        isChiller = chiller;
    }

    public boolean isVRV() {
        return isVRV;
    }

    public void setVRV(boolean VRV) {
        isVRV = VRV;
    }

    public boolean isInstall() {
        return isInstall;
    }

    public void setInstall(boolean install) {
        isInstall = install;
    }

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAccountHolder() {
        return AccountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        AccountHolder = accountHolder;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankBranch() {
        return BankBranch;
    }

    public void setBankBranch(String bankBranch) {
        BankBranch = bankBranch;
    }
}
