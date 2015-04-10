package com.valueclickbrands.solr;

import com.valueclickbrands.solr.service.ServiceMain;
import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.zookeeper.service.ZKWatch;

public class TestWatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestWatch().test();
	}

	private void test() {
		Configure.init();
		ZKService zkService = new ZKService("solrcloud001.la1.vcinv.net:2181,solrcloud002.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181", "", "/testRootPath");
		ZKWatch watcher = new ServiceMain(zkService);
		zkService.setWatcher(watcher);
		zkService.start();
		ZKWatch watcher1 = new ServiceMain(zkService);
		zkService.setWatcher(watcher1);
		zkService.initWatcher();
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
