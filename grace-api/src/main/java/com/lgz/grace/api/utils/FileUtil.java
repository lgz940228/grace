package com.lgz.grace.api.utils;

import java.io.*;
import java.util.*;

public class FileUtil {
    public static String readFile(String path, String code, int skipLen, int maxLen) {
        RandomAccessFile acc = null;
        try {
            if (maxLen < 1) {
                return "";
            }
            if (code == null) {
                code = "UTF-8";
            }
            acc = new RandomAccessFile(path, "r");
            long fileSize = acc.length();
            int readSize = 0;
            if (fileSize < skipLen) {
                return "";
            }
            int size = (int) Math.min(maxLen, fileSize - skipLen);
            byte[] bytes = new byte[size];
            if (skipLen > 0) {
                acc.skipBytes(skipLen);
            }
            readSize = acc.read(bytes, 0, size);
            return new String(bytes, 0, readSize, code);
        } catch (Exception e) {
           // ExceptionUtil.throwRunTime(e);
        } finally {
            if (acc != null) {
                try {
                    acc.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static List getChildFile(String path, Boolean type) {
        File file = new File(path);
        File[] files = file.listFiles();
        List models = new LinkedList();
        if (files != null) {
            List<File> list = new ArrayList<File>(Arrays.asList(files));
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1 != null && o2 != null) {
                        return o1.getName().compareTo(o2.getName());
                    }
                    return -1;
                }
            });
            for (File temp : files) {
                String name = temp.getName();
                if (Boolean.TRUE.equals(type)) {
                    String s = (temp.isFile() ? "file" : "dir");
                    long len = (temp.isFile() ? temp.length() : -1);
                    name = temp.getName() + " _ " + s + " _ " + len + " _ "
                            + TimeUtil.formatAllTime(new Date(temp.lastModified()));
                }
                models.add(name);
            }
        }
        return models;
    }

    public static Object getContent(String path) throws RuntimeException {
        return getContent(path, null, null, 0, Integer.MAX_VALUE);
    }

    public static Object getContent(String path, String code, Boolean type, int skipLen, int maxLen) throws RuntimeException {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("file is not exits,path = " + path);
        }
        if (file.isDirectory()) {
            return getChildFile(path, type);
        } else {
            return readFile(path, code, skipLen, maxLen);
        }
    }

    public static boolean writeFileBytes(String path, byte[] bytes) {
        File file = new File(path);
        FileOutputStream fileWriter = null;
        try {
            fileWriter = new FileOutputStream(file);
            fileWriter.write(bytes);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public static byte[] readFileBytes(String path) {
        FileInputStream fis = null;
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(path);
            baos = new ByteArrayOutputStream(fis.available());
            ;
            int size = 0;
            byte[] c = new byte[1024];
            while ((size = fis.read(c)) > 0) {
                baos.write(c, 0, size);
            }
            bytes = baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
        return bytes;
    }

    public static String readFile(String path, String code) throws RuntimeException {
        try {
            return readStr(new FileInputStream(path), code);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("path=" + path, e);
        }
    }

    public static String readStr(InputStream is, String code) {
        InputStreamReader isr = null;
        StringBuilder builder;
        try {
            builder = new StringBuilder(Math.max(is.available(), 20480));
            int size = 0;
            char[] c = new char[1024];
            isr = new InputStreamReader(is, code == null ? "UTF-8" : code);
            while ((size = isr.read(c)) > 0) {
                builder.append(c, 0, size);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();
    }

    public static String readFile(String path) throws RuntimeException {
        return readFile(path, null);
    }

    public static void writeFile(String path, String content) throws RuntimeException {
        File file = new File(path);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class FileModel {
        private String name;
        private long size;
        private String time;
        private String type;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
