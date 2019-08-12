package per.jm.demo.pojo.bo;

public class GameUserInfor {
    private int gameId; // 本局游戏的id
    private String openId;
    private String wxName;
    private boolean status;//是否存活
    private boolean isTheSpy; //是不是卧底
    private boolean notPoll;  //投过票没有
    private int poll;//每轮的票数
    private String content; //说的话
    private int pollWho; //本轮投给谁
    private boolean isReciveInfor; //是否接收消息，阵亡后可选择
    private String antistop;//关键字
    private boolean thisTurnIsSpoken; //标志本轮已经说过话了

    public boolean isThisTurnIsSpoken() {
        return thisTurnIsSpoken;
    }

    public void setThisTurnIsSpoken(boolean thisTurnIsSpoken) {
        this.thisTurnIsSpoken = thisTurnIsSpoken;
    }

    public String getAntistop() {
        return antistop;
    }

    public void setAntistop(String antistop) {
        this.antistop = antistop;
    }

    public boolean isNotPoll() {
        return notPoll;
    }

    public void setNotPoll(boolean notPoll) {
        this.notPoll = notPoll;
    }

    public boolean isTheSpy() {
        return isTheSpy;
    }

    public void setTheSpy(boolean theSpy) {
        isTheSpy = theSpy;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPoll() {
        return poll;
    }

    public void setPoll(int poll) {
        this.poll = poll;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPollWho() {
        return pollWho;
    }

    public void setPollWho(int pollWho) {
        this.pollWho = pollWho;
    }

    public boolean isReciveInfor() {
        return isReciveInfor;
    }

    public void setReciveInfor(boolean reciveInfor) {
        isReciveInfor = reciveInfor;
    }


}
