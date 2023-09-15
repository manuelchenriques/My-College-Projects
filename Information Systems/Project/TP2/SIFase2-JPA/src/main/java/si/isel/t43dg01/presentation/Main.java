package si.isel.t43dg01.presentation;


public class Main {
    public static void main(String[] args) {
        try {
            new JPAConsole();
        } catch(Exception e) {
            System.out.println("Something went wrong with the application.");
            System.out.println(e.getMessage());
        }
    }
}
