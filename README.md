Large Graph Analysis Using HDFS, Hadoop and Java - MapReduce
------------------------------------------------------------

A MapReduce program to calculate the sum of the weights of all incoming edges for each node in the graph.
 - Loaded two graph files into HDFS. 
     Each file stores a list of edges as tab separatedvalues.  
     Each line represents a single edge consisting of three columns: 
     (source node ID, target node ID, edge weight), 
      Each of which is separated by a tab (\t). 
      Node IDs are positive integers, and weights are also positive integers. 
      Edges are sorted in a random order.
src tgt weight
51 117 1
51 194 1
51 299 3
151 230 51
151 194 79
130 51 10

 - Code accepts two arguments upon running. The first argument (args[0]) will be a
path for the input graph file on HDFS, and the second argument (args[1]) will be a path for
output directory. The default output mechanism of Hadoop will create multiple files on the output
directory such as part00000, part00001, which will be merged and downloaded to a local 
directory by the run script. 

The format of the output is as follows. 
  - Each line represents a node ID and the sum of its incoming edges’ weights. 
  - The ID and the sum are separated by a tab(\t), and lines are not sorted. 
The following example result computed based on the toy graph above.
51 10
117 1
194 80
230 51
299 3

Nodes with no incoming edges (i.e. the sum equals zero) are excluded.


- Implementation
For each string of src,tgt,weight received by the mapper 
  - The mapper will tokenize this string and return the last two tokens as the key value pair of target node and weight
  - Reducer receiving all the values for a given key, will sum them. 
  - If the sum is > 0, it will publish the key,sum


Directory Structure
-------------------
1.
Compiling the Source to get a Jar to deploy on hdfs
pom.xml contains necessary dependencies and compile configurations for each task.
To compile, you can simply call Maven in the corresponding directory (Task1 or
Task2 where pom.xml exists) by this command:
mvn package


2.
Distributing up the data files on HDFS for each node
sudo su hdfs
hadoop fs mkdir
/user/cse6242/
hadoop fs chown
cloudera /user/cse6242/
exit
su cloudera
hadoop fs put
graph1.tsv /user/cse6242/graph1.tsv
hadoop fs put
graph2.tsv /user/cse6242/graph2.tsv


3.
Running the jar using the run<>.sh script
1) run<>.sh will run JAR on Hadoop/Scala specifying the input file on HDFS (the first
argument) and output directory on HDFS (the second argument)
2) It will merge outputs from output directory and download to local file system.
3) remove the output directory on HDFS.


- MapReduce Implemented in Java on Hadoop, Using HDFS
