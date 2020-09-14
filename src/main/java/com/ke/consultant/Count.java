package com.ke.consultant;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;


public class Count {
    public static void main(String[] args) throws Exception{
        if (args.length < 1) {
            System.out.println("Enter filename");
            return;
        }
        HashMap<String, Integer> map = new HashMap<>();
        int finalCount = 0;
        try {
            Path file = Paths.get("", "Input\\" + args[0]);
            File textFile = new File(file.toString());
            BufferedReader myReader = new BufferedReader(new FileReader(textFile));
            String line = "";
            String [] words;
            while ((line = myReader.readLine()) != null) {
                words = line.split(" ");
                for (String word: words) {
                    if (!map.containsKey(word)) {
                        map.put(word, 1);
                    } else {
                        Integer count = map.get(word);
                        map.replace(word, ++count);
                    }
                }
            }
            myReader.close();
            map.forEach((s, integer) -> System.out.println(s + " " + integer));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
