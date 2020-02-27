package com.happy3w.utils.downlod.httpdownload;

import com.happy3w.utils.downlod.httpdownload.mode.DownloadTask;
import com.happy3w.utils.downlod.httpdownload.ref.BaseReadonlyTableModel;
import com.happy3w.utils.downlod.httpdownload.ref.ExtTable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Service
public class HttpDownloadFrame extends JFrame {
    private JTextField txtUrl;
    private JTextField txtDir;
    private ExtTable tblInfo;
    private BaseReadonlyTableModel<DownloadTask> tblModelInfo;

    public HttpDownloadFrame() {
        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void postAction() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HttpDownloadFrame frame = HttpDownloadFrame.this;

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        System.exit(0);
                    }
                });
                frame.setVisible(true);
            }
        });
    }

    private void initUI() {
        this.setSize(900, 700);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(createMainPanel());
        this.setTitle("Happy3w Downloader");
    }

    private JPanel createMainPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridBagLayout());

        int row = 0;

        pnl.add(createNewDownloadPanel(), new GridBagConstraints(0, row++, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(8, 8, 0, 8), 0, 0));
        pnl.add(createDownloadListPanel(), new GridBagConstraints(0, row++, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(8, 8, 0, 8), 0, 0));
        pnl.add(createBtnDelete(), new GridBagConstraints(0, row++, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
        return pnl;
    }

    private JButton createBtnDelete() {
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doDelete();
            }
        });
        return btnDelete;
    }

    private void doDelete() {
    }

    private ExtTable createDownloadListPanel() {
        tblModelInfo = new BaseReadonlyTableModel<DownloadTask>(new String[]{"Name", "URL", "Total Size", "Download Size", "Percentage"}) {
            @Override
            public Object[] formatItem(DownloadTask task) {
                return new Object[]{
                        task.getFileName(),
                        task.getUrl(),
                        task.getTotalSize(),
                        task.getDownloadSize(),
                        task.getDownloadSize() / (double) task.getTotalSize()
                };
            }
        };
        tblInfo = new ExtTable();
        tblInfo.setModel(tblModelInfo);

        return tblInfo;
    }

    private JPanel createNewDownloadPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridBagLayout());

        int row = 0;
        pnl.add(new JLabel("URL"), new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnl.add(getTxtUrl(), new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        row++;
        pnl.add(new JLabel("Dir"), new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnl.add(getTxtDir(), new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnl.add(getBtnCreate(), new GridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        return pnl;
    }

    private JButton getBtnCreate() {
        JButton btnDownload = new JButton("Download");
        btnDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doDownload();
            }
        });
        return btnDownload;
    }

    private void doDownload() {

    }

    private JTextField getTxtDir() {
        txtDir = new JTextField();
        return txtDir;
    }

    private JTextField getTxtUrl() {
        txtUrl = new JTextField();
        return txtUrl;
    }
}
