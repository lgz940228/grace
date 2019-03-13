package com.lgz.grace.api.utils.io;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
    public static byte[] readAll(InputStream is) throws IOException {
        if (is != null) {
            int len = Math.max(is.available(), 1024);
            FastOutputStream fos = new FastOutputStream(len, 1.5);
            int c;
            while ((c = is.read()) != -1) {
                fos.write(c);
            }
            return fos.getBytes();
        } else {
            return new byte[0];
        }
    }

   /* public static Res close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                return Res.errE("close error", e);
            }
        }
        return Res.suc();
    }*/
}
