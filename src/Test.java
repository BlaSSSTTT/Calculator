public class Test {
    public static void main(String[] args) {
        //String[] arr = {"((a*3)+5)*3+a^a+a+1","a=2"}; //40
        //String[] arr = {"sin(3.14)^-2"}; //394237.30
        //String[] arr = {"-2*2^2+3*1/-2", "x=2"}; //-9,5
        //String[] arr = {"-a-10","a=2"}; //-12
        //String[] arr = {"5*7-5*7/9+5/7/3","a=2"}; //8
        //String[] arr = {"10+5-a","a=-2"}; //17
        //String[] arr = {"15+a/a+a","a=2"}; //18
        //String[] arr = {"sin(a)","a=2"}; //0.909
        //String[] arr = {"sin(cos(a))","a=2"}; // -0.404
        //String[] arr = {"sqrt(4)+log10(1000)"}; //5
        //String[] arr = {"(a+b)*(a-b)","a=3","b=2"};//5
        //String[] arr = {"3.+5."};//8
        //String[] arr = {"a^a^a","a=2"};//16
        //String[] arr = {"a/0","a=2"};
        String[] arr = {"()(()()","a=2"};
        //String[] arr = {"a**b","a=2","b=6"};
        //String[] arr = {"log2(a)","a=4"};
        Calculator.main(arr);

    }
}
