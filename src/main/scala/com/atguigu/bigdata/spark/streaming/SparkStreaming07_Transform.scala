package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object SparkStreaming07_Transform {
  def main(args: Array[String]): Unit = {
    //使用StreamingContext完成wordCount

    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    //创建SparkStreaming上下文对象
    //第二个参数表示数据采集的周期
    val context: StreamingContext = new StreamingContext(sparkConf,Seconds(3))

    //从指定的端口获取数据
    val socketDStream: ReceiverInputDStream[String] = context.socketTextStream("hadoop102",9999)

    //将每一行的数据进行扁平化操作
    val lineDStream: DStream[String] = socketDStream.flatMap {
      line => {
        line.split(" ")
      }
    }

    //将每个单词进行结构的转换
    val wordToOneDStream: DStream[(String, Int)] = lineDStream.map {
      word => (word, 1)
    }

    //计算出每个单词出现的个数
    val wordToSumDStream: DStream[(String, Int)] = wordToOneDStream.reduceByKey(_+_)


//    wordToSumDStream.foreachRDD{
//      rdd=>{
//        rdd.foreachPartition()
//      }
//    }


    /*
    // TODO 代码（Driver）（一次）
    val sumDStream: DStream[Int] = wordToSumDStream.map {
        case (word, sum) => {
            // TODO 代码（Executor）(N次)
            sum
        }
    }

    // TODO 代码（Driver）（1次）
    val value: DStream[Int] = wordToSumDStream.transform {
        rdd => {
            // TODO 代码(Driver)(M次)  比如取黑名单判断
            rdd.map{
                // TODO 代码（Executor）(N次)
                _._2
            }
        }
    }
    */



    //启动采集器
    context.start()

    //driver等待采集器结束
    context.awaitTermination()
  }
}
