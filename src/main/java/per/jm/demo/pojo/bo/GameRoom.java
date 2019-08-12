package per.jm.demo.pojo.bo;

import per.jm.demo.util.GameThread;

import java.util.List;

public  class  GameRoom {

    private String roomMaker;//房主
    private int size; //满足条件后，自动开始游戏
    private int nowNum; //开启游戏的人数，手动开启
    private int userId; //该谁说话
    private List<GameUserInfor> users;
    private int theSpyNum; //有几个卧底
    private String roomId;//房间号
    private int thePoorNum;//平民人数
    private int liveNum; //活着的人数
    private int polledNum; //已投票人数
    private int thisRurnAt; //判断本轮进行到哪里了
    private Thread th;// 这个房间所在的线程
    private GameThread gth ;//线程类
    private String first;
    private String  second;
    private boolean isStart; //游戏是否开始
    private boolean isPoll;//是否投票完成
    private boolean isSpeak; //是否是描述关键词
    private boolean isCountResult; //是否是计算结果
    private boolean isFreeSpeakTime;// 是否是自由发言时间
    private boolean isPolling; //进行投票
    private String openId;//当前说话人的openId
    private boolean changeWord; //修改关键词
    private Integer position;//在队列中的位置,用于定时退出
    private Integer antiPo; //关键词在数据库中的位置

    public Integer getAntiPo() {
        return antiPo;
    }

    public void setAntiPo(Integer antiPo) {
        this.antiPo = antiPo;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isChangeWord() {
        return changeWord;
    }

    public void setChangeWord(boolean changeWord) {
        this.changeWord = changeWord;
    }

    public synchronized String getOpenId() {
        return openId;
    }

    public synchronized void setOpenId(String openId) {
        this.openId = openId;
    }

    public synchronized boolean isPolling() {
        return isPolling;
    }

    public synchronized void setPolling(boolean polling) {
        isPolling = polling;
    }

    public synchronized boolean isSpeak() {
        return isSpeak;
    }

    public synchronized void setSpeak(boolean speak) {
        isSpeak = speak;
    }

    public synchronized boolean isCountResult() {
        return isCountResult;
    }

    public synchronized void setCountResult(boolean countResult) {
        isCountResult = countResult;
    }

    public synchronized boolean isFreeSpeakTime() {
        return isFreeSpeakTime;
    }

    public synchronized void setFreeSpeakTime(boolean freeSpeakTime) {
        isFreeSpeakTime = freeSpeakTime;
    }

    public synchronized String getFirst() {
        return first;
    }

    public synchronized void setFirst(String first) {
        this.first = first;
    }

    public synchronized String getSecond() {
        return second;
    }

    public synchronized void setSecond(String second) {
        this.second = second;
    }

    public synchronized GameThread getGth() {
        return gth;
    }

    public synchronized void setGth(GameThread gth) {
        this.gth = gth;
    }

    public synchronized Thread getTh() {
        return th;
    }

    public void setTh(Thread th) {
        this.th = th;
    }

    public synchronized int getThisRurnAt() {
        return thisRurnAt;
    }

    public synchronized void setThisRurnAt(int thisRurnAt) {
        this.thisRurnAt = thisRurnAt;
    }

    public synchronized int getLiveNum() {
        return liveNum;
    }

    public synchronized void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }

    public synchronized int getPolledNum() {
        return polledNum;
    }

    public synchronized void setPolledNum(int polledNum) {
        this.polledNum = polledNum;
    }

    public synchronized boolean isStart() {
        return isStart;
    }

    public synchronized void setStart(boolean start) {
        isStart = start;
    }

    public synchronized int getThePoorNum() {
        return thePoorNum;
    }

    public synchronized void setThePoorNum(int thePoorNum) {
        this.thePoorNum = thePoorNum;
    }

    public int getTheSpyNum() {
        return theSpyNum;
    }

    public synchronized void setTheSpyNum(int theSpyNum) {
        this.theSpyNum = theSpyNum;
    }

    public synchronized String getRoomId() {
        return roomId;
    }

    public synchronized void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public synchronized String getRoomMaker() {
        return roomMaker;
    }

    public synchronized void setRoomMaker(String roomMaker) {
        this.roomMaker = roomMaker;
    }
    public synchronized int getSize() {
        return size;
    }

    public synchronized void setSize(int size) {
        this.size = size;
    }

    public synchronized int getNowNum() {
        return nowNum;
    }

    public synchronized void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public synchronized int getUserId() {
        return userId;
    }

    public synchronized void setUserId(int userId) {
        this.userId = userId;
    }

    public synchronized boolean isPoll() {
        return isPoll;
    }

    public synchronized void setPoll(boolean poll) {
        isPoll = poll;
    }

    public synchronized List<GameUserInfor> getUsers() {
        return users;
    }

    public synchronized void setUsers(List<GameUserInfor> users) {
        this.users = users;
    }
}
