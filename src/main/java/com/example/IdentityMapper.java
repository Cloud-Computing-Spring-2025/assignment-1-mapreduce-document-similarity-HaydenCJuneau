package com.example;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class IdentityMapper extends Mapper<DocumentWriteable, IntWritable, DocumentWriteable, IntWritable> {

    @Override
    protected void map(DocumentWriteable key, IntWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, value);
    }
    
}
