package per.jm.demo.util;

import java.util.ArrayList;
import java.util.Collections;

public class CheckSignature {


    public static String CheckSignatures(String str){
        String[] content=str.split("&");
        String signature=content[0].split("=")[1];
        String timestamp=content[2].split("=")[1];
        String nonce=content[3].split("=")[1];
        //第一步中填写的token一致
        String token="jm12315";

        ArrayList<String> list=new ArrayList<String>();
        list.add(nonce);
        list.add(timestamp);
        list.add(token);

        //字典序排序
        Collections.sort(list);
        //SHA1加密
        String checksignature= SHA1Util.encode(list.get(0)+list.get(1)+list.get(2));
        System.out.println(signature);
        System.out.println(checksignature);

        if(checksignature.equals(signature)){
            return content[1].split("=")[1];
        }
        return null;
    }
}
