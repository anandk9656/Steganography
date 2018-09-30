package Steganography_Controller;

import java.io.File;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;


public class Steg_control
{
	
	private final EncrypInpic_GUI	view;
	private final Steganography	model;
        private final JPanel	decode_panel;
	private final JPanel	encode_panel;
	private final JTextArea input;
	private final JButton	encodeButton,decodeButton;
	private final JLabel	image_input;
	private final JMenuItem encode;
	private final JMenuItem decode;
	private final JMenuItem exit;
	private final Encode	enc;
	private final Decode	dec;
	private final EncodeButton encButton;
	private final DecodeButton decButton;
	private String	stat_path = "";
	private String	stat_name = "";

        public Steg_control(EncrypInpic_GUI aView, Steganography aModel)
	{
		
		view  = aView;
		model = aModel;
		encode_panel= view.getTextPanel();
		decode_panel= view.getImagePanel();
		input	= view.getText();
		image_input	= view.getImageInput();
		encodeButton= view.getEButton();
		decodeButton= view.getDButton();
		encode	= view.getEncode();
		decode	= view.getDecode();
		exit	= view.getExit();

		enc = new Encode();
		encode.addActionListener(enc);
		dec = new Decode();
		decode.addActionListener(dec);
		exit.addActionListener(new Exit());
		encButton = new EncodeButton();
		encodeButton.addActionListener(encButton);
		decButton = new DecodeButton();
		decodeButton.addActionListener(decButton);

		
		encode_view();
	}


	private void encode_view()
	{
		update();
		view.setContentPane(encode_panel);
		view.setVisible(true);
	}
	private void decode_view()
	{
		update();
		view.setContentPane(decode_panel);
		view.setVisible(true);
	}

	private class Encode implements ActionListener
	{
		
                @Override
		public void actionPerformed(ActionEvent e)
		{
			encode_view(); 
		}
	}

	
	private class Decode implements ActionListener
	{
                @Override
		public void actionPerformed(ActionEvent e)
		{
			decode_view(); 

			
			JFileChooser chooser = new JFileChooser("./");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new Image_Data());
			int returnVal = chooser.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File directory = chooser.getSelectedFile();
				try{
					String image = directory.getPath();
					stat_name = directory.getName();
					stat_path = directory.getPath();
					stat_path = stat_path.substring(0,stat_path.length()-stat_name.length()-1);
					stat_name = stat_name.substring(0, stat_name.length()-4);
					image_input.setIcon(new ImageIcon(ImageIO.read(new File(image))));
				}
				catch(Exception except) {
				
				JOptionPane.showMessageDialog(view, "The File cannot be opened!",
					"Error!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	
	private class Exit implements ActionListener
	{
		
                @Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}

	
	private class EncodeButton implements ActionListener
	{
		
                @Override
		public void actionPerformed(ActionEvent e)
		{
			
			JFileChooser chooser = new JFileChooser("./");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new Image_Data());
			int returnVal = chooser.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File directory = chooser.getSelectedFile();
				try{
					String text = input.getText();
					String ext  = Image_Data.getExtension(directory);
					String name = directory.getName();
					String path = directory.getPath();
					path = path.substring(0,path.length()-name.length()-1);
					name = name.substring(0, name.length()-4);

					String stegan = JOptionPane.showInputDialog(view,
									"Enter output file name:", "File name",
									JOptionPane.PLAIN_MESSAGE);

					if(model.encode(path,name,ext,stegan,text))
					{
						JOptionPane.showMessageDialog(view, "The Image was encoded Successfully!",
							"Success!", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(view, "The Image could not be encoded!",
							"Error!", JOptionPane.INFORMATION_MESSAGE);
					}
					decode_view();
					image_input.setIcon(new ImageIcon(ImageIO.read(new File(path + "/" + stegan + ".png"))));
				}
				catch(HeadlessException | IOException except) {
				JOptionPane.showMessageDialog(view, "The File cannot be opened!",
					"Error!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

	}

	
	private class DecodeButton implements ActionListener
	{
	        @Override
		public void actionPerformed(ActionEvent e)
		{
			String message = model.decode(stat_path, stat_name);
			System.out.println(stat_path + ", " + stat_name);
			if(!"".equals(message))
			{
				encode_view();
				JOptionPane.showMessageDialog(view, "The Image was decoded Successfully!",
							"Success!", JOptionPane.INFORMATION_MESSAGE);
				input.setText(message);
			}
			else
			{
				JOptionPane.showMessageDialog(view, "The Image could not be decoded!",
							"Error!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	
	public void update()
	{
		input.setText("");			
		stat_path = "";				
		stat_name = "";				
	}

	

	 
	public static void main(String args[])
	{
            Steg_control steg_control = new Steg_control(new EncrypInpic_GUI("Steganography"),new Steganography());
	}

}