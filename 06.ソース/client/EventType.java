package client;
/**
 * event情報を決めるenmuです.
 * @author hatsugai
 * @version 0.1
 */
public enum EventType {
	FINISH(false,true,false),
	ERROR(true,false,false),
	YOURTURN(false,false,true),
	BLANK(false,false,false);

	private boolean isError;
	private boolean isFinish;
	private boolean isTurn;

	private EventType(boolean isError,boolean isFinish,boolean isTurn){

		this.isError=isError;
		this.isFinish=isFinish;
		this.isTurn=isTurn;
	}
/**
 * どのイベントか振り分けます.
 * @param event event情報です。このターンの行動の指示に関する情報が入っています。
 * @return EventTypeを返します。
 */
	public static EventType getEventType(String event){
		if(event.equals("win")||event.equals("lose")||event.equals("draw")){
			return EventType.FINISH;
		}else if(event.equals("error")){
			return EventType.ERROR;
		}else if(event.equals("YourTurn")){
			return EventType.YOURTURN;
		}else{
			return EventType.BLANK;
		}
	}

	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public boolean isFinish(){
		return isFinish;
	}
	public void setFinish(boolean isFinish){
		this.isFinish = isFinish;
	}
	public boolean isTurn(){
		return isTurn;
	}
	public void setTurn(boolean isTurn){
		this.isTurn = isTurn;
	}
}
