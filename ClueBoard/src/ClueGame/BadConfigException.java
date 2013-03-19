package ClueGame;

public class BadConfigException extends Exception {
	private String message;
	
	BadConfigException(String message){
		this.message = message;
	}

	
	@Override
	public String toString() {
		return message;
	}
	
}
