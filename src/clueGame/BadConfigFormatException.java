package clueGame;

public class BadConfigFormatException extends Exception {

	//variable 
	private String configFile;
	
	public BadConfigFormatException(String configurationFile) {
		super();
		this.configFile = configurationFile;
	}

	@Override
	public String toString() {
		return "BadConfigFormatException: The file " + configFile + "is configured incorrectly.";
	}
	
	
	
}
