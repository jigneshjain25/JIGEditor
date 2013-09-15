import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fife.ui.rtextarea.RTextScrollPane;


public class defaultCodeListener implements ActionListener,Constants{

	Main frame=null;
	int codeNo;
	
	public defaultCodeListener(Main main,int c) {
		frame = main;
		codeNo = c;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		final JFrame defC = new JFrame("Default code for "+CHECKMENUCODES[codeNo]);
		defC.setSize(480, 300);
		final RSyntaxTextAreaExt defaultCode = new RSyntaxTextAreaExt();
		defaultCode.setSyntaxEditingStyle(STYLECODES[codeNo]);
		defaultCode.setRows(40);
		defaultCode.setColumns(30);
		RTextScrollPane scrollerCur = new RTextScrollPane(defaultCode);
		scrollerCur.setHorizontalScrollBarPolicy(RTextScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollerCur.setVerticalScrollBarPolicy(RTextScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JPanel textP = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		textP.add(defaultCode, scrollerCur);
		
		File theDir = new File(".jigeditor_defcode");
		if(!theDir.exists())theDir.mkdir();
		
		File file = new File(".jigeditor_defcode/"+CHECKMENUCODES[codeNo]+".txt");
		if (file.exists())
		{
			try{	
				FileReader fileReader=new FileReader(file);
				BufferedReader reader=new BufferedReader(fileReader);
				String line=null;
				while((line=reader.readLine())!=null)
					defaultCode.append(line+"\n");
				
			}catch(Exception ex){
			System.out.println("ERROR OPENING THE FILE");
			}
		}
		else
		{
			try
			{
				BufferedWriter writer=new BufferedWriter(new FileWriter(file));
				defaultCode.setRows(10);
			}
			catch (IOException e1)
			{
				System.out.println("hi lol");
			}
		}
		
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				File file = new File(".jigeditor_defcode/"+CHECKMENUCODES[codeNo]+".txt");
				try
				{
					BufferedWriter writer=new BufferedWriter(new FileWriter(file));
					writer.write(defaultCode.getText());
					writer.close();	
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				defC.dispose();
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defC.dispose();
			}
		});
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		defC.add(textP, BorderLayout.CENTER);
		defC.add(buttonPanel, BorderLayout.EAST);
		defC.setLocationRelativeTo(null);
		defC.setVisible(true);
	}
}
