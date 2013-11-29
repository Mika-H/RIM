import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    //каталоги в которых хранится вся пропарсенная информация
    private static File catalogData;
    private static File catalogUrl;
    private static File catalogMicroformat;
    private static File urlMicroformat;
    private static FileWriter fr;

    private String url;
    private String[] href;
    Result sLink;
    private Document doc;
    private ArrayList<String> allLink;

    public Parser(String url) throws IOException{
        this.url = url;
        init(url);
        seachLink();
    }

    public void init(String domenUrl)throws IOException{
        // приведение строки URL к нужному виду "http://site.com/example.html"
        if(url==null) System.err.println("URL don't found");
        else if (url.indexOf("http://")==-1) {
            url="http://".concat(url);


        }
        doc = Jsoup.connect(url)
                .timeout(10000)
                .userAgent("Mozilla")
                .get();


    }
    public Result seachLink(){
        sLink = Result.get();
        sLink.setDescription("Link: ");
        Elements el = doc.select("a[href]");
        sLink.setSize(el.size());
        Object [] e = el.toArray();
        href = new String[e.length];
        int i =0;
        for(Element element: el){

            href[i] = element.attr("href");
            System.out.println(i+". "+href[i]);
            i++;
            //allLink.add(href[i]);
        }
        sLink.add(url, href);
        return sLink;
    }
    public String toString(Result r){

        return r.toString(url);
    }

    // создаем дерево каталог для складирования пропарсеной информации
    public final static void createDataTree() throws IOException{
        catalogData = new File(System.getProperty("user.dir")+File.separator+"data");
        catalogUrl = new File(catalogData.getAbsolutePath()+File.separator+"url");
        catalogMicroformat = new File(catalogData.getAbsolutePath()+File.separator+"microformat");
        createCatalog(new File[] {catalogData, catalogUrl, catalogMicroformat});

    }

    private static void createCatalog(File ...catalogs){
        for(File catalog: catalogs){
            if(!catalog.exists()||!catalog.isDirectory()){
                catalog.mkdirs();
                System.out.println("Каталог "+catalog.getAbsolutePath()+" создан");
            }
            else{
                System.out.println("Каталог "+catalog+" уже существует");
            }
        }
    }

    private static void createFile(File file ){
        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("Файл "+file.getAbsolutePath()+" создан");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static void destroy() throws IOException{
        if (fr!=null) {
            fr.close();
            System.out.println("Считывающий поток закрыт");
        }
    }

    /*Здесь храним ссылки на страницы сайтов на которых найдены микроформаты."
    + " Формат записи следующий childUrl~parentUrl~microformatName */
    public static void addMicroformatInFile(String childUrl, String parentUrl, String microformatName){
        try {
            urlMicroformat = new File(catalogMicroformat, "url.txt");
            createFile(urlMicroformat);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(urlMicroformat, true))); //поток записи в файл информации о микроформатах
            pw.println(childUrl+"~"+parentUrl+"~"+microformatName);
            pw.flush();
            pw.close();
            System.out.println("Найден МИКРОФОРМАТ "+microformatName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLinksInFile(String fileName, ArrayList<String> information) throws IOException{
        File fileForWrite = new File(catalogUrl.getPath()+File.separator+fileName+".txt");

        try {
            createFile(fileForWrite);
            FileWriter fwLink = new FileWriter(fileForWrite);
            BufferedWriter bw = new BufferedWriter(fwLink);
            PrintWriter out = new PrintWriter(bw);
            for(String linkData: information){
                out.println(linkData);
                out.flush();
            }
            fwLink.close();
            bw.close();
            out.close();
        } catch (IOException e) {
            //createDataTree(); //создаем дерево каталогов
            System.err.println("Ошибка записи в файл "+fileForWrite.getName());
            System.err.println(e);
        }

    }
    // Метод повертає назву домену з посилання що йому передається
    public static String getDomain(String url){
        int length =url.indexOf("/",9);
        if(length==-1){
            System.out.println("Домен: "+url.substring(7));
            return url.substring(7);
        }
        else{
            System.out.println("Домен: "+url.substring(7, length));
            return url.substring(7, length);
        }
    }
    //Метод для Fork :-)
    public static ArrayList<String> parse(String urlData){
        // ініціалізація данних для коректної роботи парсера

        String parentUrl; //url сторінки з якої була отримане посилання
        String childUrl; //url сторінки для якої проводиться парсинг
        Document doc;
        ArrayList<String> linkAll;
        int position=urlData.indexOf("~");//номер символа з якого починається посилання
        //Випадок з пустим посиланням
        if(urlData==""||urlData==null) {
            System.err.println("URL don't found");
            return new ArrayList<String>();
        }
        //Випадок коли в метод передані два посилання розділених "~"
        if (position!=-1){
            childUrl=urlData.substring(0, position);
            parentUrl=urlData.substring(position+1);
        }
        //Коли в метод передане одне посилання
        else{
            parentUrl="http://";
            childUrl=urlData;
        }
        //Коли посилання не повне
        if (childUrl.indexOf("http://")==-1) {
            childUrl=parentUrl.concat("/").concat(childUrl);

        }
        try{
            doc = Jsoup.connect(childUrl)
                    .timeout(10000)
                    .userAgent("Mozilla")
                    .get();
            Elements el = doc.select("a[href]");
            Elements hCard = doc.select("vCard");
            Elements hCalendar_s = doc.select("span").addClass("vevent").addClass("vcalendar");
            Elements hCalendar_d = doc.select("div").addClass("vevent").addClass("vcalendar");
            Elements hCalendar_a = doc.select("abbr").addClass("vevent").addClass("vcalendar");
            //Elements hCalendar=doc.select("span").addClass("vcalendar");
            Element geo = doc.addClass("geo");

            linkAll = new ArrayList<String>(el.size());
            for(Element element: el){
                linkAll.add(element.attr("href")+"~"+childUrl);
            }
/*			if(){

				addMicroformatInFile(childUrl, parentUrl, "hCard");
				System.out.println("В hcard записано:"+hCard);
			}*/
            if(hCard!=null||hCalendar_s!=null||hCalendar_d!=null||hCalendar_a!=null||geo!=null){
                addMicroformatInFile(childUrl, parentUrl, "Microformat");
            }
	/*		if(){
				addMicroformatInFile(childUrl, parentUrl, "geo");
				System.out.println("В geo записано:"+hCalendar_s);
			}*/
            writeLinksInFile(getDomain(childUrl), linkAll);
            return linkAll;
        }
        catch (IOException e){
            if ( e.getMessage().contains("Unhandled content type") ||
                    e.getMessage().contains("Premature EOF") ||
                    e.getMessage().contains("Read timed out") ||
                    e.getMessage().contains("403 error loading URL") ||
                    e.getMessage().contains("404 error loading URL") ||
                    e.getMessage().contains("405 error loading URL") ||
                    e.getMessage().contains("500 error loading URL") ||
                    e.getMessage().contains("503 error loading URL") ) {
                System.err.println("Внимание! Страницу "+urlData+" пропарсить не удалось! "+ e.getMessage());
                return new ArrayList<String>();
            }
        }
        catch (IllegalArgumentException ie){
            System.err.println("Внимание! Не верный формат url:"+childUrl+"  "+ie.getMessage());
        }

        return new ArrayList<String>();
    }


    public static void main(String[] args)throws IOException {
        createDataTree();
        long start = new Date().getTime();
        ArrayList<String>test = new ArrayList<>();/* = parse("http://microformats.org");*/
        test.add("http://microformats.org");
        test.add("http://ukr.net");
        test.add("http://slando.ua");

        ArrayList<String> temp= new ArrayList<String>();
        int i = 0;
        for(int p=0; p<2; p++){
            for (String task:test){
                System.out.println(i+". "+task);
                temp.addAll(parse(task));
                System.out.println(temp.size());
                i++;
                if(i%50==0){
                    long tempTime =new Date().getTime()-start;
                    System.out.println("Время парсинга "+i+" страниц"+tempTime/1000+" секунд.");
                    System.out.println("Среднее время парсинга одной страницы: "+tempTime/(1000.00*i)+" секунд");
                }

            }
            System.out.println();
            System.out.printf("!!!!Круг %d пройден!!!!", p);
            test.clear();
            test.addAll(temp);
        }
        long end = new Date().getTime();
//        for(String link: temp)
//            System.out.println(link);
        System.out.println(temp.size());

        long timeWork = end-start; // время выполнения кода
        System.out.println("Время парсинга: "+timeWork/1000+" секунд.");
    }


}
