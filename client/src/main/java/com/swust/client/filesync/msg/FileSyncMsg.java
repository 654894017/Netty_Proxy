package com.swust.client.filesync.msg;

import java.util.List;

/**
 * @author hz20035009-逍遥
 * date   2020/9/30 17:42
 */
public class FileSyncMsg {
    //count=4 n=3 rem=1    这表示划分了4个数据块，数据块大小为3字节，剩余1字节给了最后一个数据块
    private int blockSum;
    private int blockByte;
    private int lastBlockByte;

    //滚动校验和（弱） 和 强校验和 一一对应  总数应等于总数据块数量
    private List<String> rollingChecksum;
    private List<String> strongChecksum;

    //需要传输的数据块索引和具体内容
    private int idx;
    private byte[] data;
}