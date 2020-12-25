import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpReadWriter {

    Math math;

    public static final String SITE_DIRECTORY_PATH = "/Users/danivan/IdeaProjects/prac9/site/";
    public static final String ABOUT_PAGE_HTML = "about-page/about.html";
    public static final String TASK_PAGE_HTML = "task-page/task.html";
    public static final String BOOKS_PAGE_HTML = "books-page/books.html";

    public static final String TYPE_HTML = "text/html";
    public static final String TYPE_JSON = "application/json";
    public static final String TYPE_PLAIN = "text/plain";

    private final PrintWriter out;
    private String httpRequest;

    public HttpReadWriter(PrintWriter out, String httpRequest) {
        this.out = out;
        this.httpRequest = httpRequest;
    }

    private void sendHttpResponse(String content, String type) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: " + type);
        out.println("Content-Length: " + content.getBytes().length);
        out.print("\r\n");
        out.println(content);
    }

    private String getExpansion(String address) {
        Matcher matcher = getMatcher(address, "\\.([^\\.]*)$");
        if (matcher != null) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private void analyzePost(String json) {
        Book newBook = new Gson().fromJson(json, Book.class);
        if (newBook.getId() == null) {
            Hibernate.applyTransaction(newBook, Hibernate.Operation.ADD);
        } else {
            if (newBook.getName() == null &&
                    newBook.getAuthor() == null &&
                    newBook.getYear() == null) {
                Hibernate.applyTransaction(newBook, Hibernate.Operation.DELETE);
            } else {
                Hibernate.applyTransaction(newBook, Hibernate.Operation.UPDATE);
            }
        }
        sendHttpResponse("done", TYPE_PLAIN);
    }

    private void analyzeAddress(String address) {
        if (address.isEmpty()) {
            sendFile(ABOUT_PAGE_HTML, TYPE_HTML);
            return;
        }
        switch (address) {
            case "about":
                sendFile(ABOUT_PAGE_HTML, TYPE_HTML);
                return;
            case "task":
                sendFile(TASK_PAGE_HTML, TYPE_HTML);
                return;
            case "books":
                sendFile(BOOKS_PAGE_HTML, TYPE_HTML);
                return;
            default:
                String expansion = getExpansion(address);
                if (expansion == null) {
                    // get list of books
                    if (address.equals("books/list")) {
                        List<Book> books = Hibernate.getListOfBooks();
                        sendHttpResponse(new Gson().toJson(books), TYPE_JSON);
                        return;
                    }
                    // build task response
                    if (address.matches("task/.+")) {
                        Matcher matcher = getMatcher(address, "^task/(.+)");
                        if (matcher != null) {
                            String request = matcher.group(1);
                            math = new Math(request);
                            math.Parser();
                            sendHttpResponse(
                                    Double.toString(math.getResult()),
                                    TYPE_HTML
                            );
                        }
                        return;
                    }
                } else {
                    // manage some files like .js or .css
                    sendFile(address, "text/" + expansion);
                    return;
                }
        }
        sendFile(ABOUT_PAGE_HTML, TYPE_HTML);
    }

    private Matcher getMatcher(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return ((matcher.find()) ? matcher : null);
    }

    private void sendFile(String address, String type) {
        try {
            String file = new String(
                    Files.readAllBytes(
                            Paths.get(SITE_DIRECTORY_PATH + address)
                    ));
            System.out.println(" > new request with existing file: /" + address);
            sendHttpResponse(file, type);
        } catch (IOException e) {
            System.err.println(" > new request with a non-existent address: /" + address);
        }
    }

    public void sendResponse() {
        try {
            httpRequest = URLDecoder.decode(httpRequest, StandardCharsets.UTF_8.toString());
            if (getMatcher(httpRequest, "^GET") != null) {
                Matcher matcher = getMatcher(httpRequest, "^GET /(.*) HTTP");
                if (matcher != null) {
                    System.out.println();
                    String address = matcher.group(1);
                    analyzeAddress(address);
                } else System.err.println(" > invalid request");
            }
            if (getMatcher(httpRequest, "^POST") != null) {
                if (getMatcher(httpRequest, "^POST /books/post HTTP") != null) {
                    Matcher jsonMatcher = getMatcher(httpRequest, "\\{.*\\}$");
                    if (jsonMatcher != null) {
                        analyzePost(jsonMatcher.group());
                    }
                } else System.err.println(" > invalid POST request");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

