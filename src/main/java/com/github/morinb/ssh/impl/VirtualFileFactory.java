package com.github.morinb.ssh.impl;

import com.github.morinb.ssh.VirtualFile;
import com.jcraft.jsch.ChannelSftp;

import java.io.File;

public class VirtualFileFactory {
    private static VirtualFileFactory INSTANCE = new VirtualFileFactory();

    private VirtualFileFactory() {
    }

    public static VirtualFileFactory instance() {
        return INSTANCE;
    }

    public VirtualFile create(ChannelSftp.LsEntry entry, String path) {
        return new DistantVirtualFile(entry, path);
    }

    public VirtualFile create(File file) {
        return new LocalVirtualFile(file);
    }
}
