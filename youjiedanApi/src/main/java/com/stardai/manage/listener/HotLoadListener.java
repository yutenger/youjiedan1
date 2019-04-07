package com.stardai.manage.listener;


import com.stardai.manage.classloader.HotLoadClassLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * 热更新监听器
 *
 * @author yax
 * @create 2019-01-15 16:11
 **/
//@WebListener
public class HotLoadListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new Thread(() -> {
            try {
                // 说明，这里的监听也必须是目录
                Path path = Paths.get(this.getClass().getResource("/").getPath().substring(1)+"com/stardai/manage/hotLoadClass/");
                WatchService   watcher = FileSystems.getDefault().newWatchService();
                //path.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
                path.register(watcher,ENTRY_MODIFY);
                while (true) {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        //System.out.println(event.context() + " comes to " + event.kind());
                        if (event.kind() == ENTRY_MODIFY) {
                            //事件可能lost or discarded
                            HotLoadClassLoader.updateClassLoader();
                            continue;
                        }
                    }
                    if (!key.reset()) { // 重设WatchKey
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
