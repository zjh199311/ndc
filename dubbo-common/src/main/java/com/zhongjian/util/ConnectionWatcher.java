package com.zhongjian.util;  
  
  
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.WatchedEvent;  
import org.apache.zookeeper.Watcher;  
import org.apache.zookeeper.ZooKeeper;  
import org.apache.zookeeper.Watcher.Event.KeeperState;  
  
  
public class ConnectionWatcher implements Watcher {  
  
    private static final int SESSION_TIMEOUT = 20000;  
  
    private int capacity = 10;
    
    protected List<ZooKeeper> zks = new ArrayList<ZooKeeper>(capacity);  
    
    private CountDownLatch connectedSignal = new CountDownLatch(capacity);  
  
    private AtomicInteger seq = new AtomicInteger(0);
    public void connect(String hosts) throws IOException, InterruptedException {
    	for (int i=0; i< capacity; i ++) {
    		zks.add(new ZooKeeper(hosts, SESSION_TIMEOUT, this)); 
		}
        connectedSignal.await();
    }  
  
    @Override  
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {  
            connectedSignal.countDown();  
        }  
    }
    
    public ZooKeeper getZk(){
    	int index = seq.getAndIncrement() % capacity;
    	return zks.get(index);
    	
    }
    
}  