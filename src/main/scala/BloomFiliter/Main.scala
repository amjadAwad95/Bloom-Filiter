package BloomFiliter

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.varia.NullAppender
import org.apache.spark.{SparkConf, SparkContext}
import breeze.util.BloomFilter

object Main {
  def main(args: Array[String]): Unit = {

    val nullAppender = new NullAppender
    BasicConfigurator.configure(nullAppender)

    val sparkConf =
      new SparkConf().setAppName("BloomFilter").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val wordRDD = sc
      .textFile("data/*.txt")
      .flatMap(line ⇒ {
        var splitedString = line.split("\\W+")
        if (splitedString.startsWith(" ")) {
          splitedString = splitedString.slice(1, splitedString.length)
        }
        splitedString
      })
      .filter(x ⇒ x != "")
      .distinct()

    val numberOfElement = wordRDD.count().toInt

    val Array(insertRDD, testRDD) =
      wordRDD.randomSplit(Array(0.8, 0.2), seed = 42)

    val bloomFilter = insertRDD
      .mapPartitions(iter ⇒ {
        val bloomFilter =
          BloomFilter.optimallySized[String](numberOfElement, 0.01)
        iter.foreach(i ⇒ bloomFilter += i)
        Iterator(bloomFilter)
      })
      .reduce(_ | _)

    val falsePositive =
      testRDD.filter(word ⇒ bloomFilter.contains(word)).count().toDouble

    val errorRateEmpirically = falsePositive / testRDD.count()

    testRDD
      .collect()
      .foreach(word ⇒
        println(s"The word ${word}: ${bloomFilter.contains(word)}")
      )

    println(s"The error rate empirically: ${errorRateEmpirically}")
  }
}
