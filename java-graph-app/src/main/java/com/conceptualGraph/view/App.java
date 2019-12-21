package com.conceptualGraph.view;

import com.conceptualGraph.BookGenerator;
import com.conceptualGraph.controller.DictOptimizer;
import com.conceptualGraph.controller.PreChecker;
import com.conceptualGraph.model.Reader;
import org.apache.poi.hpsf.Property;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class App {
    private final JFileChooser dirChooser = new JFileChooser(); //позволяет выбирать директории
    private final JFileChooser fileChooser = new JFileChooser(); //позволяет выбирать файл
    private int fileListCounter = 1;
    private JFrame frame;
    private ArrayList<File> fileArrayList = new ArrayList<>();
    private TextArea textArea;
    private TextField chooseFileTextField;

    /**
     * Конструктор устанавливающий свойства,здесь нужно устанавливать значения полученных полей из полученных свойств
     *
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
        JMenuItem selectFileItem = new JMenuItem("Выбрать файл");
        JMenuItem generateBookItem = new JMenuItem("Сгенерировать книгу");
        JMenuItem readTxtItem = new JMenuItem("Прочитать книгу txt");
        JMenuItem wikiTestItem = new JMenuItem("Взять страницу");
        JMenuItem MyTestItem = new JMenuItem("Тест словаря");
        JMenuItem NewTestItem = new JMenuItem("Новый тест");
        selectDirItem.addActionListener(new selectDirItemActionListener());
        selectFileItem.addActionListener(new selectFileItemActionListener());
        generateBookItem.addActionListener(new generateBookActionListener());
        readTxtItem.addActionListener(new readTxtActionListener());
        wikiTestItem.addActionListener(new wikiTestItemActionListener());
        MyTestItem.addActionListener(new MyTestItemActionListener());
        NewTestItem.addActionListener(new NewActionListener());
        mainMenu.add(selectDirItem);
        mainMenu.add(selectFileItem);
        testMenu.add(generateBookItem);
        testMenu.add(readTxtItem);
        testMenu.add(wikiTestItem);
        testMenu.add(MyTestItem);
        testMenu.add(NewTestItem);
        menuBar.add(mainMenu);
        menuBar.add(testMenu);
        frame.setJMenuBar(menuBar);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        setFileChooserFilters(dirChooser);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setFileChooserFilters(JFileChooser fileChooser) {
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Simple text documents", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft documents", "doc"));
        fileChooser.setAcceptAllFileFilterUsed(true);
    }

    private void scanDirectory(File selectedFile) {
        final String[] okFileExtensions = new String[]{"docx", "doc", "html", "txt"};
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

    private class selectDirItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dirChooser.getSelectedFile() != null) dirChooser.setCurrentDirectory(dirChooser.getSelectedFile());
            else {
                dirChooser.setCurrentDirectory(new File("./"));
            }
            int result = dirChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileArrayList.clear();
                textArea.setText("");
                fileListCounter = 1;
                scanDirectory(dirChooser.getSelectedFile());
            }
        }
    }

    private class enterFileNumberActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int enteredFileNumber = Integer.parseInt(chooseFileTextField.getText());
            if (enteredFileNumber > 0 && enteredFileNumber <= fileArrayList.size()) {
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

    private class selectFileItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)  {
            if (fileChooser.getSelectedFile() != null) fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
            else {
                fileChooser.setCurrentDirectory(new File("./"));
            }
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                Reader.checkAndRead(fileChooser.getSelectedFile());
            }
        }
    }

    private class wikiTestItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DictOptimizer dictOptimizer = new DictOptimizer();
            dictOptimizer.makeUpMap("https://ru.wikipedia.org/wiki/Робоцып");


        }
    }

    private class MyTestItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DictOptimizer dictOptimizer = new DictOptimizer();
            try {
                ArrayList<Integer> ids = dictOptimizer.getIDs(
                        "https://ru.wikipedia.org/w/api.php?" +
                                "action=query&list=random&rnlimit=100&rnnamespace=0&format=json"
                );
                for (Integer id : ids) {
                    dictOptimizer.makeUpMap("https://ru.wikipedia.org/?curid=" + id);
                }
                dictOptimizer.sortMap();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private class NewActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PreChecker.readDicts();
            String sentence = "география каналы сбыта и твоей дистрибуции";
            String[] words = sentence.split(" ");
            Boolean[] booleans = PreChecker.arrayCheck(words);
            for (int i=0; i<words.length; i++) {
                System.out.println(booleans[i] + " " + words[i]);
            }
        }
    }
}
