package com.hyb;


public class JavaTest {

    private int d=1;
    public void a(final int x){
        class b{
            public void c(){
                int y=x;
                System.out.println(y);
                System.out.println(d);
            }
        }
        new b().c();
    }

    public static void main(String[] args) {
        new JavaTest().a(1);
    }


}
