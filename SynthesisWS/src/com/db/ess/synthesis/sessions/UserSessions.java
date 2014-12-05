package com.db.ess.synthesis.sessions;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import java.util.Collections;

public class UserSessions {

	private static final Logger logger = Logger.getLogger(UserSessions.class.getName());
	
	private static final ConcurrentMap<String, UserSession> sessionMap = 
		new ConcurrentHashMap<String, UserSession>();
    private static Timer cleanupTimer = new Timer();
    
    static {
    	startCleanupTask();
    }
	
    public static void addSession(String userEmail, UserSession session) {
    	if (userEmail == null || session == null) {
    		throw new InvalidParameterException("userEmail="+userEmail + ", session="+session);
    	}
    	//logger.info("Saving a user session for " + userEmail);
    	if (sessionMap.containsKey(userEmail)) {
    		logger.warn(userEmail + " already exists. The new session overrides the old one.");
    	}
    	sessionMap.put(userEmail, session);
    }
    
    public static UserSession getSession(String userId) {
    	if (userId == null) {
    		throw new InvalidParameterException("userId is null");
    	}
    	return sessionMap.get(userId);
    }
    
    public static List<UserSession> getAllSessions() {
    	return Collections.unmodifiableList(new ArrayList(sessionMap.values()));
    }
    
    public static void removeSession(String userEmail) {
    	if (userEmail == null) {
    		throw new InvalidParameterException("userEmail is null");
    	}
    	//logger.info("Removing a user session for " + userEmail);
   		sessionMap.remove(userEmail);
    }
    
    public static void startCleanupTask() {
  		cleanupTimer.schedule(new CleanupTask(),
  				CleanupTask.CLEANUP_DELAY, CleanupTask.CLEANUP_PERIOD);
    }
    
    static class CleanupTask extends TimerTask {
    	public static final long CLEANUP_PERIOD = 1000 * 60; // 1 minute
    	public static final long CLEANUP_DELAY = 1000 * 5; // 5 seconds
    	public void run() {
    		try {
    		//	logger.info("CleanupTask started cleaning up...");
    			Iterator iter = sessionMap.entrySet().iterator();
    			while(iter.hasNext()) {
    				Map.Entry<String, UserSession> entry = (Map.Entry<String, UserSession>) iter.next();
    				String userEmail = entry.getKey();
    				UserSession session = entry.getValue();
    				if (!session.isLive()) {
    					session.clear();
    					iter.remove();
    		//			logger.info(userEmail + "'s session is removed because expired.");
    				}
    			}
    		} catch (Exception ex) {
    			logger.error("CleanupTask failed to run..", ex);	
    		}
    	}
    }

}
