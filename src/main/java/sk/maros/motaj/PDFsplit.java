package sk.maros.motaj;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFsplit {
    private JButton buttonOpenFile;
    private JPanel mainPanel;
    private JButton buttonSplitPdf;
    private JButton buttonSaveFiles;
    private JTextField textFieldOpenFile;
    private JTextField textFieldSaveTo;
    private String pathToSourceFile = "";
    private String pathToSaveFiles = "";

    public PDFsplit() {
        //Button open file
        buttonOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create a file chooser
                final JFileChooser fileChooser = new JFileChooser();
                //Filter only pdf files can be selected
                fileChooser.setFileFilter(new FileNameExtensionFilter(".pdf", "pdf"));
                //Disable choose all type files
                fileChooser.setAcceptAllFileFilterUsed(false);

                //In response to a button click:
                int returnVal = fileChooser.showOpenDialog(fileChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    //Path to source (selected) file.
                    pathToSourceFile = file.getAbsoluteFile().toString();
                    //Set text to open file text field
                    textFieldOpenFile.setText(pathToSourceFile);
                }
            }
        });
        //Button split PDF file
        buttonSplitPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Splitter splitter = new Splitter();
                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    PDFTextStripper textStripper = new PDFTextStripper();

                    stripper.setSortByPosition(true);

                    File file = new File(pathToSourceFile);
                    // Load pdf file
                    PDDocument document = PDDocument.load(file);

                    // Split the pages of a PDF document
                    List<PDDocument> Pages = splitter.split(document);

                    // Saving pdf splits by number(default) or by text from pdf file
                    int i = 0;
                    for (PDDocument pdf : Pages) {
                        //Get text from pdf
                        //String pdfText = textStripper.getText(pdf);
                        // Split by whitespace
                        //String[] linesInPdf = pdfText.split("\\r?\\n");

                        //Save pdf
                        pdf.save(pathToSaveFiles +i+".pdf");
                        System.out.println("Saved in "+pathToSaveFiles+i+".pdf");
                        i ++;
                    }
                    JOptionPane.showMessageDialog(mainPanel, "The pdf file was successfully splitted");
                    document.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "No pdf file was selected");
                    ex.printStackTrace();
                }
            }
        });
        //Button save file
        buttonSaveFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create a file chooser
                final JFileChooser fileChooser = new JFileChooser();
                //Filter, only pdf files
                fileChooser.setFileFilter(new FileNameExtensionFilter(".pdf", "pdf"));
                //Disable all type files
                fileChooser.setAcceptAllFileFilterUsed(false);
                //Only directory can be selected to save the splited files
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnVal = fileChooser.showSaveDialog(fileChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    //Path to save directory
                    pathToSaveFiles = file.getAbsoluteFile()+"/";
                    textFieldSaveTo.setText(pathToSaveFiles);
                }
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Split PDF file.");
        frame.setContentPane(new PDFsplit().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
