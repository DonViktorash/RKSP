package utils;

import utils.Result;

public class ResultMaker implements Result {

    private final String input;

    public ResultMaker(String input) {
        this.input = input;
    }

    @Override
    public String getResult() {
        String elementsList[] = input.split(", ", 0);
        String  result = "",
                result_ints = "",
                result_floats = "",
                result_other = "";

        for (int i = 0; i < elementsList.length; i++) {
            if (elementsList[i].matches("^[+-]?(0|[1-9][0-9]*)$")) {
                result_ints += " " + elementsList[i];
            } else if (elementsList[i].matches("^[0-9]*[,][0-9]+$")) {
                result_floats += " " + elementsList[i];
            } else {
                result_other += " " + elementsList[i];
            }
        }

        result += "Целые числа:" + result_ints + "<br>";
        result += "Вещественные числа:" + result_floats + "<br>";
        result += "Остальное:" + result_other;

        return result;
    }

}
