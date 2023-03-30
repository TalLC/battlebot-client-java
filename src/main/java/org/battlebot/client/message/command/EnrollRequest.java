package org.battlebot.client.message.command;

public class EnrollRequest {
	private String team_id;
	private String bot_name;
	
	public EnrollRequest(String team_id, String bot_name) {
		super();
		this.team_id = team_id;
		this.bot_name = bot_name;
	}

	public String getTeam_id() {
		return team_id;
	}

	public String getBot_name() {
		return bot_name;
	}
	
}
