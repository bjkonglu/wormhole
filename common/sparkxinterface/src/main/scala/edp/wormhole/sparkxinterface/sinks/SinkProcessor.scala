/*-
 * <<
 * wormhole
 * ==
 * Copyright (C) 2016 - 2017 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */


package edp.wormhole.sparkxinterface.sinks

import edp.wormhole.common.ConnectionConfig
import edp.wormhole.ums.UmsFieldType.UmsFieldType
import edp.wormhole.ums.UmsProtocolType.UmsProtocolType
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

abstract class SinkProcessor {

  def process(protocolType:UmsProtocolType,
              sourceNamespace:String,
              sinkNamespace:String,
              sinkProcessConfig:SinkProcessConfig,
              schemaMap: collection.Map[String, (Int, UmsFieldType, Boolean)],
              tupleList: Seq[Seq[String]],
              connectionConfig:ConnectionConfig)

  def publicProcess(session:SparkSession,
                    protocolType:UmsProtocolType,
                    sourceNamespace:String,
                    sinkNamespace:String,
                    sinkProcessConfig:SinkProcessConfig,
                    schemaMap: collection.Map[String, (Int, UmsFieldType, Boolean)],
                    data: RDD[Seq[String]],
                    connectionConfig:ConnectionConfig): Unit ={
    //FIXME 将处理后的数据落地
    data.foreachPartition(partition=>{
      println(s"partition----------->" + partition.size)
      println("partition rows--------------->" + partition.mkString(","))
      println("--------------------------------------------")
      val partitionSeq = partition.toSeq
      println(s"partitionList---------->$partitionSeq")
      println("partitionList size--------->" + partitionSeq.size)

      process(protocolType,
        sourceNamespace,
        sinkNamespace,
        sinkProcessConfig,
        schemaMap,
        partitionSeq,
        connectionConfig)

    })
  }


}
