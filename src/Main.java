import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static ExecutorService executeIt = Executors.newFixedThreadPool(1000);

    /**
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService smtpExec=Executors.newFixedThreadPool(1);
        smtpExec.execute(new SmtpServiceManager());
        // стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket server = new ServerSocket(3345)) {
            System.out.println("Start server listen 3345");
            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new ClientMessageHandler(client));
                System.out.print("Добавили коннект");
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
