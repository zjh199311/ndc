package com.zhongjian.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.PropUtil;

/**
 * 
 * 本类简单实现了分布式锁的机制
 * 
 * 采用单机zookeeper服务器测试；运行多次本机程序，相当于多个客户端用户 全部启动完成后，第一个客户端的会话需要手动中断，相当于触发客户端宕机现象
 * 
 * 
 * 本类实现的分布式锁避免羊群效应（Herd Effect），具体可详见代码
 * 
 * 
 * 
 * 
 * @author Liu Dengtao
 * 
 *         2014-2-28
 */

@Component
public class DistributedLock extends ConnectionWatcher {

	@Autowired
	private PropUtil propUtil;
	
	private volatile boolean isInit = false;
	private static final String GROUPNAME = "orderRoot";
	private static final String MEMBERNAME = "_locknode_";
	private static final String PATH = "/" + GROUPNAME + "/" + MEMBERNAME;
	
	public String join( String bucket) throws KeeperException, InterruptedException {
		String groupPath = PATH;
		String path = groupPath + "/lock-" + bucket + "-";
		// 建立一个顺序临时节点
		String createdPath = getZk().create(path, null/* data */, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		return createdPath;
	}

	public void delete(String path) throws InterruptedException, KeeperException {
		getZk().delete(path, 0);
		System.out.println(new Date() + "我释放了分布锁，哈哈！ myId:" + path);
	}

	/**
	 * 检查本客户端是否得到了分布式锁
	 * 
	 * @param groupPath
	 * @param myName
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean checkState(String myName) throws KeeperException, InterruptedException {
		String groupPath = PATH;
		List<String> childList = getZk().getChildren(groupPath, false);
		String[] myStr = myName.split("-");
		long myId = Long.parseLong(myStr[2]);
	    String myBucket = myStr[1];
		boolean minId = true;
		for (String childName : childList) {
			String[] str = childName.split("-");
			String bucket = str[1];
			if (bucket.equals(myBucket)) {
				long id = Long.parseLong(str[2]);
				if (id < myId) {
					minId = false;
					break;
				}
			}
		}
		if (minId) {
			System.out.println(new Date() + Thread.currentThread().getName() + "我得到了分布锁，哈哈！ myId:" + myId);
			return true;
		} else {
			System.out.println(new Date() + Thread.currentThread().getName() + "继续努力吧，  myId:" + myId);
			return false;
		}
	}

	/**
	 * 若本客户端没有得到分布式锁，则进行监听本节点前面的节点（避免羊群效应）
	 * 
	 * @param groupPath
	 * @param myName
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean listenNode(final String myName, final CountDownLatch cLatch)
			throws KeeperException, InterruptedException {
		String groupPath = PATH;
		List<String> childList = getZk().getChildren(groupPath, false);

		String[] myStr = myName.split("-");
		String myBucket = myStr[1];
		long myId = Long.parseLong(myStr[2]);

		List<Long> idList = new ArrayList<Long>();
		Map<Long, String> sessionMap = new HashMap<Long, String>();

		for (String childName : childList) {
			String[] str = childName.split("-");
			String bucket = str[1];
			if (bucket.equals(myBucket)) {
				long id = Long.parseLong(str[2]);
				idList.add(id);
				sessionMap.put(id, str[1] + "-" + str[2]);
			}
		}

		Collections.sort(idList);

		int i = idList.indexOf(myId);
		if (i < 0) {
			throw new IllegalArgumentException("数据错误！");
		}

		if (i == 0) {
			return checkState( myName);
		}
		// 得到前面的一个节点
		long headId = idList.get(i - 1);

		String headPath = groupPath + "/lock-" + sessionMap.get(headId);
		System.out.println(Thread.currentThread().getName() + "添加监听：" + headPath);
		Stat stat = getZk().exists(headPath, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println(Thread.currentThread().getName() + "已经触发了" + event.getType() + "事件！");
				try {
					if (checkState(myName)) {
						cLatch.countDown();
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		if (stat == null) {
			return checkState( myName);
		} else {
			return false;
		}

	}

	public String lock(String lockTag) throws Exception {
		if (!isInit) {
			synchronized (this) {
				if (!isInit) {
					this.connect(propUtil.getZkAddress().trim());
					isInit = true;
				}
			}
		}
		String node = this.join(lockTag);
		if (!this.checkState(node)) {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			if (!this.listenNode(node, countDownLatch)) {
				countDownLatch.await();
			}
		}
		return node;
	}

	public void unlock(String node) throws Exception {
		this.delete(node);
	}
	
}
