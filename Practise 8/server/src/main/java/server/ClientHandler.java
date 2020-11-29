package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
//    static final String FILE_NOT_FOUND = "404.html";
//    static final String METHOD_NOT_SUPPORTED = "not_supported.html";

    private static Socket clientDialog;

    public ClientHandler(Socket client) {
        this.clientDialog = client;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested;

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

            // пока поддерживаем GET and HEAD запросы
            if (method.equals("GET")) {

                // По умолчнаию используется index.html
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }

                String content = getContentType(fileRequested);
                byte[] fileData = null;
                if (isHtml(fileRequested)) {
                    fileData = getHtml().getBytes();
                } else { //css, js or other
                    fileData = readFileData(fileRequested);
                }

                // шлем HTTP Headers
                out.println("HTTP/1.1 200 OK");
                out.println("Server: Java HTTP Server : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + content);
                //Длина ответа - эхо запроса без первого "/"
                out.println("Content-length: " + fileData.length);
                out.println(); // Пустая строка между headers и содержимым!
                out.flush();

                dataOut.write(fileData, 0, fileData.length);
                dataOut.flush();
                System.out.println("Ответ отослан");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }
        }
    }

    private boolean isHtml(String fileRequested) {
        return (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"));
    }

    // Возвращаем поддерживываемый  MIME Types
    private String getContentType(String fileRequested) {
        return isHtml(fileRequested) ? "text/html" : "text/plain";
    }

    //возвращаем содержимое файла
    private byte[] readFileData(String filePath) throws IOException {
        InputStream fileIn = null;
        int fileLength;
        byte[] fileData;

        try {
            fileIn = getClass().getResourceAsStream(filePath);
            if(null == fileIn) {
                throw new FileNotFoundException();
            }
            fileLength = fileIn.available();
            fileData = new byte[fileLength];
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    private String getHtml() {
        String html = "<html>";
        html += "<head>";
        html += "<script type=\"text/javascript\" src=\"jsfile.js\"></script> ";
        html += "<link rel=\"stylesheet\" type=\"text/css\" href=\"myfile.css\">";
        html += "<title>Prakticum3</title>";
        html += "</head>";
        html += "<body>";
        html += "<h1>List of students: </h1><br/>";
        html += prepareTable(DataManager.getInstance().getStudentList());
        html += "</body>";
        return html;
    }

    private String prepareTable(List<Student> list) {
        String test;
        String table = "<table class=\"content\">";
        table += "<tr><th>Student</th><th>Age</th><th/></tr>";

        for (Student student : list) {
            table += "<tr>" + //Создание строки
                     "<td>"+student.getFIO()+"</td>" + //Создание ячейки 1
                     "<td>"+student.getAge()+"</td>" + //Создание ячейки 2
                     "<td><input type=\"button\" value=\"Press\" onclick=\"ShowMessage('"+student.getFIO()+"')\">" + //Вывод
                    "</td>" + //Строка вывода данных + вывода на дисплей данных

                     "</tr>";
        }
        table += "</table>";

        return table;
    }



}