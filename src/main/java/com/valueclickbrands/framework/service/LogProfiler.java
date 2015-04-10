package com.valueclickbrands.framework.service;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author 潘冬 2009-7-23 下午09:50:30
 */
public class LogProfiler {
	private static final LRUMap logMap = new LRUMap(100);

	public Object profileMethod(ProceedingJoinPoint call) throws Throwable {
		Log log = obtainLogger(call.getTarget().getClass());
		StopWatch watch = new StopWatch();
		watch.start();
		log.debug("call " + call.getSignature().toLongString());
		Object rt = call.proceed();
		watch.stop();
		log.debug("executing time: "
				+ DurationFormatUtils.formatDuration(watch.getTime(),
						"m:s.S"));
		return rt;
	}

	private Log obtainLogger(Class classObj) {
		Log log = (Log) logMap.get(classObj);
		if (log == null) {
			log = LogFactory.getLog(classObj);
			logMap.put(classObj, log);
		}
		return log;
	}
}
