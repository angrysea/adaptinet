package com.db.ess.synthesis.dao.tasks.exception;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.PushedData;
import com.db.ess.synthesis.sessions.UserSession;
import com.db.ess.synthesis.sessions.UserSessions;
import com.db.ess.synthesis.util.ESSLocation;

public class PendingExceptionCountService {
    
	private static final Logger logger = Logger.getLogger(PendingExceptionCountService.class.getName());
	private static PendingExceptionCountService instance = new PendingExceptionCountService();
    
	private ScheduledExecutorService scheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor();
	public static final long INITIAL_DELAY = 5; // in seconds
	public static final long PERIOD = 10; // in seconds
	
    public static PendingExceptionCountService instance() {
    	return instance;
    }
    
    public void start() {
    	scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
    		public void run() {
    			try {
    				for(UserSession s : UserSessions.getAllSessions()) {
    					int userId = s.getUserProfile().getuserId();
    					int newYorkCount = TaskHelper.getPendingCount(userId, ESSLocation.NEWYORK);
    					int londonCount = TaskHelper.getPendingCount(userId, ESSLocation.LONDON);
    					//logger.info(String.format(">>> userId[%d], newYorkCount[%d], londonCount[%d]", 
    					//		userId, newYorkCount, londonCount));
    					PushedData data = new PushedData();
    					data.settarget("Exception");
    					data.setmessage(newYorkCount + ":" + londonCount);
    					s.addPushedData("PendingException", data);
    				}
    			} catch (Exception ex) {
    				logger.error(">>> failed to run PendingExceptionCountService..", ex);
    			}
    		}
    	},
    	INITIAL_DELAY,
    	PERIOD,
    	TimeUnit.SECONDS);
    }
    
    public void stop() {
    	logger.info(">>> shutting down PendingExceptionCountService...");
    	scheduledExecutorService.shutdown();
    }
}
