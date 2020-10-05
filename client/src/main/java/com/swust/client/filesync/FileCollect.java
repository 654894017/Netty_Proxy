package com.swust.client.filesync;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import sun.security.provider.MD5;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hz20035009-逍遥
 * date   2020/9/30 15:59
 */
public class FileCollect {
    public static void main(String[] args) throws Exception {
        new FileCollect().collect();
    }

    public void collect() throws Exception {
        File directory = new File("E:/logs");
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(5);
        // 创建一个文件观察器用于处理文件的格式
        FileAlterationObserver observer = new FileAlterationObserver(directory);
//        FileAlterationObserver observer = new FileAlterationObserver(directory,
//                FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt")));
        //设置文件变化监听器
        observer.addListener(new FileAlterationListener() {
            @Override
            public void onStart(FileAlterationObserver fileAlterationObserver) {
            }

            @Override
            public void onDirectoryCreate(File file) {
                System.out.println("onDirectoryCreate");
            }

            @Override
            public void onDirectoryChange(File file) {
                System.out.println("onDirectoryChange");
            }

            @Override
            public void onDirectoryDelete(File file) {
                System.out.println("onDirectoryDelete");
            }

            @Override
            public void onFileCreate(File file) {
                System.out.println("onFileCreate");
            }

            @Override
            public void onFileChange(File file) {
                System.out.println("onFileChange");
            }

            @Override
            public void onFileDelete(File file) {
                System.out.println("onFileDelete");
            }

            @Override
            public void onStop(FileAlterationObserver fileAlterationObserver) {
            }
        });
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        monitor.start();


        LockSupport.park();
        MD5 md5 = new MD5();

    }
}