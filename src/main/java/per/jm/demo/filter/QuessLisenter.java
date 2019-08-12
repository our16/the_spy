package per.jm.demo.filter;

import per.jm.demo.pojo.bo.QueesList;
import per.jm.demo.pojo.bo.RingQuees;
import per.jm.demo.util.QuartzUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 1.初始化时创建一个循环队列；
 * 2.关闭时就关闭循环队列
 * */
public class QuessLisenter implements ServletContextListener {
    private final Integer maxSize = 10;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /**
         * 初始化一个循环队列，用于定时清除过期的房间
         * */
        // 新建一个头结点
        QueesList queesListHead = new QueesList(1,null,null,null,null);
        QueesList before = queesListHead;//指针
        //使用循环创建一个指定大小的循环队列
        int i = 1;
        //第29 个时退出
        while (i < maxSize-1){
            i++;
            System.out.println(i);
            QueesList aNewOne = new QueesList(i,i-1,before,null,null);
            before.setNext(aNewOne);
            before = aNewOne; //指向新节点
        }
        System.out.println("循环结束："+i);
        //第30个和第一个连接起来
        QueesList last = new QueesList(i+1,i,before,queesListHead,null);
        before.setNext(last);
        queesListHead.setBefore(last);
        RingQuees.getRooms().put("timer",queesListHead); //保存这个队列
        System.out.println("开启定时器,保存的结点:"+queesListHead.getNum());
        QuartzUtil.getQuartz();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        QuartzUtil.stopQuartz();
    }
}
