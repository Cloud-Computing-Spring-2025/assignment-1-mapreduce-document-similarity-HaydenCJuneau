package com.example.controller;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

import com.example.DocumentSimilarityMapper;
import com.example.DocumentSimilarityReducer;
import com.example.DocumentWritable;
import com.example.FinalReducer;
import com.example.IdentityMapper;

public class DocumentSimilarityDriver {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration config = new Configuration();
        Path out = new Path(args[1]);

        // Pair the documents to their words
        Job job1 = Job.getInstance(config, "Jaccard Similarity");
        job1.setJarByClass(DocumentSimilarityDriver.class);

        job1.setMapperClass(DocumentSimilarityMapper.class);
        job1.setReducerClass(DocumentSimilarityReducer.class);

        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(DocumentWritable.class);

        job1.setOutputKeyClass(DocumentWritable.class);
        job1.setOutputValueClass(IntWritable.class);

        job1.setOutputFormatClass(SequenceFileOutputFormat.class);

        // Input and Output Paths for First Job
        FileInputFormat.addInputPath(job1, new Path(args[0]));
        Path intermediateOutput = new Path(out, "out1");
        FileOutputFormat.setOutputPath(job1, intermediateOutput);

        if (!job1.waitForCompletion(true)) {
            System.exit(1);
        }

        // Sort and compute similarity
        Job job2 = Job.getInstance(config, "Filter");
        job2.setJarByClass(DocumentSimilarityDriver.class);

        job2.setMapperClass(IdentityMapper.class);
        job2.setReducerClass(FinalReducer.class);

        job2.setInputFormatClass(SequenceFileInputFormat.class);

        job2.setMapOutputKeyClass(DocumentWritable.class);
        job2.setMapOutputValueClass(IntWritable.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job2, intermediateOutput);
        FileOutputFormat.setOutputPath(job2, new Path(out, "out2"));

        if (!job2.waitForCompletion(true)) {
            System.exit(1);
        }

        System.exit(0);
    }
}