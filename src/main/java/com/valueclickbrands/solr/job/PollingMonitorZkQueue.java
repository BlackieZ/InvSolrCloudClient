package com.valueclickbrands.solr.job;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import com.valueclickbrands.solr.service.ServiceMain;

public class PollingMonitorZkQueue {
	private static Logger logger = Logger.getLogger(PollingMonitorZkQueue.class);
	private ServiceMain serviceMain;
	private static final ReentrantLock PollingMonitorZkQueue = new ReentrantLock();

	public void setServiceMain(ServiceMain serviceMain) {
		this.serviceMain = serviceMain;
	}
	
	public void execute() {
		if (PollingMonitorZkQueue.tryLock()) {
			try {
				logger.info("PollingMonitorZkQueue start.");
				int taskCount = serviceMain.getTaskService().getTaskCount();
				if (taskCount > 0) {
					serviceMain.dataChangeNotify();
				}
				logger.info("PollingMonitorZkQueue end. taskCount=" + taskCount);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				PollingMonitorZkQueue.unlock();
			}
		}else {
			logger.info("PollingMonitorZkQueue is already excuting!");

		}

		
	}

}
