package com.atguigu.bigdata.spark.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

object SparkSQL03_UDAF {
  def main(args: Array[String]): Unit = {

    //自定义UDAF函数

    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("UDAF").setMaster("local")
    val sparkSession: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //创建函数
    val uDAF = new AvgAgeUDAF()
    //向Spark环境中注册
    sparkSession.udf.register("myAvg", uDAF)

    //读取数据集
    val df: DataFrame = sparkSession.read.json("input/user.json")

    //创建临时表
    df.createOrReplaceTempView("user")

    //使用sql访问数据
    sparkSession.sql("select myAvg(age) from user").show()

    //关闭资源
    sparkSession.close()
  }
}

//声明聚合函数
// 继承UserDefinedAggregateFunction
// 重写方法
// 需要通过聚合函数保存中间的变量，然后再计算最终的结果
// select avg(age) from user
class AvgAgeUDAF extends UserDefinedAggregateFunction {

  //输入参数的类型
  override def inputSchema: StructType = {
    new StructType().add("age", LongType)
  }

  //缓冲区的数据类型
  //1。年龄的总和
  //2.总共的人数
  override def bufferSchema: StructType = {
    new StructType().add("sum", LongType).add("count", LongType)
  }

  //计算结果的类型
  override def dataType: DataType = DoubleType

  //函数的稳定性
  override def deterministic: Boolean = true

  //初始化缓冲区的数据 ROW类型的子类，按照顺序进行初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  //对分区内的数据进行操作
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  //对分区间的数据进行操作
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //计算
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}