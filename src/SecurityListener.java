import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SecurityListener implements ActionListener{
	
	Main frame = null;
	String key = "JIGEditor";	
	
	public SecurityListener(Main main) {
		frame = main;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
