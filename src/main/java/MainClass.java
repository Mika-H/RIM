import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.*;

public class MainClass {

    public static void main(String[] args) {
        double startTime, endTime;
        try {
            Parser.createDataTree();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Properties dbProperties = new Properties();
        FileInputStream in = null;
        try {

            in = new FileInputStream(new File("..\\RIM\\src\\main\\resources\\dbproperties.properties"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            dbProperties.load(in);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String userName = dbProperties.getProperty("username");
        String password = dbProperties.getProperty("password");
        String url = dbProperties.getProperty("url");

        System.out.println(userName + password + url);



////        String url1 = "http://microformats.org";
////        String url2 = "http://ukr.net";
//        ForkJoinPool fjp = new ForkJoinPool();
//        ArrayList<String> arrayList = new ArrayList<>(20_000);
//        ArrayList arrayListTemp;
//        arrayList.add("http://microformats.org");
//        arrayList.add("http://ukr.net");
//        arrayList.add("http://slando.ua");
////        arrayList.add(url2);
////        arrayList.ensureCapacity(10_000_000_000);
//        startTime = System.currentTimeMillis();
//        // в циклі запускаємо опрацювання задачі
//        for (int i = 0; i<2; i++) {
//            ForkCompute task = new ForkCompute(arrayList, 0, arrayList.size());
//
//            arrayListTemp = fjp.invoke(task);
//            arrayList.clear();
//            arrayList = arrayListTemp;
//            task.reinitialize();
//
//        }
//        endTime = System.currentTimeMillis();
//
////        for(String url: arrayList){
////            System.out.println(url);
////        }
//        System.out.println("arrayList.size(): " + arrayList.size());
//        System.out.println("Time of the parsing: " + (endTime - startTime));
//        System.out.println("Parsed pages amount: " + ForkCompute.countParsedPages);
    }
}
