package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Task1 {


 /* mapper class for each src,tgt wt - spit out tgt, wgt if wgt > 0 */
  public static class EdgeWeightMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        // skip the source node
        itr.nextToken();
        // get the target node
        word.set(itr.nextToken());
        // get the wright of the incoming ink to the target node
        int weight = Integer.parseInt(itr.nextToken());
        context.write(word, new IntWritable(weight));

      }
    }
  }


  /* 
   * mapper class to sum the incoming edge weights of the target nodes as key
   *   
   */
  public static class EdgeWeightReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      if ( sum > 0 ) {
       result.set(sum);
       context.write(key, result);
      }
    }
  }




  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("mapreduce.output.textoutputformat.separator", "\t");


    Job job = Job.getInstance(conf, "Task1");

    /* TODO: Needs to be implemented */

    job.setJarByClass(Task1.class);
    job.setMapperClass(EdgeWeightMapper.class);
    job.setCombinerClass(EdgeWeightReducer.class);
    job.setReducerClass(EdgeWeightReducer.class);
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
