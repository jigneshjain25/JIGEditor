import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JScrollPane;

import org.fife.ui.rtextarea.RTextScrollPane;


public class openListener implements ActionListener, Constants {
	
	Main frame=null;
	
	public openListener(Main main) {
		frame = main;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		frame.fileOpen.showOpenDialog(frame.frame);
		openFile(frame.fileOpen.getSelectedFile());			
	}

	void openFile(File file){
		RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt(file);
		RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
		scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ButtonTabComponent btc = new ButtonTabComponent(frame.jTabbedPane);
		frame.jTabbedPane.add(file.getName(),scrollerCur); 
		frame.jTabbedPane.setTabComponentAt(frame.jTabbedPane.getTabCount()-1, btc);
		frame.jTabbedPane.setSelectedIndex(frame.jTabbedPane.getTabCount()-1);	        						     
		
		try{	
				FileReader fileReader=new FileReader(file);
				BufferedReader reader=new BufferedReader(fileReader);
				String line=null;
				while((line=reader.readLine())!=null)
					editorCur.append(line+"\n");
				
			}catch(Exception ex){
			System.out.println("ERROR OPENING THE FILE");
			}
		
		editorCur.requestFocusInWindow();
		editorCur.changed=false;
}	
}
