# Bloom Filter with Apache Spark

## Overview
This project implements a Bloom Filter using Apache Spark to efficiently test set membership with a low false positive rate. The program reads text data, extracts unique words, and applies a Bloom Filter to estimate set membership.

## Prerequisites
Ensure you have the following installed:
- Apache Spark
- Scala
- sbt (Scala Build Tool)

## Setup & Installation
1. Clone this repository:
   ```bash
   git clone [<repository-url>](https://github.com/amjadAwad95/Bloom-Filiter.git)
   ```
2. Ensure the required dependencies are available in `build.sbt`:
   ```sbt
   libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.3"
   libraryDependencies += "org.scalanlp" %% "breeze" % "2.1.0"
   libraryDependencies += "org.scalanlp" %% "breeze-viz" % "2.1.0"
   ```
3. Compile the project:
   ```bash
   sbt compile
   ```
4. Run the application:
   ```bash
   sbt run
   ```

## How It Works
1. Reads text data from the `data/` directory.
2. Splits text into words and removes duplicates.
3. Splits the dataset into an insert set (80%) and a test set (20%).
4. Creates a Bloom Filter and inserts words from the insert set.
5. Tests the words from the test set for membership.
6. Computes the empirical error rate.

## Key Features
- Uses **Breeze's BloomFilter** for probabilistic membership testing.
- Distributed processing with **Apache Spark**.
- Configurable false positive rate.

## Example Output
```
The word example: true
The error rate empirically: 0.015
```

## License
This project is open-source and available under the MIT License.

