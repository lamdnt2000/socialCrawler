public class Demo {
    public static void main(String[] args) {
        String a = "a";
        Test(a);
        System.out.println(a);
    }
    public static void Test(String a){
        ThreadLocal<String> b = new ThreadLocal<>();
        b.set(a);
        System.out.println(a);
        b.remove();
    }
}
