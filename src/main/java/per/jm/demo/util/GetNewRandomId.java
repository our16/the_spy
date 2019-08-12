package per.jm.demo.util;

import java.util.Random;

public class GetNewRandomId {
   //private String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String base = "123456789";
    private  final  int length = 5;
    public  String creatID() {

        long t = System.currentTimeMillis();// 获得当前时间的毫秒数

        Random rd = new Random(t);// 作为种子数传入到Random的构造器中
        StringBuffer content =  new StringBuffer();
      for(int i =0 ; i < length ; i++)
      {
          int str = rd.nextInt(length-1);
          content.append(base.charAt(str));
      }
        return content.toString();
    }

    //暂时无法实现
    public static String RQ_code(){

        return null;
    }
    public static int getUserId(){
        long t = System.currentTimeMillis();
        Random rd = new Random(t);
        int code = 0;
        for(int i =0 ; i <3 ; i++)
        {
        code = rd.nextInt(9999);
        }
        return code;
    }

    public static int getRandomNum(int size){
        long t = System.currentTimeMillis()+12;
        Random rd = new Random(t);

        int id = 0;
        do {
            id = rd.nextInt(9999);
        }while(id > size);

        return id;
    }

    public void test(){
        System.out.println(getRandomNum(3));
    }

}
