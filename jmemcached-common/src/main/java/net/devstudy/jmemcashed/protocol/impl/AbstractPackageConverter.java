package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import net.devstudy.jmemcashed.protocol.model.Version;

public abstract class AbstractPackageConverter {

    protected void checkProtocolVersion(byte versionByte) {
        Version version = Version.valueOf(versionByte);
        if (version != Version.VERSION_1_0) {
            throw new JMemcashedException(String.format("Unsupported protocol version: %s", version));
        }
    }

    protected byte getVersionByte() {
        return Version.VERSION_1_0.getByteCode();
    }
}
