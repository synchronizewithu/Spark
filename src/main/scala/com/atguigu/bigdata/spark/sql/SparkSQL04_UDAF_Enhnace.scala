package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator


object SparkSQL04_UDAF_Enhnace {
  def main(args: Array[String]): Unit = {

    //自定义UDAF函数——升级版（强类型）
    // 创建配置对象
    val sparkConf = new SparkConf().setAppName("SparkSQL01_Demo").setMaster("local[*]")

    // SparkSQL上下文环境对象
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //★★★★
    import sparkSession.implicits._


    //创建函数，并将函数转换为查询的列
    val uDAF = new avgUDAF()
    val col: TypedColumn[Person, Double] = uDAF.toColumn.name("ageAvg")


    //准备数据
    val df: DataFrame = sparkSession.read.json("input/user.json")

//    df.createOrReplaceTempView("person")
    //输入参数为类，因此使用DSL的方式访问数据
    val ds: Dataset[Person] = df.as[Person]
    ds.select(col).show()

    sparkSession.close()
  }
}

//声明样例类
case class Person(name: String, age: Long)

case class avgBuffer(var sumAge: Long, var count: Long)

//声明聚合函数（强类型）
// 继承Aggregator
// 声明泛型,in, buf, out
// 重写方法
class avgUDAF extends Aggregator[Person, avgBuffer, Double] {

  //缓冲区的初始化操作
  override def zero: avgBuffer = {
    avgBuffer(0L, 0L)
  }

  //分区内操作
  override def reduce(buffer: avgBuffer, person: Person): avgBuffer = {
    buffer.sumAge = buffer.sumAge + person.age
    buffer.count = buffer.count + 1L
    buffer
  }

  //分区间操作
  override def merge(buffer1: avgBuffer, buffer2: avgBuffer): avgBuffer = {
    buffer1.sumAge = buffer1.sumAge + buffer2.sumAge
    buffer1.count = buffer1.count + buffer2.count
    buffer1
  }

  //计算操作
  override def finish(reduction: avgBuffer): Double = {
    reduction.sumAge.toDouble / reduction.count
  }

  //自定义类型使用:Encoders.product
  override def bufferEncoder: Encoder[avgBuffer] = Encoders.product

  //Scala中的类型使用
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}