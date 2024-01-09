import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * The program which calculating formula given in parameters in main
 * First parameter is formula, next are variables which is in formula
 */
public class Calculator {
    public static void main(String[] args) {
        String formula = args[0].replaceAll("\\s","");
        HashMap<String,Double> values = getValues(args);
        System.out.println(calculate(formula, values));
    }
    /**
     * The function to read all parameters
     * @param args The array of strings where 1.. elements are parameters
     * @return Hash map where kay is parameter and value is number
     */
    private static HashMap<String, Double> getValues(String[] args) {
        HashMap<String, Double> map = new HashMap<>();
        for (int i=1;i<args.length;i++) {
            args[i] = args[i].replaceAll("\\s", "");
            String[] strs = args[i].split("=");
            map.put(strs[0], Double.parseDouble(strs[1]));
        }
        return map;
    }
    /**
     * The function check if string is number
     * @param string The string to check
     * @return true if is, false if isn`t
     */
    private static boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * The function check if string is sing
     * @param s The string to check
     * @return true if is, false if isn`t
     */
    private static boolean isSign(String s) {
        return s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*") || s.equals("^") || s.equals("(");
    }

    /**
     * The function check if s1 has higher priority then s2
     * @return true if is, false if isn`t
     */
    private static boolean higherPriority(String s1, String s2) {

        if(isUnoOperation(s1)){
            return true;
        }
        if (s1.equals("^") && !isUnoOperation(s2)&&!s2.equals("^")) {
            return true;
        }
        return (s1.equals("*") || s1.equals("/")) && (s2.equals("+") || s2.equals("-"));
    }

    /**
     * The function check if string is unary operation
     *  @param s The string to check
     *  @return true if is, false if isn`t
     */
    private static boolean isUnoOperation(String s) {
        return s.equals("sqrt") || s.equals("sin") || s.equals("cos") || s.equals("tan") || s.equals("atan") || s.equals("log10") || s.equals("log2")||
                s.equals("-sqrt") || s.equals("-sin") || s.equals("-cos") || s.equals("-tan") || s.equals("-atan") || s.equals("-log10") || s.equals("-log2") ;
    }
    /**
     * The function add minus to number in string
     * @param s The number to add
     * @return The number with minus
     */
    private static String addUnaryMinus(String s) {
        return "-"+s;
    }

    /**
     * The function which do dual operation
     * @param first The first number
     * @param second The second number
     * @param sign The operation which must be done
     * @return The result
     */
    private static double doDualOperation(String first,String second, String sign) throws Exception {
        double c;
        double a = Double.parseDouble(first);
        double b = Double.parseDouble(second);
        if(sign.equals("/")&&b==0){
            throw new Exception("Division by 0");
        }
        c = switch (sign) {
            case "+" -> a + b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "-" -> a - b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Invalid variable format: " + sign);
        };
        return c;
    }

    /**
     * The function which do unary operation
     * @param number The number
     * @param sign The operation to do
     * @return The result
     */
    private static double doUnoOperation(String number, String sign) throws Exception {
        double c;
        double a = Double.parseDouble(number);
        boolean isNegative = false;
        if(sign.startsWith("-")){
            isNegative = true;
            sign  = sign.replace("-","");
        }
        if((sign.equals("log10")||sign.equals("log2")||sign.equals("sqrt"))&&a<0){
            throw new Exception("Can`t "+sign+" to negative number");
        }
        c = switch (sign) {
            case "sin" -> Math.sin(a);
            case "cos" -> Math.cos(a);
            case "log10" -> Math.log10(a);
            case "log2" -> Math.log(a) / Math.log(2);
            case "tan" -> Math.tan(a);
            case "atan" -> Math.atan(a);
            case "sqrt" -> Math.sqrt(a);
            default -> throw new IllegalArgumentException("Invalid variable format: " + sign);
        };
        if(isNegative){
            return -c;
        }else {
            return c;
        }
    }

    /**
     * The function which solve equation given in reverse polish expression
     * @param reversePolishExpression The equation in reverse polish expression
     * @return The result
     */
    private static String solve(ArrayList<String> reversePolishExpression) {
        try {
            for (int j = 1; j < reversePolishExpression.size(); j++) {
                if (!isNumber(reversePolishExpression.get(j))) {
                    //peek sign
                    String sign = reversePolishExpression.get(j);
                    double c;
                    if (isUnoOperation(sign)) {
                        c = doUnoOperation(reversePolishExpression.get(j - 1), sign);
                        //replace sign and number by result
                        reversePolishExpression.remove(j);
                        reversePolishExpression.remove(j - 1);
                        reversePolishExpression.add(j - 1, String.valueOf(c));
                        j--;
                    } else {
                        c = doDualOperation(reversePolishExpression.get(j - 2), reversePolishExpression.get(j - 1), sign);
                        //replace sign and numbers by result
                        reversePolishExpression.remove(j);
                        reversePolishExpression.remove(j - 1);
                        reversePolishExpression.remove(j - 2);
                        reversePolishExpression.add(j - 2, String.valueOf(c));
                        j -= 2;
                    }
                }
            }
        }catch (IndexOutOfBoundsException e){
            System.out.print("Error, incorrect input\nExit code:");
            return "-1";
        }catch (Exception e) {
            System.out.print("Error: "+e.getMessage()+"\nExit code:");
            return "-1";
        }
        return reversePolishExpression.get(0);
    }

    /**
     * The function which replace all variables in formula by numbers
     */
    private static void replaceVariables(ArrayList<String> formula, HashMap<String, Double> variables){
        for(int i=0;i<formula.size();i++){
            String token = formula.get(i);
            boolean isNegative = false;
            if(token.startsWith("-")){
                token = token.replace("-","");
                isNegative = true;
            }
            if(variables.containsKey(token)){
                if(isNegative){
                    formula.set(i, String.valueOf(-variables.get(token)));
                }else {
                    formula.set(i, String.valueOf(variables.get(token)));
                }
            }
        }
    }

    /**
     * The function which solve equations
     */
    private static double calculate(String formula, HashMap<String, Double> variables) {
        try {
            ArrayList<String> reversePolishExpression = new ArrayList<>();
            Stack<String> signs = new Stack<>();
            //Split the string into sub-string by characters /*-+^()
            String[] tokens = formula.split("\\s*(?=[-+*/^()])|(?<=[-+*/^()])\\s*");
            boolean needReverse = false;
            for (int i = 0; i < tokens.length; i++) {
                //if token is number
                if (isNumber(tokens[i])) {
                    //if was unary minus
                    if (needReverse) {
                        reversePolishExpression.add(addUnaryMinus(tokens[i]));
                        needReverse = false;
                    } else {
                        reversePolishExpression.add(tokens[i]);
                    }
                } else if(variables.containsKey(tokens[i])) {
                    if (needReverse) {
                        reversePolishExpression.add(addUnaryMinus(tokens[i]));
                        needReverse = false;
                    } else {
                        reversePolishExpression.add(tokens[i]);
                    }
                }else if (tokens[i].equals("(")) {
                    signs.add(tokens[i]);
                } else if (tokens[i].equals(")")) {
                    //add all sign between () into expression and check if formula correct
                    boolean isError = true;
                    while (!signs.empty()) {
                        String str = signs.pop();
                        if (str.equals("(")) {
                            isError = false;
                            break;
                        }
                        reversePolishExpression.add(str);
                    }
                    if (isError) {
                        throw new Exception("Error, incorrect input formula, bad spaced parentheses\nExit code:");
                    }
                } else {
                    //if unary minus
                    if (tokens[i].equals("-") && (i == 0 || isSign(tokens[i - 1]))) {
                        needReverse = true;
                    } else {
                        if(needReverse && (isUnoOperation(tokens[i]))){
                            tokens[i] = addUnaryMinus(tokens[i]);
                            needReverse = false;
                        }
                        if (signs.empty()) {
                            signs.add(tokens[i]);
                        }else {

                            String str = signs.peek();
                            //check and add sign to correct position
                            if (str.equals("(") || higherPriority(tokens[i], str)) {
                                signs.add(tokens[i]);
                            } else {
                                do {
                                    str = signs.pop();
                                    reversePolishExpression.add(str);
                                } while (!signs.empty()&&!signs.peek().equals("(")&&!higherPriority(tokens[i], signs.peek()));
                                signs.add(tokens[i]);

                            }
                        }
                    }
                }

            }
            //add all signs from stack to expression
            while (!signs.empty()) {
                if (signs.peek().equals("(")) {
                    throw new Exception("Error, incorrect input formula, bad spaced parentheses\nExit code:");
                }
                reversePolishExpression.add(signs.pop());
            }
            //replace variables in reverse polish expression
            replaceVariables(reversePolishExpression, variables);
            //solve created expression
            String answer = solve(reversePolishExpression);

            return Double.parseDouble(answer);
        }catch (Exception e){
            System.out.print("Error: "+e.getMessage()+"\nExit code:");
            return -1;
        }
    }
}
