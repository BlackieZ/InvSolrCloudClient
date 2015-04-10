package com.valueclickbrands.solr.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.solr.util.SendMailUtil;

public class MonitorZkQueue {
	private static Logger logger = Logger.getLogger(MonitorZkQueue.class);
	private static ZKService zkService;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	
	private int process() {
		logger.info("MonitorZkQueue start");
		int result= 0;
		Configure.init();
		zkService = new ZKService(Configure.ZookeeperHosts, "", Configure.ZookeeperQueuePath);
		zkService.getcService().start();
		int taskCount = getTaskCount();
		String zkTaskProcessTimeStr = zkService.getPathValue(Configure.ZkTaskProcessTimePath);
		long zkTaskProcessTime = 0;
		if (StringUtils.isNotEmpty(zkTaskProcessTimeStr)) {
			zkTaskProcessTime = Long.parseLong(zkTaskProcessTimeStr);
		}
		
		try {
			Thread.sleep(Configure.MonitorZkGetTaskSleepTime*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		String zkTaskProcessTimeStr2 = zkService.getPathValue(Configure.ZkTaskProcessTimePath);
		long zkTaskProcessTime2 = 0;
		if (StringUtils.isNotEmpty(zkTaskProcessTimeStr2)) {
			zkTaskProcessTime2 = Long.parseLong(zkTaskProcessTimeStr2);
		}
		zkService.getcService().close();
		long currentTime = System.currentTimeMillis();
		long timeInterval = currentTime - zkTaskProcessTime;
		String msg = "taskCount=" + taskCount + ", zkTaskProcessTime=" + format.format(new Date(zkTaskProcessTime)) + ", currentTime=" + format.format(new Date(currentTime) + ". ");
		if (zkTaskProcessTime2 != 0 && zkTaskProcessTime == zkTaskProcessTime2 &&
				taskCount > Configure.MonitorAllowCount && timeInterval > Configure.MonitorZkTaskTimeOut*1000) {
			if (StringUtils.isNotEmpty(Configure.MonitorStopServerShellPath)) {
				if (!new File(Configure.MonitorStopServerShellPath).exists()) {
					logger.error("The excute file is not exists. file=" + Configure.MonitorStopServerShellPath);
				}
			} else {
				logger.error("MonitorStopServerShellPath can't be null.");
			}
			if (StringUtils.isNotEmpty(Configure.MonitorStartServerShellPath)) {
				if (!new File(Configure.MonitorStartServerShellPath).exists()) {
					logger.error("The excute file is not exists. file=" + Configure.MonitorStartServerShellPath);
				}
			} else {
				logger.error("MonitorStartServerShellPath can't be null.");
			}
			runShellForWait(Configure.MonitorStopServerShellPath);
			String excString = "";
			if (!new File(Configure.MonitorServerPidFilePath).exists()) {
				msg += excString = " stop server success.";
				logger.info(excString);
				runShell(Configure.MonitorStartServerShellPath);
				msg += excString = " restart server.";
				logger.info(excString);
			} else {
				msg += excString = " stop server fail.";
				logger.error(excString);
			}
			SendMailUtil send = new SendMailUtil();
			send.sendBatchReport("Zookeeper Queue Task Error", "The zookeeper Queue Task is not work, Please check it. " + msg);
			result = 1;
		}
		logger.info(msg);
		logger.info("MonitorZkQueue end");
		return result;
	}
	
	private int getTaskCount(){
		List<String> list = zkService.getChildrenList(Configure.ZookeeperQueuePath);
		return list!=null?list.size():0;
	}
	
	private void runShell(String shStr) {
		try {
			Runtime.getRuntime().exec(shStr);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
    private List<String> runShellForWait(String shStr) {
    	List<String> strList = new ArrayList<String>();
    	
        Process process;
        try {
			process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr});
	        InputStreamReader ir = new InputStreamReader(process
	                .getInputStream());
	        LineNumberReader input = new LineNumberReader(ir);
	        String line;
	        if (process != null) {
	        	logger.info("pid:" + process.toString());
	        	process.waitFor();
	        }
	        
	        while ((line = input.readLine()) != null){
	            strList.add(line);
	        }
	        logger.info("runShell success.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
        
        return strList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MonitorZkQueue().process();
	}

}
