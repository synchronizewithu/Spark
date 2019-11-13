package com.atguigu.bigdata.spark.streaming


import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming04_KafkaSource {
  def main(args: Array[String]): Unit = {

    //创建对接kafka的sparkStreaming

    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("kafka").setMaster("local[*]")
    val context = new StreamingContext(sparkConf, Seconds(3))

    val kafkaDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
      context,
      Map(
        ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
        //老版本中还是使用zookeeper存储消费者offset
        //新版本使用BOOTSTRAP_SERVERS_CONFIG=bootstrap.servers
        "zookeeper.connect" -> "hadoop102:2181",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer"
      ),
      Map("atguigu" -> 3),
      StorageLevel.MEMORY_ONLY
    )

    val lineDStream: DStream[Char] = kafkaDStream.flatMap(_._2)

    val wordToOneDStream: DStream[(Char, Int)] = lineDStream.map( (_,1) )

    val wordToCountDStream: DStream[(Char, Int)] = wordToOneDStream.reduceByKey(_+_)

    wordToCountDStream.print()

    context.start()

    context.awaitTermination()


  }
}
