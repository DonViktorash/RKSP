package server;

import models.Student;
import utils.FreeMarker;
import utils.Hibernate;
import utils.ResultMaker;
import models.Book;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpReadWriter {
    private static final String FRONT_DIRECTORY_PATH =
            "C:/Users/alyau/Desktop/РКСП/Исходники/PR_10/server/src/main/static/";

    private static final String TYPE_HTML = "text/html";
    private static final String TYPE_PLAIN = "text/plain";

    private static final String ABOUT_PAGE_ADDRESS = "about";
    private static final String TASK_PAGE_ADDRESS  = "task";
    private static final String BOOKS_PAGE_ADDRESS = "books";

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

    private void sendPageAfterProcessing(String page, Map<String, Object> root){
        sendHttpResponse(
                Objects.requireNonNull(
                        FreeMarker.getHtml(page, root)
                ),
                TYPE_HTML
        );
    }

    private void analyzePost(String json) {
        Book newBook = new Gson().fromJson(json, Book.class);
        if (newBook.getId() == null) {
            Hibernate.applyTransaction(newBook, Hibernate.Operation.ADD);
        } else {
            if (newBook.getName() == null &&
                    newBook.getAuthor() == null &&
                    newBook.getYear() == null)
            {
                Hibernate.applyTransaction(newBook, Hibernate.Operation.DELETE);
            } else {
                Hibernate.applyTransaction(newBook, Hibernate.Operation.UPDATE);
            }
        }
        sendHttpResponse("done", TYPE_PLAIN);
    }

    private void analyzeAddress(String address) {
        if (address.isEmpty()) {
            address = ABOUT_PAGE_ADDRESS;
        }

        Map<String, Object> root = new HashMap<>();
        switch (address) {
            case ABOUT_PAGE_ADDRESS:
                Student.addToMap(root);
                sendPageAfterProcessing(address, root);
                return;
            case TASK_PAGE_ADDRESS:
                root.put("lastTaskRequest", SimpleHttpServer.lastTaskRequest);
                if (SimpleHttpServer.lastTaskRequest.isEmpty()) {
                    root.put("lastTaskResponse", "");
                } else {
                    root.put(
                            "lastTaskResponse",
                            new ResultMaker(SimpleHttpServer.lastTaskRequest).getResult()
                    );
                }
                sendPageAfterProcessing(address, root);
                return;
            case BOOKS_PAGE_ADDRESS:
                root.put("books", Hibernate.getListOfBooks());
                sendPageAfterProcessing(address, root);
                return;
            default:
                String expansion = getExpansion(address);
                if (expansion == null) {
                    // build task response
                    if (address.matches("task/.+")) {
                        Matcher matcher = getMatcher(address, "^task/(.+)");
                        if (matcher != null) {
                            String request = matcher.group(1);
                            SimpleHttpServer.lastTaskRequest = request;
                            sendHttpResponse(
                                    new ResultMaker(request).getResult(),
                                    TYPE_HTML
                            );
                        }
                        return;
                    }
                } else {
                    // manage some files like .js or .css
                    sendFile(address, "text/"+expansion);
                    return;
                }
        }
        analyzeAddress(ABOUT_PAGE_ADDRESS);
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
                            Paths.get(FRONT_DIRECTORY_PATH + address)
                    ));
            System.out.println(" > new request with existing file: /" + address);
            sendHttpResponse(file, type);
        } catch (IOException e) {
            System.err.println(" > new request with a non-existent address: /" + address);
        }
    }

    public void prepareResponse() {
        try {
            httpRequest = URLDecoder.decode(httpRequest, StandardCharsets.UTF_8.toString());
            if (getMatcher(httpRequest, "^GET") != null) {
                Matcher matcher = getMatcher(httpRequest, "^GET /(.*) HTTP");
                if (matcher != null) {
                    System.out.println();
                    String address = matcher.group(1);
                    analyzeAddress(address);
                } else System.err.println(" > invalid GET request");
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
