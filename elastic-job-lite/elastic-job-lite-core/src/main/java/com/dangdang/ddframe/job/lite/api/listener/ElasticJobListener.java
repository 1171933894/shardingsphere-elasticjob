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

package com.dangdang.ddframe.job.lite.api.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;

/**
 * 弹性化分布式作业监听器接口.（实现对作业执行的同步监听、处理）
 *
 * 若作业处理作业服务器的文件，处理完成后删除文件，可考虑使用每个节点均执行清理任务。
 * 此类型任务实现简单，且无需考虑全局分布式任务是否完成，请尽量使用此类型监听器。
 * 
 * @author zhangliang
 */
public interface ElasticJobListener {
    
    /**
     * 作业执行前的执行的方法.
     * 
     * @param shardingContexts 分片上下文
     */
    void beforeJobExecuted(final ShardingContexts shardingContexts);
    
    /**
     * 作业执行后的执行的方法.
     *
     * @param shardingContexts 分片上下文
     */
    void afterJobExecuted(final ShardingContexts shardingContexts);
}
