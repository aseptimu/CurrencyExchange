package dao.currency;

public class Currency {
    private int id;
    private String code;
    private String full_name;
    private String sign;

    public Currency(int id, String code, String full_name, String sign) {
        this.id = id;
        this.code = code;
        this.full_name = full_name;
        this.sign = sign;
    }

    public Currency(String code, String full_name, String sign) {
        this.code = code;
        this.full_name = full_name;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
