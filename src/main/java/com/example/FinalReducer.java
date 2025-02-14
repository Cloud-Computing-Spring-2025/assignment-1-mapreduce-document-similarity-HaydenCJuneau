package com.example;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FinalReducer extends Reducer<DocumentWritable, IntWritable, Text, DoubleWritable> {

    private Text documentNames = new Text();
    private DoubleWritable similarity = new DoubleWritable();

    @Override
    protected void reduce(DocumentWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int intersection = 0;
        for (IntWritable i: values) intersection++;
        
        documentNames.set(key.getId());
        similarity.set(intersection / (key.getWords() - intersection));

        context.write(documentNames, similarity);
    }
    
}
