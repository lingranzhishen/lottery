package base;

public class Main {

    public static int finallyTest(){
        int a=0;
        try{
            a=1;
            return a;
        }finally {
            a=3;
            return a;
        }
    }

    public  static void main(String []args){
        System.out.println(finallyTest());
    }

}
