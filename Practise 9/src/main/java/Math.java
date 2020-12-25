import java.util.ArrayList;
import java.util.List;

public class Math implements Result{
    Double max;
    String text;
    double[] nums;
    List<Double> lineOfNums;
    private int i;

    /*
    Создание метода для возвращения результата.
    Входных параметров: нет
    Выходные данные: результат вычислений.
    */
    @Override
    public double getResult() {
        //Вернуть параметр - результат вычислений.
        return max;
    }

    //Конструктор для строки
    public Math(String text) {
        this.text = text;
        System.out.println("Конструктор для строки:"+this.text);
    }

    public void Parser(){

        ArrayList<Double>list = new ArrayList<>();

        //Массив чисел
        String[] temp = text.split(",");

        int length = temp.length;

        //Парсим числа в целочисленные и добавление в лист
        for(int j = 0; j < temp.length; j++){
//            i = Integer.parseInt(temp[j]);
            double k = Double.parseDouble(temp[j]);
            list.add(k);
        }
        //Отправляем в метод сортировки
        //Без ожидания
        maxNumOfList(list);
    }

    public void maxNumOfList(ArrayList<Double>list){
        max = list.get(0);
        for (int i = 1; i < list.size(); i++){
            if(list.get(i) > max){
                max = list.get(i);
            }
        }
    }
}
