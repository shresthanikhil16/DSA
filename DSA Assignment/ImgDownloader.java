import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class ImgDownloader extends JFrame {
    private JTextField urlTextField;
    private JButton downloadButton, pauseButton, resumeButton, cancelButton;
    private JProgressBar progressBar;
    private ExecutorService executorService;
    private boolean pauseDownload = false;
    private boolean cancelDownload = false;

    public ImgDownloader() {
        initComponents();
        executorService = Executors.newFixedThreadPool(5);
        getContentPane().setBackground(new Color(153,204,255));
    }

    private void initComponents() {
        setTitle("Image Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1)); // 3 rows: 1 for URL, 1 for buttons, 1 for progress bar
        

        // Row 1: URL label and input field
        JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        urlPanel.setBackground(new Color(153,204,255));
        urlPanel.add(new JLabel("URL:"));
        urlTextField = new JTextField(30);
        urlPanel.add(urlTextField);
        add(urlPanel);

        // Row 2: Buttons (Download, Pause, Resume, Cancel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(153,204,255));
        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startDownload(urlTextField.getText());
            }
        });
        buttonPanel.add(downloadButton);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseDownload();
            }
        });
        buttonPanel.add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumeDownload();
            }
        });
        buttonPanel.add(resumeButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelDownload();
            }
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel);

        // Row 3: Progress bar
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setBackground(new Color(153,204,255));
        add(progressBar);

        setPreferredSize(new Dimension(400, 200));

        pack();
        setLocationRelativeTo(null);
    }

    private void startDownload(String imageUrl) {
        if (!imageUrl.isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                final String filePath = fileChooser.getSelectedFile().getPath(); // Declare filePath as final
                String fileExtension = getFileExtension(imageUrl); // Get the file extension from the URL
    
                StringBuilder filePathBuilder = new StringBuilder(filePath); // Use StringBuilder to construct the file path
                filePathBuilder.append(fileExtension); // Append the file extension to the file path
    
                executorService.execute(new Runnable() {
                    public void run() {
                        try {
                            URL url = new URL(imageUrl);
                            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                                 BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePathBuilder.toString()))) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                long totalBytesRead = 0;
                                long fileSize = url.openConnection().getContentLength();
    
                                while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                                    if (cancelDownload) {
                                        SwingUtilities.invokeLater(() -> {
                                            JOptionPane.showMessageDialog(ImgDownloader.this, "Download canceled.");
                                            progressBar.setValue(0);
                                        });
                                        return;
                                    }
    
                                    if (pauseDownload) {
                                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ImgDownloader.this, "Download paused."));
                                        while (pauseDownload) {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
    
                                    out.write(buffer, 0, bytesRead);
                                    totalBytesRead += bytesRead;
    
                                    int percentage = (int) ((totalBytesRead * 100) / fileSize);
                                    SwingUtilities.invokeLater(() -> progressBar.setValue(percentage));
    
                                    // Introduce a delay to slow down the download
                                    try {
                                        Thread.sleep(100); // Adjust this value to control the download speed
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
    
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(ImgDownloader.this, "Image downloaded successfully.");
                                    progressBar.setValue(0);
                                });
                            }
                        } catch (IOException e) {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ImgDownloader.this, "Error downloading image: " + e.getMessage()));
                        }
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid URL.");
        }
    }
    
    

    private String getFileExtension(String url) {
        String extension = "";
        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = url.substring(lastDotIndex);
        }
        return extension;
    }

    private void pauseDownload() {
        pauseDownload = true;
    }

    private void resumeDownload() {
        pauseDownload = false;
    }

    private void cancelDownload() {
        cancelDownload = true;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new ImgDownloader().setVisible(true));
    }
}