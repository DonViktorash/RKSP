package logic;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello,world");
//        String text = "1,2,3,4,5";
        String text = "-10,-257,-3.1,-4000,-5";
        Math math = new Math(text);
        math.Parser();
        System.out.println("Result of work is:"+ math.getResult());
    }
}
