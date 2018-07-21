package kk.techbytecare.hvacaward.Model;

public class Promo {

    private String id;
    private String code;

    public Promo() {
    }

    public Promo(String id, String code) {
        this.id = id;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
