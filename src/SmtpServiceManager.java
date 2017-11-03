import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by VBelov on 03.11.2017.
 */
public class SmtpServiceManager implements Runnable {
    @Override
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(100);
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (MessagesService.getSize() > 0) {
                try {
                    Future future = service.submit(new SMTPServer());
                    if (future.isDone()) {
                        System.out.println(future.get());
                    } else {
                        System.out.println("Занято");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
