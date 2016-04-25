package com.github.morinb.ssh.impl;

import com.github.morinb.ssh.VirtualFile;
import com.jcraft.jsch.ChannelSftp;

public class DistantVirtualFile implements VirtualFile {

    ChannelSftp.LsEntry delegate;
    private String path;

    public DistantVirtualFile(ChannelSftp.LsEntry delegate, String path) {
        this.delegate = delegate;
        this.path = path;
    }

    @Override
    public boolean isDirectory() {
        return delegate.getAttrs().isDir();
    }

    @Override
    public String getName() {
        return delegate.getFilename();
    }

    @Override
    public String getAbsolutePath() {
        return path + "/" + getName();
    }

    @Override
    public long getSize() {
        return delegate.getAttrs().getSize();
    }

    @Override
    public String getPermissions() {
        return delegate.getAttrs().getPermissionsString();
    }

    @Override
    public String getCreationTime() {
        return delegate.getAttrs().getAtimeString();
    }

    @Override
    public String getModificationTime() {
        return delegate.getAttrs().getMtimeString();
    }
}
