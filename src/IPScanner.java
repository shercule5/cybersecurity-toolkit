import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.*;

public class IPScanner {
    public static void scanHost(String host, int portStart, int portEnd) {
        System.out.println("Resolving " + host + "...");
        try {
            InetAddress addr = InetAddress.getByName(host);
            String ip = addr.getHostAddress();
            System.out.println("Resolved: " + ip);

            boolean reachable = addr.isReachable(2000);
            System.out.println("Reachable: " + reachable);

            ExecutorService pool = Executors.newFixedThreadPool(50);
            CompletionService<Integer> ecs = new ExecutorCompletionService<>(pool);

            for (int p = portStart; p <= portEnd; p++) {
                final int port = p;
                ecs.submit(() -> {
                    try (Socket s = new Socket()) {
                        s.connect(new java.net.InetSocketAddress(ip, port), 300);
                        return port;
                    } catch (IOException ex) {
                        return -1;
                    }
                });
            }

            int total = portEnd - portStart + 1;
            for (int i = 0; i < total; i++) {
                int result = ecs.take().get();
                if (result > 0) {
                    System.out.println("Port OPEN: " + result);
                }
            }

            pool.shutdownNow();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
