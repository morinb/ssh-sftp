package com.github.morinb.ssh.impl;

import com.github.morinb.ssh.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Baptiste on 26/04/2016.
 */
public class LocalVirtualFile implements VirtualFile {
    private final File delegate;
    private final BasicFileAttributes attributes;

    public LocalVirtualFile(File file) {
        this.delegate = file;
        this.attributes = getAttributes(file);
    }

    private BasicFileAttributes getAttributes(File file) {
        BasicFileAttributes basicFileAttributes = null;
        try {
            basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException ignored) {
        }
        return basicFileAttributes;
    }

    public boolean isDirectory() {
        return delegate.isDirectory();
    }

    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getAbsolutePath() {
        return delegate.getAbsolutePath();
    }

    @Override
    public long getSize() {
        return delegate.length();
    }

    public String getPermissions() {
        return getPermissions(delegate);
    }

    private static final SimpleDateFormat locale = new SimpleDateFormat();

    public String getCreationTime() {
        return (locale.format(new Date(attributes.creationTime().toMillis())));
    }

    @Override
    public String getModificationTime() {
        return (locale.format(new Date(attributes.lastModifiedTime().toMillis())));
    }

    private String getPermissions(File file) {
        final char type = isDirectory() ? 'd' : '-';
        final char read = file.canRead() ? 'r' : '-';
        final char write = file.canWrite() ? 'w' : '-';
        final char exec = file.canExecute() ? 'x' : '-';
        return String.format("%c%c%c%c%c%c%c%c%c%c", type, read, write, exec, read, write, exec, read, write, exec);
    }
}
