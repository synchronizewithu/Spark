package com.atguigu.bigdata.spark.streaming

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

//优雅的关闭
object SparkStreaming08_Stop {
  def main(args: Array[String]): Unit = {

    //优雅的关闭

    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")

    sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    //创建SparkStreaming上下文对象
    //第二个参数表示数据采集的周期
    val context: StreamingContext = new StreamingContext(sparkConf, Seconds(3))

    //从指定的端口获取数据
    val socketDStream: ReceiverInputDStream[String] = context.socketTextStream("hadoop102", 9999)

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
    val wordToSumDStream: DStream[(String, Int)] = wordToOneDStream.reduceByKey(_ + _)

    //打印结果
    wordToSumDStream.print()


    //启动新的线程，希望在特殊的场合可以关闭SparkStreaming
    new Thread(
      new Runnable {
        override def run(): Unit = {

          while (true) {
            try {
              Thread.sleep(5000)
            } catch {
              case ex: Exception => println(ex)
            }

            //监控HDFS文件的变化
            val fileSystem: FileSystem = FileSystem.get(new URI("hdfs://hadoop102:9000"), new Configuration(), "atguigu")

            //获取环境的状态
            val state: StreamingContextState = context.getState()

            //如果环境对象处于活跃状态，可以进行关闭操作
            if (state == StreamingContextState.ACTIVE) {

              val flag: Boolean = fileSystem.exists(new Path("hdfs://hadoop102:9000/stopSpark"))
              if (flag) {
                context.stop(true, true)
                //当对象关闭时，此线程需要结束
                System.exit(0)
              }
            }
          }


        }
      }
    ).start()


    //启动采集器
    context.start()

    //driver等待采集器结束
    context.awaitTermination()
  }
}
