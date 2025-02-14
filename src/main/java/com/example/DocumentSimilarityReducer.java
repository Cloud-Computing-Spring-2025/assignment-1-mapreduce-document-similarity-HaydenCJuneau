package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DocumentSimilarityReducer extends Reducer<Text, DocumentWriteable, DocumentWriteable, IntWritable> {

    private DocumentWriteable document = new DocumentWriteable();
    private final static IntWritable one = new IntWritable(1);

    @Override
    protected void reduce(Text key, Iterable<DocumentWriteable> values, Context context) throws IOException, InterruptedException {
        List<DocumentWriteable> sorted = new ArrayList<>();
        for (DocumentWriteable d: values) sorted.add(d);

        Collections.sort(sorted);

        // Make Pairs
        for (int i = 0; i < sorted.size() - 1; i++) {
            DocumentWriteable a = sorted.get(i);

            for (int j = i + 1; j < sorted.size(); j++) {
                DocumentWriteable b = sorted.get(j);

                document.setId(a.getId() + "_" + b.getId());
                document.setWords(a.getWords() + b.getWords());

                context.write(document, one);
            }
        }
    }
        
}
