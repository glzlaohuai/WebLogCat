package com.imob.weblogcat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamReader {

    public static byte[] readBytesFromInputStream(InputStream inputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];

        try {
            for (int i = inputStream.read(bytes); i != -1; i = inputStream.read(bytes)) {
                bos.write(bytes, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        bytes = bos.toByteArray();

        try {
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }


}
