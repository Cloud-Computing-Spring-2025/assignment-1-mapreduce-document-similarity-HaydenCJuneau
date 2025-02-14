package com.example.controller;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import com.example.DocumentSimilarityMapper;
import com.example.DocumentSimilarityReducer;
import com.example.DocumentWriteable;
import com.example.FinalReducer;
import com.example.IdentityMapper;

public class DocumentSimilarityDriver {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration config = new Configuration();
        Path out = new Path(args[2]);

        // Task: Jaccard similarity between documents
        Job job1 = Job.getInstance(config, "Jaccard Similarity");
        job1.setJarByClass(DocumentSimilarityDriver.class);
        job1.setMapperClass(DocumentSimilarityMapper.class);
        job1.setReducerClass(DocumentSimilarityReducer.class);
        job1.setOutputKeyClass(DocumentWriteable.class);
        job1.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job1, new Path(args[1]));
        FileOutputFormat.setOutputPath(job1, new Path(out, "out1"));

        if (!job1.waitForCompletion(true)) {
            System.exit(1);
        }

        // Task: Jaccard similarity between documents
        Job job2 = Job.getInstance(config, "Filter");
        job2.setJarByClass(DocumentSimilarityDriver.class);
        job2.setMapperClass(IdentityMapper.class);
        job2.setReducerClass(FinalReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job2, new Path(out, "out1"));
        FileOutputFormat.setOutputPath(job2, new Path(out, "out2"));

        if (!job2.waitForCompletion(true)) {
            System.exit(1);
        }

        System.exit(0);
    }
}