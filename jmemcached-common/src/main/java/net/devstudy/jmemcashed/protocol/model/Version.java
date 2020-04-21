
package net.devstudy.jmemcashed.protocol.model;

import net.devstudy.jmemcashed.exception.JMemcashedException;

public enum Version {

    VERSION_0_0(0, 0),
    VERSION_1_0(1, 0);

    private byte high; // contain 3 bit high

    private byte low;

    Version(int high, int lov) {
        this.high = (byte) (high & 0x7); // remove all values higher 7
        this.low = (byte) (lov & 0xF);
    }

    public static Version valueOf(byte byteCode) {
        for (Version version : Version.values()) {
            if (version.getByteCode() == byteCode) {
                return version;
            }
        }

        throw new JMemcashedException("Unsupported byteCode for Version: " + byteCode);
    }

    public byte getByteCode() {
        return (byte) (low + (high << 4));
    }

    @Override
    public String toString() {
        return String.format("%s.%s", high, low);
    }
}
