public class stuff {

    public static void main() {
        System.out.println(l(4));
    }

    public static int l(int n) {
        int result = n;
        int count = 0;
        while (count < result) {
            n += n;
            count++;
        }
        return n;
    }

}
