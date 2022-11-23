import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5555)) {
            while (true) {
                System.out.println("Ожидание подключения...");
                Socket client = server.accept();
                System.out.println("Подключение установлено");
                InputStream input = client.getInputStream(); // получаеа вход
                OutputStream output = client.getOutputStream(); // получаем выход
                try {
                    while (true) {
                        byte first = (byte) input.read(); // замораживаем сервер до тех пор, пока клиент не отправит хотя бы один байт
                        byte[] bytes = new byte[input.available() + 1]; // создаем массив байтов полученной информации
                        bytes[0] = first; // кладем полученный первый байт
                        while (input.available() > 0) { // пока информация есть
                            bytes[bytes.length - input.available()] = (byte) input.read(); // получаем оставшиеся байты
                        }
                        String message = new String(bytes); // создаем строку из байтов
                        if (message.equals("exit")) { // если сообщение равно exit
                            System.out.println("Подключение разорвано");
                            break; // разрываем связь с клиентом
                        }
                        System.out.println("Получено сообщение: " + message);
                        output.write(bytes); // записываем полученные байты клиенту
                        output.flush();      // отправляем
                        System.out.println("Сообщение отправлено клиенту");
                    }
                } catch (SocketException e) {
                    System.out.println("Подключение разорвано");
                }
            }
        }
    }
}