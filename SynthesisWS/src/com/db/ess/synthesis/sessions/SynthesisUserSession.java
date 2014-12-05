package com.db.ess.synthesis.sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.db.ess.synthesis.dvo.PushedData;
import com.db.ess.synthesis.dvo.UserProfile;

import java.util.Collections;

public class SynthesisUserSession implements UserSession {

	private UserProfile userProfile;
	private Map<String, PushedData> pushedData;
	private long timestamp; 
    public static final long SESSION_TIMEOUT = 1000 * 60; // 1 minute
	
	public SynthesisUserSession(UserProfile up) {
		this.userProfile = up;
		pushedData = new ConcurrentHashMap<String, PushedData>();
		timestamp = System.currentTimeMillis();
	}
	
	@Override
	public boolean isLive() {
		return System.currentTimeMillis() - timestamp < SESSION_TIMEOUT;
	}

	@Override
	public List<PushedData> getPushedData() {
		return Collections.unmodifiableList(new ArrayList(pushedData.values()));
	}

	@Override
	public UserProfile getUserProfile() {
		return userProfile;
	}

	@Override
	public void addPushedData(String key, PushedData data) {
		pushedData.put(key, data);
	}
	
	@Override
	public void updateTimestamp() {
		timestamp = System.currentTimeMillis();
	}

	@Override
	public void clear() {
		pushedData.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (userProfile != null) {
			sb.append("User email: ");
			sb.append(userProfile.getemail());
		} else {
			sb.append("Invalid session: UserProfile is null.");
		}
		return sb.toString();
	}
}
