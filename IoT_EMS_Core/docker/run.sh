#!/bin/sh
#java -classpath "./lib/cassandra-driver-core-3.3.0.jar:./lib/metrics-core-3.2.2.jar:./lib/mysql-connector-java-5.1.43.jar:./lib/netty-handler-4.1.15.Final.jar:./lib/slf4j-api-1.7.25.jar:./lib/guava-18.0.jar:./lib/netty-3.10.6.Final:./" Core.main.Main


java -classpath "./lib/cassandra-driver-core-3.3.0.jar:./lib/metrics-core-3.2.2.jar:./lib/mysql-connector-java-5.1.43.jar:./lib/slf4j-api-1.7.25.jar:./lib/guava-18.0.jar:./lib/netty-all-4.0.47.Final.jar:./classes" Core.main.Main
