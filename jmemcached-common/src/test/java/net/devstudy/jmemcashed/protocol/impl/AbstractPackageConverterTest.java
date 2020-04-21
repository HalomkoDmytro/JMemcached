package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AbstractPackageConverterTest {

    private AbstractPackageConverter abstractPackageConverter = new AbstractPackageConverter() {
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void checkProtocolVersionSuccess() {
        try {
            abstractPackageConverter.checkProtocolVersion((byte) 16);
        } catch (Exception e) {
            fail("Support protocol version should be 1.0");
        }
    }

    @Test
    public void checkProtocolVersionFailed() {
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Unsupported protocol version: 0.0"));
        abstractPackageConverter.checkProtocolVersion((byte) 0);
    }

    @Test
    public void getVersionByteCode() {
        assertEquals(16, abstractPackageConverter.getVersionByte());
    }
}
