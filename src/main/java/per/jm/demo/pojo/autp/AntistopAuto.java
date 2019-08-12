package per.jm.demo.pojo.autp;

public class AntistopAuto {
    private Integer id;

    private String fisrt;

    private String second;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFisrt() {
        return fisrt;
    }

    public void setFisrt(String fisrt) {
        this.fisrt = fisrt == null ? null : fisrt.trim();
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second == null ? null : second.trim();
    }
}