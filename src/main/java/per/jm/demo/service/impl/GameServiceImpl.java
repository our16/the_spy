package per.jm.demo.service.impl;

import org.springframework.stereotype.Service;
import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;
import per.jm.demo.pojo.bo.RingQuees;
import per.jm.demo.service.GameService;
import per.jm.demo.util.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    //让它一直在内存中切唯一
    private static Map<String, GameRoom> rooms = new HashMap<>() ; //新建一个总的房间，其他房间都放在这里面,key值是房间号

    public static Map<String, GameRoom> getRooms() {
        return rooms;
    }

    public static void setRooms(GameRoom room,String id) {
        rooms.put(id,room);
        System.out.println("room size:"+rooms.size());
    }

    @Override
    public List<String> createGameRoom(int size, String openid,String name ,int theSpy,String fisrt,String second) throws IOException {
       String room =  GameRoomIdForPlayer.getRoomId().get(openid);
       if(room != null)
       {
           List<String> list2 = new ArrayList<>();
           list2.add("你已经在房间里面了");
           return list2;
       }
        GameRoom gr = new GameRoom();
        List<GameUserInfor> users = new ArrayList<>();
        gr.setSize(size);
        gr.setNowNum(1); //房主本人
        gr.setThePoorNum(1);//平民人数包括房主本人
        String  roomId = new GetNewRandomId().creatID();
        gr.setRoomId(roomId); //生成房间号
        GameRoomIdForPlayer.getRoomId().put(openid,roomId);
        gr.setRoomMaker(openid); //设置房主id
       // System.out.println(gr.getRoomMaker());
        gr.setTheSpyNum(theSpy);//卧底人数
        gr.setPoll(false);
        gr.setUserId(0);
        gr.setUsers(null);
        gr.setStart(false);
        gr.setSecond(second);
        gr.setFirst(fisrt);
        gr.setUsers(users);
        gr.setAntiPo(1);
        gr.setChangeWord(false);  // 初始化默认状态为否
        gr.setPosition(null);
        GameRoomFactory.getRooms().put(roomId,gr);
        /**
         *
         * 添加到队列当中
         *
         * */
        RingQuees.setNewPot(roomId);
        //房主自己加入房间
        GameUserInfor gui = new GameUserInfor();
        gui.setOpenId(openid);
        gui.setWxName(name);
        gui.setContent(null);
        gui.setNotPoll(true);
        gui.setGameId(GetNewRandomId.getUserId());
        gui.setPoll(0); //初始票数为零
        gui.setPollWho(0);
        gui.setReciveInfor(true);
        gui.setTheSpy(false);
        gui.setStatus(true);
        gr.getUsers().add(gui); //加入map.put("keyword2","当前人数："+gameRoom.getUsers().size()+1);房间
        List<String> list = new ArrayList<>();
        list.add(roomId);
        String url = null;
        try {
            url = GetQRCode.getQRCode(roomId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(url);
        return list;
    }

    @Override
    public int joinGame(String openId, String name, String roomId) {
        GameRoom gameRoom = GameRoomFactory.getRooms().get(roomId);
        if(gameRoom == null)
            return -1;
        for(GameUserInfor user: gameRoom.getUsers())
        {
            if(user.getOpenId().equals(openId))
                return -4; // 已经加入了
        }
        if(!gameRoom.isStart())
        {
            int size = gameRoom.getUsers().size();
            int num = size;
            GameUserInfor gui = new GameUserInfor();
            gui.setOpenId(openId);
            gui.setWxName(name);
            //调用微信接口,向所有人推送，主动的。
           // System.out.println(name + ":加入游戏 ,他的openId 是："+openId);

            gui.setContent(null);
            gui.setNotPoll(true);
            gui.setGameId(GetNewRandomId.getUserId());
            gui.setPoll(0); //初始票数为零
            gui.setPollWho(0);
            gui.setReciveInfor(true);
            gui.setTheSpy(false);
            gui.setStatus(true);
            gameRoom.getUsers().add(gui);//加入房间
            num++; //玩家人数加一
            gameRoom.setNowNum(num);//当前人数加一
            Map<String,String> map = new HashMap<>();
            map.put("first","新玩家进入房间");
            map.put("keyword1","他的昵称是："+name);
            map.put("keyword2","当前房间人数:"+gameRoom.getNowNum()+"\n");
            map.put("remark",new Date()+"");
            WxMessageUtil.sendToAllPlayer(gameRoom,name,map);
            System.out.println("当前游戏人数:"+gameRoom.getNowNum()+",预设大小为："+gameRoom.getSize());
            GameRoomIdForPlayer.getRoomId().put(openId,roomId);//新增
            if(gameRoom.getNowNum() == gameRoom.getSize())
            {
                System.out.println("自动开始游戏");
                //开始游戏
                this.startGame(roomId,gameRoom.getRoomMaker());
                return 1; //游戏开始
            }
        }else {
            return -2; //游戏已经开始不能加入游戏
        }
        return 1;
    }

    @Override
    public int startGame(String roomId,String openId) {
        //获取房间,不需要房间id
        GameRoom gameRoom = GameRoomFactory.getRooms().get(GameRoomIdForPlayer.getRoomId().get(openId));
        if (gameRoom == null) return -1;
        if(gameRoom.isStart()) return -5; //游戏已经开始了
        if (gameRoom.getNowNum() < 3) return -3;//人数还没有满
        if (!gameRoom.getRoomMaker().equals(openId)) //验证是不是房主
            return -2;
        /**
         * 先重置相关参数
         * */
        for(GameUserInfor user: gameRoom.getUsers())
        {
            //重置说话权限
            user.setThisTurnIsSpoken(false);
            //重置票数
            user.setPoll(0);
            //重置投票对象
            user.setPollWho(-1);
            //重置为都不是卧底
            user.setTheSpy(false);
            //重置为没投过票
            user.setNotPoll(true);
            //设置玩家都为存活状态
            user.setStatus(true);
        }
        /**
         * 生成随机卧底
         * */
        int flag = GetNewRandomId.getRandomNum(gameRoom.getNowNum());
        choice(gameRoom);
        for(GameUserInfor userInfor:gameRoom.getUsers())
        {
            if(userInfor.isTheSpy())
            {
                System.out.println("卧底："+userInfor.getWxName());
            }else
            {
                System.out.println("平民:"+userInfor.getWxName());
            }
        }
        //总人数减去卧底人数就是平民人数
        gameRoom.setThePoorNum(gameRoom.getNowNum() - gameRoom.getTheSpyNum());
        System.out.println("游戏开始,卧底人数"+gameRoom.getTheSpyNum()+",平民人数："+gameRoom.getThePoorNum());
        //初始化完成开始游戏
        gameRoom.setStart(true);
        gameRoom.setPolling(false);
        gameRoom.setPoll(false);
        gameRoom.setCountResult(false);
        gameRoom.setSpeak(true);//开始描述关键词
        gameRoom.setFreeSpeakTime(false);
        gameRoom.setPolledNum(0);
        gameRoom.setThisRurnAt(0);
        System.out.println("游戏状态："+gameRoom.isStart()+",\n"
        +"投票状态:"+gameRoom.isPolling()+"\n"
        +"投票完成状态："+gameRoom.isPoll()+"\n"
        +"计算结果状态:"+gameRoom.isCountResult()+"\n"
        +"");
        //当前活着的人
        gameRoom.setLiveNum(gameRoom.getUsers().size());
       // System.out.println("start live num:"+gameRoom.getUsers().size());
        int j =0 ;
        //int flag = GetNewRandomId.getRandomNum(gameRoom.getTheSpyNum());
        for(GameUserInfor user: gameRoom.getUsers())
        {
            if(j == flag)
            {
               gameRoom.setUserId(user.getGameId());
               gameRoom.setOpenId(user.getOpenId());
               //选取第一个发言人
               break;
            }
            ++j;
        }
        GameThread gt = new GameThread(gameRoom);
        Thread thread = new Thread(gt);
        System.out.println("开始游戏");
        gameRoom.setGth(gt);
        gameRoom.setTh(thread);
       /* gameRoom.getGth().setWait(true);
        gameRoom.getGth().setOver(false);*/
        thread.start();//异步执行
        return 1;
    }
    @Override
    public int poll(String fromId, int toId,String roomId) {
        GameRoom gameRoom = GameRoomFactory.getRooms().get(roomId);
        if(gameRoom.isPolling())
        {
            //先对房间信息进行操作，再停止组塞
            //测试，通过定时器
           // System.out.println(gameRoom);
            if(gameRoom == null)
                return  0;
           // gameRoom.getGth().setWait(false);
           int re =  GamePollUtil.polling(roomId,fromId,toId); //投票
            if(re == -1)
                //不能投给自己
                return -3;
            if(re == -2)
                return -2;
            return 1;
        }
        return -4; //还没有开始投票，或投票结束
    }
    @Override
    public int reduceUser(String rommId) {
        return 0;
    }

    @Override
    public int gameOver(String roomMaker) {
        GameRoom gameRoom = GetGameRoomByMakerOpenId.getGameRoomByOpenIg(roomMaker);
        if(gameRoom == null)
            return  -1;
        gameRoom.getGth().setWait(false);
        gameRoom.getGth().setOver(true);
        return 1;
    }

    //阐述观点
    @Override
    public int speak(String fromId, String roomId, String content) {
        System.out.println("接收到："+fromId+"房间idL:"+roomId+".说："+content);
        GameRoom gameRoom = GameRoomFactory.getRooms().get(roomId);
        if(gameRoom == null) {
            System.out.println("房间id错误");
            return -1;
        }
        for(GameUserInfor user :gameRoom.getUsers())
        {
            if(fromId.equals(user.getOpenId()) && user.isStatus())
            {
                System.out.println("当前玩家："+user.getWxName());
                user.setContent(content);
                break;
            }
        }
        gameRoom.getGth().setWait(false); //进行一次消息转发
        return 0;
    }

    @Override
    public int outGameRoom(String openId) {
        GameRoom room = GameRoomFactory.getRooms().get(GameRoomIdForPlayer.getRoomId().get(openId));
        if(room == null)
        {
            return -1; //没有加入房间
        }
        else
        {
            int i = 0;

            for(GameUserInfor user:room.getUsers())
            {
                if(user.getOpenId().equals(openId))
                {
                    GameRoomIdForPlayer.getRoomId().remove(openId);
                    int totalNum = room.getLiveNum();
                    int nowNum  = room.getNowNum();
                    //更新房间参数
                    //前面参数先设置好
                    if(room.getUserId() == user.getGameId())
                    {
                        //这样最简单
                       return  -2;

                    }

                    if(user.isTheSpy()) {
                        /**
                         *
                         * 对卧底的操作
                         * */
                        int spyNum = room.getTheSpyNum();
                        if(user.isStatus()){
                            //还活着就要判断是不是最后一个卧底，要决定是否结束

                            if(spyNum <= 1 && room.isStart())
                            {
                                //卧底人数减一
                                room.setTheSpyNum(--spyNum);
                                //活着的人数减一
                                room.setLiveNum(--totalNum);
                                //总人数减一
                                room.setNowNum(--nowNum);
                                //设置游戏结束
                                room.getGth().setWait(false);
                                room.getGth().setOver(true);
                            }else
                            {
                                //卧底人数减一
                                room.setTheSpyNum(--spyNum);
                                //活着的人数减一
                                room.setLiveNum(--totalNum);
                                //总人数减一
                                room.setNowNum(--nowNum);
                            }
                        }else{
                            //卧底人数减一
                            room.setTheSpyNum(--spyNum);
                            //活着的人数减一
                            room.setLiveNum(--totalNum);
                            //总人数减一
                            room.setNowNum(--nowNum);
                        }
                    }else {
                        /**
                         *
                         * 对平民的操作
                         * */
                        int poorNum = room.getThePoorNum(); //获取平明人数
                        if (user.isStatus()) {
                            if (poorNum <= 2 && room.isStart()) {
                                //卧底人数减一
                                room.setThePoorNum(--poorNum);
                                //活着的人数减一
                                room.setLiveNum(--totalNum);
                                //总人数减一
                                room.setNowNum(--nowNum);
                                //设置游戏结束
                                room.getGth().setWait(false);
                                room.getGth().setOver(true);
                            } else {
                                //卧底人数减一
                                room.setThePoorNum(--poorNum);
                                //活着的人数减一
                                room.setLiveNum(--totalNum);
                                //总人数减一
                                room.setNowNum(--nowNum);
                            }
                        } else {
                            //卧底人数减一
                            room.setThePoorNum(--poorNum);
                            //活着的人数减一
                            room.setLiveNum(--totalNum);
                            //总人数减一
                            room.setNowNum(--nowNum);
                        }
                    }
                    room.getUsers().remove(i);
                    Map<String,String> map = new HashMap<>();
                    map.put("first","玩家退出房间");
                    map.put("keyword1","他的昵称是："+user.getWxName());
                    map.put("keyword2","当前房间人数:"+room.getNowNum()+"\n");
                    map.put("remark",new Date()+"");
                    WxMessageUtil.translateSpeakContent(room,user,map);
                    return 1;
                }
                i++;
            }
        }
        return 0;
    }

    @Override
    public int closeGameRoom(String openId) {
        GameRoom room = GameRoomFactory.getRooms().get(GameRoomIdForPlayer.getRoomId().get(openId));
        if(room == null)
        {
            return -1; //没有创建房间
        }
        if(room.getRoomMaker().equals(openId))
        {
            GameRoomFactory.getRooms().remove(room.getRoomId());
            for(GameUserInfor user: room.getUsers())
            {
                GameRoomIdForPlayer.getRoomId().remove(user.getOpenId());
            }
            return 1;
        }
        return 0;
    }

    private void choice(GameRoom gameRoom){
        int i = 0;
        int flag = -1;
        do
        {
            flag = GetNewRandomId.getRandomNum(Integer.parseInt((System.currentTimeMillis()%1000)+""));
            if(flag > -1 && flag < gameRoom.getUsers().size())
            {
                if (!gameRoom.getUsers().get(i).isTheSpy()){
                    gameRoom.getUsers().get(i).setAntistop(gameRoom.getFirst());
                    gameRoom.getUsers().get(i).setTheSpy(true);
                    i++;
                    System.out.println("设置了一个卧底****************************");
                }
            }
        } while(i < gameRoom.getTheSpyNum());
    }
}
