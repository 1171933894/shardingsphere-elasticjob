/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.lite.internal.election;

import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;

/**
 * 主节点路径.
 * 
 * @author zhangliang
 */

/**
 * >>在 leader 目录下一共有三个存储子节点：
 *
 * >election：主节点选举
 *
 * [zk: localhost:2181(CONNECTED) 1] ls /elastic-job-example-lite-java/javaSimpleJob/leader/election
 * [latch, instance]
 * [zk: localhost:2181(CONNECTED) 2] get /elastic-job-example-lite-java/javaSimpleJob/leader/election/instance
 * 192.168.16.137@-@1910
 *
 * /leader/election/instance 是临时节点，当作业集群完成选举后，存储主作业实例主键( JOB_INSTANCE_ID )。
 * /leader/election/latch 主节点选举分布式锁，是 Apache Curator 针对 Zookeeper 实现的分布式锁的一种，笔者暂未了解存储形式，无法解释。
 *
 * >sharding：作业分片项分配
 *
 * [zk: localhost:2181(CONNECTED) 1] ls /elastic-job-example-lite-java/javaSimpleJob/leader/sharding
 * [necessary, processing]
 * [zk: localhost:2181(CONNECTED) 2] 个get /elastic-job-example-lite-java/javaSimpleJob/leader/sharding
 * [zk: localhost:2181(CONNECTED) 3] 个get /elastic-job-example-lite-java/javaSimpleJob/leader/processin
 *
 * /leader/sharding/necessary 是永久节点，当相同作业有新的作业节点加入或者移除时，存储空串( "" )，标记需要进行作业分片项重新分配；当重新分配完成后，移除该数据节点。
 * /leader/sharding/processing 是临时节点，当开始重新分配作业分片项时，存储空串( "" )，标记正在进行重新分配；当重新分配完成后，移除该数据节点。
 *
 * >failover：作业失效转移
 *
 * [zk: localhost:2181(CONNECTED) 2] ls /elastic-job-example-lite-java/javaSimpleJob/leader/failover
 * [latch, items]
 * [zk: localhost:2181(CONNECTED) 4] ls /elastic-job-example-lite-java/javaSimpleJob/leader/failover/items
 * [0]
 *
 * /leader/failover/latch 作业失效转移分布式锁，和 /leader/failover/latch 是一致的。
 * /leader/items/${ITEM_ID} 是永久节点，当某台作业节点 CRASH 时，其分配的作业分片项标记需要进行失效转移，存储其分配的作业分片项的 /leader/items/${ITEM_ID} 为空串( "" )；当失效转移标记，移除 /leader/items/${ITEM_ID}，存储 /sharding/${ITEM_ID}/failover 为空串( "" )，临时节点，需要进行失效转移执行。
 */
public final class LeaderNode {
    
    /**
     * 主节点根路径.
     */
    public static final String ROOT = "leader";
    
    static final String ELECTION_ROOT = ROOT + "/election";
    
    static final String INSTANCE = ELECTION_ROOT + "/instance";
    
    static final String LATCH = ELECTION_ROOT + "/latch";
    
    private final JobNodePath jobNodePath;
    
    LeaderNode(final String jobName) {
        jobNodePath = new JobNodePath(jobName);
    }
    
    boolean isLeaderInstancePath(final String path) {
        return jobNodePath.getFullPath(INSTANCE).equals(path);
    }
}
