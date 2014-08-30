import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * A swing GUI for the KeyFinder class.  Has a text box for text to be pasted into
 * and a cute little button!
 * @author Jared
 *
 */
public class MainFrame extends JFrame implements ActionListener{
	
	private KeyFinder finder;
	private JButton findKey; //cute button
	private JLabel label; //key itself
	private JTextArea chords; //paste into this
	private JScrollPane scroll; //adds scroll bar to text box
	private String title;
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
	
	public MainFrame() {
		setSize(500, 500);
		label = new JLabel();
		findKey = new JButton("Find Key");
		try {
			finder = new KeyFinder("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		findKey.addActionListener(this);
		chords = new JTextArea("Put text here.");
		title = "KeyFinder:  Paste in chords and/or lyrics and click Find Key!";
		scroll = new JScrollPane(chords);
		setLayout(new BorderLayout());
		
		add(label, BorderLayout.SOUTH);
		add(findKey, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == findKey) {
			finder.setChords(chords.getText());
			label.setText(finder.findKey());
		}
	}
}
