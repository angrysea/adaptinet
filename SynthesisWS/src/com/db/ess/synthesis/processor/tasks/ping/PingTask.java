package com.db.ess.synthesis.processor.tasks.ping;

import com.db.ess.synthesis.dvo.PingRequest;
import com.db.ess.synthesis.dvo.PingResponse;
import com.db.ess.synthesis.dvo.PushedData;
import com.db.ess.synthesis.processor.tasks.NonDaoTask;
import com.db.ess.synthesis.sessions.UserSession;
import com.db.ess.synthesis.sessions.UserSessions;

public class PingTask implements NonDaoTask<PingResponse> {
	
	private PingRequest request;
	
	public PingTask(PingRequest request) {
		this.request = request;
	}
	
	public void run(PingResponse res) throws Exception {
		UserSession session = UserSessions.getSession(request.getuserEmail());
		if (session != null) {
		    session.updateTimestamp();
			for(PushedData d : session.getPushedData()) {
				res.setpushedData(d);
			}
			session.clear();
			res.setcode(200); // your session is healthy!
		}
		else {
			res.setcode(500); // your session does not exist!
		}
	}
}
