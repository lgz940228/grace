package com.lgz.grace.api.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class FastOutputStream extends OutputStream {
    public static final int GROW_LEN = 0;//定量自增方式
    public static final int GROW_RATIO = 1;//定比例自增方式
    private int growLen = 2048;//初始大小
    private float growRatio = 1.5f;//定比例自增
    private int size = 0;//当前流中数据长度
    private int pos = 0;//内部临时buffer偏移量
    private int growType = 0;//增长方式
    private LinkedList<byte[]> data;//存储数据区
    private byte[] buffer;//当前缓存区

    /**
     * 得到所有写入流中的数据
     *
     * @return
     */
    public byte[] getBytes() {
        byte[] result = new byte[size];
        int idx = 0;
        if (data != null) {
            for (byte[] temp : data) {
                System.arraycopy(temp, 0, result, idx, temp.length);
                idx += temp.length;
            }
        }
        if (pos > 0) {
            System.arraycopy(buffer, 0, result, idx, pos);
        }

        return result;
    }

    @Override
    public void write(int b) throws IOException {
        doWrite(b);
    }

    public void doWrite(int b) {
        if (buffer.length <= pos) {
            toGrow(1);
        }
        buffer[pos++] = (byte) b;
        size++;
    }

    public void doWrite(byte b[]) {
        doWrite(b, 0, b.length);
    }

    public void doWrite(byte b[], int off, int len) {
        final int couldWriteLen = Math.min(buffer.length - pos, len);
        final int nextLen = len - couldWriteLen;
        System.arraycopy(b, off, buffer, pos, couldWriteLen);
        pos += couldWriteLen;
        size += couldWriteLen;
        if (nextLen > 0) {
            toGrow(nextLen);
        }
        System.arraycopy(b, off + couldWriteLen, buffer, pos, nextLen);
        pos += nextLen;
        size += nextLen;
    }

    @Override
    public void write(byte b[], int off, int len) {
        doWrite(b, off, len);
    }

    /**
     * 需要增长至少len长度
     *
     * @param len
     */
    private void toGrow(int len) {
        int newLen;
        if (growType == GROW_LEN) {
            newLen = growLen;
        } else {
            newLen = (int) ((size * growRatio) - size);
        }
        newLen = Math.max((int) (size * 0.2), Math.max(len, newLen));

        if (data == null) {
            data = new LinkedList<byte[]>();
        }
        data.add(buffer);
        buffer = new byte[newLen];
        pos = 0;
    }

    public FastOutputStream() {
        this(2048);
    }

    public FastOutputStream(int growLen) {
        this.growLen = growLen;
        this.growType = GROW_LEN;
        this.buffer = new byte[growLen];
    }

    public FastOutputStream(int initLen, float growRatio) {
        this.growLen = initLen;
        this.growRatio = growRatio;
        this.growType = GROW_RATIO;
        this.buffer = new byte[initLen];
    }

    public FastOutputStream(int initLen, double growRatio) {
        this(initLen, (float) growRatio);
    }


    public int getSize() {
        return size;
    }

    public int getGrowLen() {
        return growLen;
    }

    public void setGrowLen(int growLen) {
        this.growLen = growLen;
        this.growType = GROW_LEN;
    }

    public float getGrowRatio() {
        return growRatio;
    }

    public void setGrowRatio(float growRatio) {
        this.growRatio = growRatio;
        this.growType = GROW_RATIO;
    }

    public int getGrowType() {
        return growType;
    }

    public void setGrowType(int growType) {
        this.growType = growType;
    }
}
