package client;

import net.sf.json.JSONObject;

public class CreateGameInfo {
	JSONObject gameInfo = new JSONObject();

	public JSONObject yourturn(){
		gameInfo.put("event", "YourTurn");
		String[][] loc;
		loc = new String[3][3];
		for(int aa = 0;aa<3;aa++){
			for(int bb = 0;bb<3;bb++){
				loc[aa][bb]="-";
			}
		}
		gameInfo.put("location",loc);
		return gameInfo;
	}

	public JSONObject win(){
		gameInfo.put("event", "win");
		return gameInfo;
	}

	public JSONObject error(){
		String[] error = new String[2];
		error[0]= "oversubscribed";
		error[1]="sameLogic";
		gameInfo.put("error[]", error);
		return gameInfo;
	}

}
