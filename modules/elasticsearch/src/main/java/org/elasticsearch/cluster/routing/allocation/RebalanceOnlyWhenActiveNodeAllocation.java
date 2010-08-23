/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.cluster.routing.allocation;

import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.MutableShardRouting;
import org.elasticsearch.cluster.routing.RoutingNodes;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.settings.Settings;

import java.util.List;

/**
 * Only allow rebalancing when all shards are active within the shard replication group.
 *
 * @author kimchy (shay.banon)
 */
public class RebalanceOnlyWhenActiveNodeAllocation extends NodeAllocation {

    public RebalanceOnlyWhenActiveNodeAllocation(Settings settings) {
        super(settings);
    }

    @Override public boolean canRebalance(ShardRouting shardRouting, RoutingNodes routingNodes, DiscoveryNodes nodes) {
        List<MutableShardRouting> shards = routingNodes.shardsRoutingFor(shardRouting);
        for (ShardRouting allShard : shards) {
            if (!allShard.active()) {
                return false;
            }
        }
        return true;
    }
}
