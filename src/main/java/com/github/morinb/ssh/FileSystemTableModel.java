package com.github.morinb.ssh;

import com.github.morinb.Icons;
import com.google.common.collect.Lists;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FileSystemTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"", "File Name", "Rights", "Size", "Creation Time", "Last Modified"};
    private static final Class<?>[] COLUMN_CLASSES = {Icon.class, String.class, String.class, Long.class, String.class, String.class};

    List<VirtualFile> entries = Lists.newLinkedList();

    public int getRowCount() {
        return entries.size();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    public void addEntry(VirtualFile virtualFile) {
        entries.add(virtualFile);
        fireTableDataChanged();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        final VirtualFile entry = getRowAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return entry.isDirectory() ? Icons.DIR : Icons.FILE;
            case 1:
                return entry.getName();
            case 2:
                return entry.getPermissions();
            case 3:
                return entry.getSize();
            case 4:
                return entry.getModificationTime();
            case 5:
                return entry.getCreationTime();
        }
        return null;
    }

    public VirtualFile getRowAt(int rowIndex) {
        return entries.get(rowIndex);
    }

    public void clear() {
        entries.clear();
        fireTableDataChanged();
    }
}
