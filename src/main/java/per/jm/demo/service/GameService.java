package per.jm.demo.service;

import java.io.IOException;
import java.util.List;

public interface GameService {
    //根据人数大小创建房间
    List<String> createGameRoom(int size, String openid, String name, int theSpyNum, String fisrt, String second) throws IOException;
    //人数满了或者房主点击开始游戏都会触发定时事件，定时n秒后开始
    int joinGame(String openId, String name, String roomId);
    //目前想法，开一个线程,直到出游戏结果后才结束
    int startGame(String roomId, String openId);
    //投票,应当是可以同时投票
    int poll(String fromId, int toId, String roomId);
    //裁员,直接调用这个接口，查找投票最多的人，然后标记为出局
    int reduceUser(String roomId);
    //游戏结束，裁员过后判断，如果人数达到结束要求就结束
    int gameOver(String roomMaker);
    int speak(String fromId, String roomId, String content);
    //退出游戏
    int outGameRoom(String openId);
    //关闭游戏房间
    int closeGameRoom(String openId);

}
