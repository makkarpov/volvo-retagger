package ru.makkarpov.retagger;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class Utils {
	protected static JFileChooser jc = new JFileChooser("");
	
	public static JButton fileOpenButton(final JTextField fileField) {
		JButton r = new JButton("...");
		
		Dimension d = r.getSize();
		d.height = fileField.getSize().height;
		r.setSize(d);
		
		r.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					try {
						fileField.setText(jc.getSelectedFile().getCanonicalPath());
					} catch (IOException e1) {
						e1.printStackTrace();
						fileField.setText(e1.getClass().getCanonicalName());
					}
			}
		});
		
		return r;
	}
}
