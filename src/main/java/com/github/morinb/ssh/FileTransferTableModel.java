package com.github.morinb.ssh;

import com.github.morinb.Icons;
import com.google.common.collect.Lists;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;

/**
 * Created by Baptiste on 25/04/2016.
 */
public class FileTransferTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Way", "Filename", "Size", "Progress", "Status"};
    private static final Class<?>[] COLUMN_CLASSES = {Icon.class, String.class, Double.class, Integer.class, String.class};

    private LinkedList<FileTransferEntry> entries = Lists.newLinkedList();

    public int getRowCount() {
        return entries.size();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public void clear() {
        entries.clear();
        fireTableDataChanged();
    }

    public void addEntry(FileTransferEntry entry) {
        entries.add(entry);
        fireTableDataChanged();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        FileTransferEntry entry = getRowAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return Way.DOWNLOAD == entry.getWay() ? Icons.ARROW_DOWN : Icons.ARROW_UP;
            case 1:
                return entry.filename;
            case 2:
                return entry.fileSize;
            case 3:
                return entry.progress;
            case 4:
                return entry.status;
        }


        return null;
    }

    public FileTransferEntry getRowAt(int rowIndex) {
        return entries.get(rowIndex);
    }

    public static class FileTransferEntry {
        private Way way;
        private String filename;
        private Double fileSize;
        private Integer progress;
        private String status;

        public FileTransferEntry(Way way, String filename) {
            this.way = way;
            this.filename = filename;
        }

        public Way getWay() {
            return way;
        }

        public void setWay(Way way) {
            this.way = way;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public Double getFileSize() {
            return fileSize;
        }

        public void setFileSize(Double fileSize) {
            this.fileSize = fileSize;
        }

        public Integer getProgress() {
            return progress;
        }

        public void setProgress(Integer progress) {
            this.progress = progress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
