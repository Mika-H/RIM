//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;
///**
// * This class implements connection pool to database
// * @author Misha Horodny
// * @version 0.3
// */
//public class ConnectionPool {
//    private static String userName;
//    private static String password;
//    private static String url;
//    private static int maxconn = 10;
//    private static int withdrowed = 0; // counter of connection
//    private static ConnectionPool conPool;
//    private Connection[] connarray = new Connection[maxconn];
//    /**
//     * This method initiates data about database.
//     * We get informations about user name, his password and url from properties file.
//     * @throws IOException
//     */
//    private static void getDbProperties() throws IOException{
//        try {
//            FileInputStream in = new FileInputStream(new File("C:\\Users\\MIKA\\Documents\\NetBeansProjects\\ConnectionPool\\src\\connectionpool\\dbproperties.properties"));
//            Properties dbProperties = new Properties();
//            dbProperties.load(in);
//            userName = dbProperties.getProperty("userName");
//            password = dbProperties.getProperty("password");
//            url = dbProperties.getProperty("url");
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    /**
//     * Creating connection pool array, wich contains connections group.
//     * It is necesery to storage connectins in random access memory.
//     * Variable maxconn  is maximum connections in ConnectionPool.
//     */
//    private ConnectionPool(){
//        try {
//            getDbProperties();
//        } catch (IOException ex) {
//            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        for(int i=0; i<maxconn; i++){
//            try {
//                Class.forName("com.mysql.jdbc.Driver").newInstance();
//                connarray[i] = DriverManager.getConnection(url, userName, password);
//            } catch (SQLException ex) {
//                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InstantiationException ex) {
//                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalAccessException ex) {
//                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//    /**
//     * This method chec and create connection manager.
//     * @return mcc - its link by object connection pool.
//     */
//    public static ConnectionPool getConnPool(){
//        conPool = (conPool==null?new ConnectionPool(): conPool);
//        return conPool;
//    }
//    /**
//     * This method give one connection in use.
//     * The method is blocked, when no available connection, and wait.
//     * Method return connection, when it is available.
//     * @return conn - connection, which can be used.
//     */
//    synchronized public Connection getOneConnection() {
//        Connection conn = null;
//        while (withdrowed == maxconn) {
//            try {
//                wait();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        withdrowed++;
//        for (int i = 0; i < maxconn; i++) {
//            if (connarray[i] != null) {
//                conn = connarray[i];
//                connarray[i] = null;
//            }break;
//        }
//        return conn;
//    }
//    /**
//     * This method change connection status in connection pool.
//     * It is write new available connectin in to array.
//     * @param conn - connection, witch was given in use.
//     */
//    synchronized public void releaseConnection(Connection conn){
//        withdrowed--;
//        for(int i=0; i<maxconn; i++){
//            if (connarray[i]==null){
//                connarray[i] = conn;
//                break;
//            }
//        }
//        notify();
//    }
//    /**
//     * This method close all connections.
//     * Array of connections becomes contain null values.
//     */
//    synchronized public void closeAll(){
//        for(int i=0; i<maxconn; i++){
//            if(connarray[i] == null){
//                try {
//                    wait(5000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try{
//                    connarray[i].close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, "Method closeAll activated", ex);
//                }
//                connarray[i]=null;
//            }
//        }
//    }
//}
//
