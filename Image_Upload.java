/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_upload;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileSystemView;
import java.io.IOException;

/**
 *
 * @author stefancaloian
 */
public class Image_Upload implements ActionListener {

    JPanel panel, panel_2, mainPanel;
    JTextField user, pass;
    File textFile, imageFile;
    JButton signUp, logIn;
    JButton nextImageButton, newImageButton;
    CardLayout layout;
    JLabel newImage;
    int imageNumber;

    public Image_Upload() {
        textFile = new File("login.txt"); // stores the login information

        JFrame frame = new JFrame("Photo_Upload");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();

        panel.setOpaque(true);
        signUp = new JButton("Sign Up");
        signUp.setActionCommand("Sign Up");
        signUp.addActionListener(this);
        panel.add(signUp);

        logIn = new JButton("Login");
        logIn.setActionCommand("Login");
        logIn.addActionListener(this);
        panel.add(logIn);

        user = new JTextField(10);
        panel.add(user);

        pass = new JTextField(10);
        panel.add(pass);

        imageFile = new File("images.txt"); // stores the locations of the images

        panel_2 = new JPanel();
        panel_2.setOpaque(true);

        newImage = new JLabel();
        panel_2.add(newImage);

        nextImageButton = new JButton("Next Image");
        nextImageButton.setActionCommand("Next Image");
        nextImageButton.addActionListener(this);
        panel_2.add(nextImageButton);

        newImageButton = new JButton("New Image");
        newImageButton.setActionCommand("New Image");
        newImageButton.addActionListener(this);
        panel_2.add(newImageButton);

        mainPanel = new JPanel(new CardLayout()); // creates card layout so the panels could be switched out
        mainPanel.add(panel, "Panel 1");
        mainPanel.add(panel_2, "Panel 2");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Sign Up")) {
            BufferedWriter writer = null;
            try {
                imageFile.delete(); // deletes the stored images
                imageFile.createNewFile();
                writer = new BufferedWriter(new FileWriter(textFile));
                writer.write(user.getText()); // gets the user's desired name and password
                writer.newLine();
                writer.write(pass.getText());
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else if (event.getActionCommand().equals("Login")) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(textFile));
                if (user.getText().equals(reader.readLine()) && pass.getText().equals(reader.readLine())) { // if login successful, open upload page
                    CardLayout cl = (CardLayout) (mainPanel.getLayout());
                    cl.show(mainPanel, "Panel 2");
                }
                reader.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else if (event.getActionCommand().equals("Next Image")) {
            BufferedReader picReader = null;
            String picture;
            try {
                picReader = new BufferedReader(new FileReader(imageFile)); 
                imageNumber++;
                while ((picture = picReader.readLine()) != null) { // sets the image as the next one in the file
                    for (int i = 0; i < imageNumber; i++) {
                        ImageIcon img = new ImageIcon(picture); 
                        Image image = img.getImage();
                        Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                        img = new ImageIcon(newimg);
                        newImage.setIcon(img);
                    }
                }
                picReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found exception " + e.getMessage());
            } catch (IOException e) {
                System.out.println("io exception " + e.getMessage());
            }

        } else {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);
            if (event.getActionCommand().equals("New Image") && returnValue == JFileChooser.APPROVE_OPTION) { // chooses an image from the computer and adds it to the file

                BufferedWriter picWriter;
                File selectedFile = jfc.getSelectedFile();
                try {
                    picWriter = new BufferedWriter(new FileWriter(imageFile, true));
                    picWriter.write(selectedFile.getAbsolutePath());
                    picWriter.newLine();
                    picWriter.close();
                } catch (IOException ee) {
                    System.err.println(ee.getMessage());
                }
            }

        }
    }

    private static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        Image_Upload photo = new Image_Upload();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                runGUI();
            }
        });
    }
}
