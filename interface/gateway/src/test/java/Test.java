

public class Test {

    public static void main(String[] args) {
        DeepCloneExample e1 = new DeepCloneExample();
        DeepCloneExample e2 = null;
        try {
            e2 = e1.clone();
//            System.out.println(e1.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        e1.set(2, 222);
        System.out.println(e2); // 2
        System.out.println(e1);
//        ShallowCloneExample e1 = new ShallowCloneExample();
//        ShallowCloneExample e2 = null;
//        try {
//            e2 = e1.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        e1.set(2, 222);
//        System.out.println(e1); // 222
//        System.out.println(e2);



    }


}
