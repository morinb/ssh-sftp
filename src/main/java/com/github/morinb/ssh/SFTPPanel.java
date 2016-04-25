package com.github.morinb.ssh;

import com.github.morinb.Icons;
import com.github.morinb.ssh.impl.VirtualFileFactory;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;

public class SFTPPanel extends JPanel {
    private final JTable tableLocal = new JTable();
    private final JTable tableRemote = new JTable();
    private final JTable tableDownload = new JTable();

    final JSplitPane splitPaneConnections;
    final JSplitPane splitPaneMain;
    private final Session session;
    private final ChannelSftp channelSftp;

    private static final VirtualFileFactory FACTORY = VirtualFileFactory.instance();
    private final FileSystemTableModel remoteModel = new FileSystemTableModel();
    private final FileSystemTableModel localModel = new FileSystemTableModel();
    private final FileTransferTableModel transferTableModel = new FileTransferTableModel();
    private final TableRowSorter<FileSystemTableModel> localSorter = new TableRowSorter<>(localModel);
    private final TableRowSorter<FileSystemTableModel> remoteSorter = new TableRowSorter<>(remoteModel);

    JPopupMenu remotePopup = new JPopupMenu();
    JPopupMenu localPopup = new JPopupMenu();

    public SFTPPanel(Session session, ChannelSftp channelSftp) {
        this.session = session;
        this.channelSftp = channelSftp;
        tableLocal.setModel(localModel);
        tableLocal.setShowGrid(false);
        localSorter.setSortKeys(Lists.newArrayList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        tableLocal.setRowSorter(localSorter);

        createMenus(localPopup, remotePopup);

        tableRemote.setModel(remoteModel);
        tableRemote.setShowGrid(false);
        remoteSorter.setSortKeys(Lists.newArrayList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        tableRemote.setRowSorter(remoteSorter);

        tableDownload.setModel(transferTableModel);

        installPopup(tableLocal, localPopup);
        installPopup(tableRemote, remotePopup);


        splitPaneConnections = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tableLocal), new JScrollPane(tableRemote));
        splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneConnections, new JScrollPane(tableDownload));

        splitPaneMain.setLeftComponent(splitPaneConnections);
        splitPaneMain.setRightComponent(new JScrollPane(tableDownload));

        this.setLayout(new BorderLayout());
        this.add(splitPaneMain, BorderLayout.CENTER);

        fixFirstColumnSize(tableLocal, 16);
        fixFirstColumnSize(tableRemote, 16);
        fixFirstColumnSize(tableDownload, 16);
    }

    private void installPopup(JTable table, JPopupMenu popup) {
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void createMenus(JPopupMenu localPopup, JPopupMenu remotePopup) {
        JMenuItem menuItemUpload = new JMenuItem("Upload", Icons.ARROW_UP);
        menuItemUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFile(Way.UPLOAD);
            }
        });
        localPopup.add(menuItemUpload);

        JMenuItem menuItemDownload = new JMenuItem("Download", Icons.ARROW_DOWN);
        menuItemDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFile(Way.DOWNLOAD);
            }
        });
        remotePopup.add(menuItemDownload);
    }

    private void transferFile(Way way) {
        JTable table = way == Way.UPLOAD ? tableLocal : tableRemote;
        int i = table.convertRowIndexToModel(table.getSelectedRow());
        FileSystemTableModel model = (FileSystemTableModel) table.getModel();
        VirtualFile virtualFile = model.getRowAt(i);
        FileTransferTableModel.FileTransferEntry entry = new FileTransferTableModel.FileTransferEntry(way, virtualFile.getAbsolutePath());

        transferTableModel.addEntry(entry);

    }

    private void fixFirstColumnSize(JTable table, int size) {
        table.getColumnModel().getColumn(0).setWidth(size);
        table.getColumnModel().getColumn(0).setMaxWidth(size);
        table.getColumnModel().getColumn(0).setMinWidth(size);
    }

    public void refreshDividersLocations() {
        Dimension size = getSize();
        splitPaneMain.setDividerLocation((int) (size.getHeight() * 0.85));
        splitPaneConnections.setDividerLocation((int) (size.getWidth() * 0.5));
    }

    public void setLocalPath(String path) throws SftpException {
        File root = new File(path);
        if (root.exists()) {
            channelSftp.lcd(path);
            final File[] files = root.listFiles();
            if (files != null) {

                localModel.clear();
                for (File file : files) {
                    localModel.addEntry(FACTORY.create(file));
                }
            }
        }
    }


    public void setRemotePath(String path) throws SftpException {
        channelSftp.cd(path);
        String pwd = channelSftp.pwd();
        Vector<ChannelSftp.LsEntry> entries = (Vector<ChannelSftp.LsEntry>) channelSftp.ls(".");

        for (ChannelSftp.LsEntry entry : entries) {
            remoteModel.addEntry(FACTORY.create(entry, pwd));
        }
    }

}
