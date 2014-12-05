package com.db.ess.synthesis.sessions;

import java.util.List;

import com.db.ess.synthesis.dvo.PushedData;
import com.db.ess.synthesis.dvo.UserProfile;

public interface UserSession {
	public boolean isLive();
	public List<PushedData> getPushedData();
	public UserProfile getUserProfile();
    public void addPushedData(String key, PushedData data);
	public void updateTimestamp();
	public void clear();
}
