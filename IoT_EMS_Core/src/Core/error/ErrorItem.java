package Core.error;

public class ErrorItem {
	private int ID;
	private String message;
	
	public ErrorItem( int id , String msg ) {
		ID = id;
		message = msg;
	}
	
	public void setID( int id ) {
		ID = id;
	}
	
	public void setMessage( String msg ) {
		message = msg;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getMessage() {
		return message;
	}
}
