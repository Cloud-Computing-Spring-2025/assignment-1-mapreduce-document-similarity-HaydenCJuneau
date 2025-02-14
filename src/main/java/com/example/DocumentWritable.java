package com.example;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class DocumentWritable implements WritableComparable<DocumentWritable> {

    private String id;
    private int words;
    
    public DocumentWritable() {
        this.id = "";
        this.words = 0;
    }

    public DocumentWritable(String id, int words) {
        this.id = id;
        this.words = words;
    }
    
    @Override
    public void write(DataOutput out) throws IOException {
        WritableUtils.writeString(out, id);
        out.writeInt(words);
    }
    
    @Override
    public void readFields(DataInput in) throws IOException {
        id = WritableUtils.readString(in);
        words = in.readInt();
    }
    
    @Override
    public int compareTo(DocumentWritable o) {
        int cmp = id.compareTo(o.getId());
        if (cmp != 0) return cmp;
        return Integer.compare(words, o.getWords());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, words);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DocumentWritable other = (DocumentWritable)obj;
        return Objects.equals(id, other.getId()) && Objects.equals(words, other.getWords());
    }
    
    @Override
    public String toString() {
        return id + ". Unique Words: " + words; 
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public int getWords() {
        return words;
    }
    
    public void setWords(int words) {
        this.words = words;
    }
}
