package server;

import logic.Math;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    private static Socket clientDialog;

    public ClientHandler(Socket client) {
        this.clientDialog = client;
    }

    //Создание ссылки math объетка logic.Math
    //При работе в методе run() будет происходить
    //инъекция данных в данный класс для вычисления результата
    Math math;

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // канал чтения из сокета
            in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            // канал записи в сокет (для HEADER)
            out = new PrintWriter(clientDialog.getOutputStream());
            // канал записи в сокет (для данных)
            dataOut = new BufferedOutputStream(clientDialog.getOutputStream());


            // первая строка запроса
            String input = in.readLine();
            // разбираем запрос по токенам
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // получаем HTTP метод от клиента
            // текст запроса от клиента
            fileRequested = parse.nextToken().toLowerCase();


            System.out.println("Method: " + method);
            System.out.println("Request: " + fileRequested.substring(1));
//  В данной точке кода добавить вхождение в файл и вычислить ответ.
            math = new Math(fileRequested.substring(1));
            math.Parser();
//  Тестовые данные. Вывод данных на монитор для проверки
            System.out.println("Данные с парсера:"+math.getResult());

            // пока поддерживаем GET and HEAD запросы
            if (method.equals("GET") || method.equals("HEAD")) {

                String content = getContentType(fileRequested);
                //Записать в переменную body полученное значение, а не значение, полученное в ходе запроса
                String numbers = "Input numbers: " + getBody(fileRequested.substring(1));
                String body = "Result of counting: " + getBodyResult(Double.toString(math.getResult()));
                String param = "The work was performed by: Markin Viktor Vladimirovich <br> Group Number: IKBO-01-18\n" +
                        "Individual task number: 1<br>\n" +
                        "Text of the individual task: \"Search for the maximum number\"<br>";
                //String body = Double.toString(math.getResult());

                if (method.equals("GET")) {
                    // GET method - возвращаем ответ

                    // шлем HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    //Длина ответа - эхо запроса без первого "/"
                    out.println("Content-length: " + body.length() + numbers.length() + param.length());
                    out.println(); // Пустая строка между headers и содержимым!
                    out.flush();
                    dataOut.write(param.getBytes(), 0, param.length());
                    dataOut.write(numbers.getBytes(), 0, numbers.length());
                    dataOut.write(body.getBytes(), 0, body.length());
                    //Отправляем тестовые данные
//                    dataOut.write();
                    dataOut.flush();
                }

                System.out.println("Ответ отослан: " + body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Возвращаем поддерживываемый  MIME Types
    private String getContentType(String fileRequested) {
//        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
//        else
//            return "text/plain";
    }

    private String getBody(String request) {
          return "<b>" + request + "</b>" + "<br>";
    }

    private String getBodyResult(String result){
        return "<b>" + result + "</b>";
    }
}
/*
Работу выполнил: Иванов Иван Иванович Номер группы: ИКБО-01-18
Номер индивидуального задания: 3
Текст индивидуального задания: «Создание калькулятора…»
 */