import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5555)) {
            Scanner sc = new Scanner(System.in);
            InputStream input = socket.getInputStream(); // получаем ввод
            OutputStream output = socket.getOutputStream(); // получаем вывод
            while (true) {
                System.out.print("Введите сообщение: ");
                String message = sc.nextLine(); // просим пользо
                output.write(message.getBytes()); // создаем серверу сообщение
                output.flush();                   // отправляем
                if (message.equals("exit")) { // если сообщение равно exit
                    System.out.println("Завершение работы");
                    System.exit(0); // завершаем клиент
                }
                byte first = (byte) input.read(); // читаем первый байт. клиент заморозится до тех пор, пока сервер не отправит хотя бы один байт
                byte[] bytes = new byte[input.available() + 1]; // создаем массив полученных байтов от сервера
                bytes[0] = first; // в первый элемент кладем полученный первый байт
                while (input.available() > 0) { // читаем все байты, пока они есть
                    bytes[bytes.length - input.available()] = (byte) input.read(); // кладем байт в массив
                }
                String serverMessage = new String(bytes); // создаем строку из массива байтов
                System.out.println("Сервер переслал ваше сообщение: " + serverMessage);
            }
        }
    }
}