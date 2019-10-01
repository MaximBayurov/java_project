package com.conceptualGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;


/**
 * Hello world!
 *
 */
public class App 
{
    JFrame frame;
    JMenuBar menuBar;
    final JFileChooser fileChooser= new JFileChooser();

    public static void main( String[] args )
    {
        new App().start();
    }

    private void start() {
        frame= new JFrame("Концептуальный граф");

        menuBar= new JMenuBar();
        JMenu mainMenu= new JMenu("Файл");
        JMenuItem selectDirItem= new JMenuItem("Выбрать директорию");

        selectDirItem.addActionListener(new selectDirItemActionListener());

        mainMenu.add(selectDirItem);

        menuBar.add(mainMenu);

        frame.setJMenuBar(menuBar);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Simple text documents", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft documents", "doc"));

        fileChooser.setAcceptAllFileFilterUsed(true);

        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class selectDirItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int result = fileChooser.showOpenDialog(frame);

            if (result== JFileChooser.APPROVE_OPTION){
                for (File file:fileChooser.getSelectedFile().listFiles(new MyFileFilter())){
                    System.out.println(file.getName());
                };
            }

        }
    }

    private class MyFileFilter implements FileFilter {
        private final String[] okFileExtensions = new String[] {"docx", "doc", "html","txt"};

        @Override
        public boolean accept(File f) {
            for (String extension : okFileExtensions)
            {
                if (f.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
