package ShellCirculaire;

import org.mt4j.MTApplication;

public class StartShellCirculaire extends MTApplication {
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		initialize();
	}
	
	public void startUp(){
		this.addScene(new MTShellCirculaire(this, "Multi-Touch Shell Scene"));
	}
	
}
