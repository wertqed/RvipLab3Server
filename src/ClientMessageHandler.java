import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by VBelov on 02.11.2017.
 */
public class ClientMessageHandler implements Runnable {
    private static Socket clientDialog;

    public ClientMessageHandler(Socket client) {
        clientDialog = client;
    }

    @Override
    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            while (!clientDialog.isClosed()) {
                System.out.println("Читаю данные с клиента");
                String msg = in.readUTF();
                System.out.println("Прочитали сообщение- " + msg);
                MessagesService.addMessage(new Message("Иванов Иван Иванович ",msg));
                if (msg.equalsIgnoreCase("quit")) {
                    Thread.sleep(3000);
                    break;
                }
                // чистим буфер
                out.flush();
            }
            System.out.println("Client disconnected");
            // закрываем сначала каналы сокета !
            in.close();
            out.close();
            // потом закрываем сокет общения с клиентом в нити
            clientDialog.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // потом закрываем сокет общения с клиентом в нити
        try {
            clientDialog.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}