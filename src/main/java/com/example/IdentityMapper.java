package com.example;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class IdentityMapper extends Mapper<DocumentWritable, IntWritable, DocumentWritable, IntWritable> {

    @Override
    protected void map(DocumentWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, value);
    }
    
}
