import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.text.NumberFormat;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

// https://stackoverflow.com/questions/15159453/how-can-i-start-a-server-in-a-background-thread-and-know-that-the-server-did-not

// Changes the password of the database from trades to test
public class Main
{
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final String TASKLIST = "wmic path win32_process get ProcessID,Commandline";
    // private static final String WinUser = "NAM\\111wastu05";
    //private static final String WinPass = "Welcome1";
    private static final String WinUser = System.getenv("WinUser");
    private static final String WinPass = System.getenv("WinPass");

    private static void menu() {
        System.err.println("0. Change Password - requires DBrootPW, DBuserName, DBuserBadPW variables to be set\n" +
                "1. Check Password\n" +
                "2. Reset Password - requires DBrootPW, DBuserName, DBuserBadPW variables to be set\n" +
                "3. Service Clash - requires the port number to clash with, you must stop the service first and the service name\n" +
                "4. Delay Trades\n" +
                "5. Fill Logs\n" +
                "6. Duplicate Entries in Database\n" +
                "7. Set wrong price in database\n" +
                "8. Start lots of process to slow system\n" +
                "9. Stop/Start a service");
    }

    public static void main(String[] args)
    {
        String runAttribute ="";
        String runAttribute2 ="";
        if ( args.length <= 0 ) {
            System.err.println("You need to specify one of the following;");
            menu();
            System.exit(1);
        }

        if ( args.length > 1 ) {
            runAttribute = args[1];
        }

        if ( args.length > 2 ) {
            runAttribute2 = args[2];
        }
        //String rootPW = "my-secret-pw";
        String rootPW = System.getenv("DBrootPW");
        String userName = System.getenv("DBuserName");
        String userBadPW = System.getenv("DBuserBadPW");
        String userPW = System.getenv("DBuserPW");
        int inputValue = Integer.parseInt(args[0]);

        switch (inputValue) {
            case 0:
                // Set the password so that it breaks the trade app
                ChangePassword(rootPW,userName,userBadPW);
                break;
            case 1:
                // Next command checks if the user has resolved the issue, so normal user password
                CheckPassword(userName,userPW);
                break;
            case 2:
                // Reset the user password to trades if the user didn't solve the problem
                ResetPassword(rootPW,userName,userPW);
                break;
            case 3:
                // 8080 = App and 9990 = App JMX - This one must be run in background with nohup or windows equivalent
                // If you can you can use 3306 (mysql) or 61616 (ActiveMQ)
                ServiceClash(Integer.parseInt(runAttribute),runAttribute2);
                break;
            case 4:
                // Sets a delay in the database by adding a trigger
                DelayTrades(Integer.parseInt(runAttribute));
                break;
            case 5:
                fillLogs();
                break;
            case 6:
                duplicateEntry();
                break;
            case 7:
                wrongPrice(Integer.parseInt(runAttribute));
                break;
            case 8:
                slowDownProcesses(Integer.parseInt(runAttribute));
                break;
            case 9:
                //controlService(runAttribute);
                break;
            default:
                System.err.println("That argument is not valid.");
                System.err.println("Options are;");
                menu();
                break;
        }
    }

    public static boolean isProcessRunning(String serviceName) throws Exception {
        // Check if Windows

        if (OS.indexOf("win") >= 0) {
            Process p = Runtime.getRuntime().exec(TASKLIST);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(serviceName)) {
                    String[] myarray = line.split(" ");
                    procs.linuxProcessID=myarray[myarray.length-1];
                    return true;
                }
            }
        } else {
            // Code for Linux
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ps -ef | grep trade | grep -v grep"});
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(serviceName)) {
                        String[] myarray = line.split("  *",-1);
                        procs.linuxProcessID=myarray[1];
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static void killProcess(String serviceName) throws Exception {
        String KILL;
        if (OS.indexOf("win") >= 0) {
            KILL = "taskkill /U "+WinUser+" /P "+WinPass+" /F /PID ";
        } else {
            KILL = "kill -9 ";
        }

        Runtime.getRuntime().exec(KILL + procs.linuxProcessID);
        procs.linuxProcessID = null;
        System.gc();
    }

    public static void stopService(String serviceName) {
        String getServiceCMD;
        if (OS.indexOf("win") >= 0) {
            getServiceCMD = "net stop " + serviceName;
        } else {
            getServiceCMD = "sudo service " + serviceName + " stop";
        }
        try {
            Runtime.getRuntime().exec(getServiceCMD);
        } catch (Exception e) {
            System.err.println("Unable to stop service "+serviceName);
            e.printStackTrace();
        }
    }

     private static void ChangePassword(String rootPassword, String userName, String userPassword) {
        try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.server:3306/mysql?useSSL=false", "root", rootPassword);
                //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "my-secret-pw");
                String sql = "SET PASSWORD FOR '"+userName+"'@'%' = PASSWORD('"+userPassword+"')";
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                stmt.close();
                conn.close();
                System.out.println("Password is changed successfully!");
        }
        catch(Exception ex){
                ex.printStackTrace();
        }
    }

    private static void CheckPassword(String userName, String userPassword) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.server:3306/mysql?useSSL=false", userName, userPassword);
            String sql = "SELECT * FROM user";
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
            stmt.close();
            conn.close();
            System.out.println("Logon Successful");
        }
        catch(Exception ex){
            System.out.println("Failed to log in to database");
            ex.printStackTrace();
        }
    }

    private static void ResetPassword(String rootPassword, String userName, String userPassword) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.server:3306/mysql?useSSL=false", "root", rootPassword);
            String sql = "SET PASSWORD FOR '"+userName+"'@'%' = PASSWORD('"+userPassword+"')";
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
            stmt.close();
            conn.close();
            System.out.println("Password is changed successfully!");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void ServiceClash(int PortNumber, String serviceName) {
        // Ensure that app has stopped java or trade-app-0.1.0.jar
        if ( serviceName == "trade-app" ) {
            String processName = "trade-app";
            try {
                if (isProcessRunning(processName))
                    killProcess(processName);
            } catch (Exception e) {
                System.out.println("Trade Application is not running or cannot be stopped");
            }
        } else {
            stopService(serviceName);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(PortNumber);
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private static void DelayTrades(int delaySecs) {
        int counter = 0;
        while (true) {
            try {
                Connection conn = null;
                Class.forName("com.mysql.jdbc.Driver");
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
                String sql = "INSERT INTO Trades (transid,stock,ptime,price,volume,buysell,state,stime) VALUES('20170712020500722-01','FTSE.LLOY','2017-07-12 02:05:00',95.7974,27000,'B','A','2017-07-12 02:05:01')";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                try {
                    Thread.sleep(delaySecs*1000);
                    counter++;
                    System.out.println("Round again "+ (Integer.toString(counter)));
                } catch (InterruptedException ex) {
                    System.out.println("Can't sleep");
                }
                try {
                    conn.rollback();
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    System.out.println("Failed to put something back");
                }
            } catch (Exception ex) {
                System.out.println("Failed to work with database");
                ex.printStackTrace();
            }
        }
    }

    private static void fillLogs() {
        Path root;
        NumberFormat nf = NumberFormat.getNumberInstance();
        if (OS.indexOf("win") >= 0) {
            root = Paths.get("C:\\");
        } else {
            root = Paths.get("/");
        }
        // for (Path root : FileSystems.getDefault().getRootDirectories()) {
            try {
                FileStore store = Files.getFileStore(root);
                //System.out.println("available=" + nf.format(store.getUsableSpace()));

                // For Linux
                String cmd;
                if (OS.indexOf("win") >= 0) {
                    //cmd = "fsutil file createnew C:\\temp\\alogfile.txt 2000000000";
                    cmd = "fsutil file createnew C:\\temp\\alogfile.txt "+nf.format(store.getUsableSpace());
                } else {
                    cmd = "dd if=/dev/zero of=/tmp/.alogfile bs="+nf.format(store.getUsableSpace());
                }
                Process p = null;
                try {
                    p = Runtime.getRuntime().exec(cmd);
                    try {
                        p.waitFor();
                        int exitStatus = p.exitValue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
            //System.out.println("error querying space: " + e.toString());
        }
    }

    private static void duplicateEntry() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.server:3306/mysql?useSSL=false", "root", "my-secret-pw");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "my-secret-pw");
            String sql = "DELIMITER //\n" +
                    "CREATE TRIGGER dupTrade\n" +
                    "BEFORE INSERT ON Trades\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "set @maxid=(select max(id) from Trades);\n" +
                    "INSERT INTO Trades (transid,stock,ptime,price,volume,buysell,state,stime)\n" +
                    "SELECT CONCAT(SUBSTRING(transid,1,LOCATE('-',transid)),'01') as transid, stock, ptime, price, volume, buysell, state, stime\n" +
                    "FROM Trades\n" +
                    "WHERE id=@maxid;\n" +
                    "END\n" +
                    "//\n" +
                    "DELIMITER ;";
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
            stmt.close();
            conn.close();
            System.out.println("Update successful!");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void wrongPrice(int bySize) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql.server:3306/mysql?useSSL=false", "root", "my-secret-pw");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "my-secret-pw");
            String sql = "DELIMITER //\n" +
                    "CREATE TRIGGER Trade100\n" +
                    "BEFORE UPDATE ON Trades\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "SET new.price=(select new.price*"+bySize+" from Trades where id=new.id);\n" +
                    "END\n" +
                    "//\n" +
                    "DELIMITER ;";
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
            stmt.close();
            conn.close();
            System.out.println("Update successful!");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void slowDownProcesses(int howMany) {
        ExecutorService exec = Executors.newFixedThreadPool(howMany);
        for (int numProcesses = 0; numProcesses < howMany; numProcesses++) {
            exec.execute(new runProcesses());
        }
    }
}

class runProcesses implements Runnable{
    private static String OS = System.getProperty("os.name").toLowerCase();
    @Override
    public void run() {
        String cmd;
        do {
            if (OS.indexOf("win") >= 0) {
                cmd = "cmd.exe";
            } else {
                cmd = "find /";
            }
            Process p;
            try {
                p=Runtime.getRuntime().exec("cmd.exe");
                //p = Runtime.getRuntime().exec("dir /ad /b /s c:\\");
                //p.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (!Thread.interrupted());
    }
}