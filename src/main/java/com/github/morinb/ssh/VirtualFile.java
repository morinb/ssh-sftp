package com.github.morinb.ssh;

public interface VirtualFile {

    boolean isDirectory();

    String getName();

    String getAbsolutePath();

    long getSize();

    String getPermissions();

    String getCreationTime();

    String getModificationTime();
}
