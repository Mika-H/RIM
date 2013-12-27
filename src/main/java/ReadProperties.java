import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadProperties {
    private static HashMap<String, String> mapOfLinksOfSites = new HashMap();
    private static HashMap<String, String> mapOfDBProperties = new HashMap();

    // метод реализовывает получение карты ссылок сайтов
    public static HashMap<String, String> getLinksOfSites(){
        try (FileInputStream in = new FileInputStream(new File
                (System.getProperty("user.dir")+"\\src\\main\\resources\\linksOfSites.properties"))) {

            Properties linksOfSitesProperty = new Properties();
            linksOfSitesProperty.load(in);
            for(String key: linksOfSitesProperty.stringPropertyNames()){
                mapOfLinksOfSites.put(key, linksOfSitesProperty.getProperty(key));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, "Файл ссылок сайтов не найден", ex);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, "Ошибка чтения файла", ex);
        }
    return mapOfLinksOfSites;
    }

    // метод реализзовывает получение карты свойств подключения к БД
    public static HashMap<String, String> getDBProperties(){
        try (FileInputStream in = new FileInputStream(new File
                (System.getProperty("user.dir")+"\\src\\main\\resources\\dbproperties.properties"))) {

            Properties dbProperty = new Properties();
            dbProperty.load(in);
            for(String key: dbProperty.stringPropertyNames()){
                mapOfDBProperties.put(key, dbProperty.getProperty(key));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, "Файл свойство подключения к БД не найден", ex);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, "Ошибка чтения файла", ex);
        }
        return mapOfDBProperties;
    }
}
