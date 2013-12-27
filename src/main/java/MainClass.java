import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass {
    private static ArrayList<String> allVacancyArrayList = new ArrayList();

    public static void main(String[] args) {

        // переменные замера времени работы программы
        long startTime, endTime;

        // переменные для запуска задач с использованием Fork/Join фреймворка
        ForkJoinPool fjp = new ForkJoinPool();

//        // получение карты свойств подключения к БД
//        HashMap<String, String> dbPpopertiesMap = new HashMap<>();
//        dbPpopertiesMap = ReadProperties.getDBProperties();
//        System.out.println(dbPpopertiesMap);
//
//        //получение карты ссылок сайтов
//        HashMap<String, String> linksOfSitesMap = new HashMap<>();
//        linksOfSitesMap = ReadProperties.getLinksOfSites();
//        System.out.println(linksOfSitesMap);

        // получаем стартовый масив-список вакансий
        allVacancyArrayList = DomenParse.regionVacancy();

//        LinkedList<String> urlsLinkedList = new LinkedList<String>(ReadProperties.getLinksOfSites().values());
//        for(String sl: urlsLinkedList){
//            allVacancyArrayList.addAll(ParseRobotaUa.regionSeachVacancy(sl));
//        }

        startTime = System.currentTimeMillis();

        // стартуем задачу через Fork/Join
        ForkComputeAction task = new ForkComputeAction(allVacancyArrayList, 0, allVacancyArrayList.size());
        fjp.invoke(task);

        endTime = System.currentTimeMillis();
        // итоги работы программы
        System.out.println("arrayList.size(): " + allVacancyArrayList.size());
        System.out.println("Time of the parsing: " + (endTime - startTime));
        System.out.println("Parsed pages amount: " + ForkComputeAction.countParsedPages);
    }
}
