package com.conceptualGraph.model;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class App 
{
    int fileListCounter = 1;
    JFrame frame;
    JMenuBar menuBar;
    final JFileChooser fileChooser = new JFileChooser();
    ArrayList<File> fileArrayList = new ArrayList<File>();
    TextArea textArea;
    Button enterFileNumberButton;
    TextField chooseFileTextField;


    protected void start() {
        frame = new JFrame("Концептуальный граф");
        textArea =new TextArea();
        chooseFileTextField = new TextField();
        enterFileNumberButton = new Button("Enter file number");
        Panel mainPanel = new Panel();
        enterFileNumberButton.addActionListener(new enterFileNumberActionListener());
        mainPanel.add(textArea);
        mainPanel.add(chooseFileTextField);
        mainPanel.add(enterFileNumberButton);
        frame.getContentPane().add(mainPanel);
        menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Файл");
        JMenuItem selectDirItem = new JMenuItem("Выбрать директорию");
        selectDirItem.addActionListener(new selectDirItemActionListener());
        mainMenu.add(selectDirItem);
        menuBar.add(mainMenu);
        frame.setJMenuBar(menuBar);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Simple text documents", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft documents", "doc"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class selectDirItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) scanDirectory(fileChooser.getSelectedFile());

        }
    }

    private void scanDirectory(File selectedFile) {
    final String[] okFileExtensions = new String[] {"docx", "doc", "html","txt"};

    for (File file:selectedFile.listFiles()){

        if(file.isDirectory()){
            scanDirectory(file);
        } else {
                for (String extension : okFileExtensions)
                {
                    if (file.getName().toLowerCase().endsWith(extension))
                    {
                        textArea.append(fileListCounter + ") " + file.getAbsolutePath() + "\n");
                        fileArrayList.add(file);
                        fileListCounter++;
                    }
                }
            }
        }
    };

    private class enterFileNumberActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int enteredFileNumber=Integer.parseInt(chooseFileTextField.getText());
            if (enteredFileNumber>0 && enteredFileNumber<fileArrayList.size()){
                Reader.checkAndRead(fileArrayList.get(enteredFileNumber - 1));
            }
        }
    }

}
