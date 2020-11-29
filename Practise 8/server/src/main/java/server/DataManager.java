package server;

import java.util.ArrayList;
import java.util.List;

//Класс, созданный по шаблону Singleton - всегда имеет один экземпляр класса
public class DataManager {
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private List<Student> studentList = new ArrayList<>();

    private DataManager() {
        init();
    }

    private void init() {
        studentList.add(new Student("Ivanov Ivan", 22));
        studentList.add(new Student("Petrov Petr", 21));
        studentList.add(new Student("Sidorov Pavel", 23));
    }

    public List<Student> getStudentList() {
        return studentList;
    }


}