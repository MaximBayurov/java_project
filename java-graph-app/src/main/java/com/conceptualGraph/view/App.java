package com.conceptualGraph.view;

import com.conceptualGraph.BookGenerator;
import com.conceptualGraph.model.Reader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;


public class App 
{
    private int fileListCounter = 1;
    private JFrame frame;
    private final JFileChooser fileChooser = new JFileChooser();
    private ArrayList<File> fileArrayList = new ArrayList<>();
    private TextArea textArea;
    private TextField chooseFileTextField;

    /**
     * Конструктор устанавливающий свойства,здесь нужно устанавливать значения полученных полей из полученных свойств
     * @param properties свойства, передаваемые Configurator
     */
    public App(Properties properties) {
    }

    public void start() {
        frame = new JFrame("Концептуальный граф");
        textArea = new TextArea();
        chooseFileTextField = new TextField();
        Button enterFileNumberButton = new Button("Enter file number");
        Panel mainPanel = new Panel();
        enterFileNumberButton.addActionListener(new enterFileNumberActionListener());
        mainPanel.add(textArea);
        mainPanel.add(chooseFileTextField);
        mainPanel.add(enterFileNumberButton);
        frame.getContentPane().add(mainPanel);
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Файл");
        JMenu testMenu = new JMenu("Тесты");
        JMenuItem selectDirItem = new JMenuItem("Выбрать директорию");
        JMenuItem generateBookItem  = new JMenuItem("Сгенерировать книгу");
        JMenuItem readTxtItem  = new JMenuItem("Прочитать книгу txt");
        selectDirItem.addActionListener(new selectDirItemActionListener());
        generateBookItem.addActionListener(new generateBookActionListener());
        readTxtItem.addActionListener(new readTxtActionListener());
        mainMenu.add(selectDirItem);
        testMenu.add(generateBookItem);
        testMenu.add(readTxtItem);
        menuBar.add(mainMenu);
        menuBar.add(testMenu);
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
            if (fileChooser.getSelectedFile()!=null) fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
            else {
                fileChooser.setCurrentDirectory(new File("./"));
            }
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileArrayList.clear();
                textArea.setText("");
                fileListCounter = 1;
                scanDirectory(fileChooser.getSelectedFile());
            }
        }
    }

    private void scanDirectory(File selectedFile) {
        final String[] okFileExtensions = new String[] {"docx", "doc", "html","txt"};
        try {
            for (File file : selectedFile.listFiles()) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {
                    for (String extension : okFileExtensions) {
                        if (file.getName().toLowerCase().endsWith(extension)) {
                            textArea.append(fileListCounter + ") " + file.getAbsolutePath() + "\n");
                            fileArrayList.add(file);
                            fileListCounter++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private class enterFileNumberActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int enteredFileNumber = Integer.parseInt(chooseFileTextField.getText());
            if (enteredFileNumber > 0 && enteredFileNumber<fileArrayList.size()) {
                Reader.checkAndRead(fileArrayList.get(enteredFileNumber - 1));
            }
        }
    }

    private class generateBookActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BookGenerator.generateBook();
        }
    }

    private class readTxtActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Reader.checkAndRead(new File("RandomBook.txt"));
        }
    }
}
