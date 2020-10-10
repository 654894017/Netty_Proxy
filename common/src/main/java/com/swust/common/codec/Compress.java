package com.swust.common.codec;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author hz20035009-逍遥
 * date   2020/10/10 9:22
 */
@Slf4j
public class Compress {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String source = "所有的人都有属于自己的优点，就算是一个缺点再多，总是遭人摒弃的人，他也一定有优点。鲁迅笔下的孔乙己，在那个时代他仿佛被社会所遗忘，人人都唾弃他，可他却是个有知识的人。\n" +
                "\n" +
                "常常听到有同学说“自己什么都不会，考试了怎么办啊？”之类的话，要记着，上帝给你关闭所有的门之后，还会给你打开一扇窗，他总会让你成功的，可前提是，你要发现自己的优势所在。\n" +
                "\n" +
                "郭沫若是我国著名的诗人，他上中学时算不上是优等生。他中学二年级的成绩理科好，而文科不行，到了三年级他成绩也是一般。不过，郭沫若最终也没有成为数学家或医学家，却作文https://Www.ZuoWEn8.Com/匪夷所思的成了大诗人，大书法家。仔细想想，如果当初他的父母培养他学理，而不是学他喜欢的文，他能成为我们心目中的伟人？所以，人的发展不能只看表面现象，要选择适宜自己成长发展的道路，因地制宜，发挥长处。";
        System.out.println("源数据大小:" + source.getBytes("utf-8").length);
        byte[] zip = zip(source.getBytes("utf-8"));
        System.out.println("压缩之后的数据大小:" + zip.length);
        System.out.println("解压之后的数据:" + new String(unzip(zip),"utf-8"));

    }

    /**
     * @param source 被压缩的源数据
     * @return 压缩之后的数据
     * 压缩数据，若异常则不进行数据压缩，原样返回
     */
    public static byte[] zip(byte[] source) {
        if (Objects.isNull(source) || source.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(source);
                gzip.close();
                return out.toByteArray();
            }
        } catch (IOException e) {
            log.error("failed to compress data, do not deal", e);
        }
        return null;
    }

    /**
     * @param zipData 压缩数据
     * @return 解压之后的数据
     */
    public static byte[] unzip(byte[] zipData) {
        if (zipData == null || zipData.length == 0) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData)) {
                GZIPInputStream inputStream = new GZIPInputStream(in);
                byte[] buffer = new byte[256];
                int n;
                while ((n = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                return out.toByteArray();
            }
        } catch (IOException e) {
            log.error("unzip failed", e);
        }
        return null;
    }
}