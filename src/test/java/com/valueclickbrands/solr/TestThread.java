package com.valueclickbrands.solr;

import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
  
/** 
 * @author nineday 
 */  
public class TestThread {  
  
    public static void main(String args[]) throws InterruptedException {  
        ExecutorService exe = Executors.newFixedThreadPool(1);  
        for (int i = 1; i <= 2; i++) {  
            exe.execute(new SubThread(i));  
        }  
//        exe.shutdown();
        while (true) {  
            if (exe.isShutdown()) {  
                System.out.println("½áÊøÁË£¡");  
                break;  
            }  
            Thread.sleep(200);  
        }  
    }
}

 
class SubThread extends Thread{  
      
    private final int i;  
    public SubThread(int i){  
        this.i = i;  
    }  
    @Override  
    public void run(){  
        System.out.println("start" + i);
        try {
			currentThread().sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("end" + i);
    }  
}  