import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineExamSystem {
    private static Map<String, String> users = new HashMap<>();
    private static Map<String, String> profiles = new HashMap<>();
    private static String currentUser = null;
    private static boolean sessionActive = false;
    private static Timer timer;
    private static int timeLimit = 60; // 60 seconds for demo

    public static void main(String[] args) {
        users.put("user1", "password1");
        profiles.put("user1", "Profile of user1");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                login(scanner);
            } else if (choice == 2) {
                break;
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            sessionActive = true;
            System.out.println("Login successful!");
            userMenu(scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void userMenu(Scanner scanner) {
        while (sessionActive) {
            System.out.println("\n1. Update Profile");
            System.out.println("2. Update Password");
            System.out.println("3. Take Exam");
            System.out.println("4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                updateProfile(scanner);
            } else if (choice == 2) {
                updatePassword(scanner);
            } else if (choice == 3) {
                takeExam(scanner);
            } else if (choice == 4) {
                logout();
            }
        }
    }

    private static void updateProfile(Scanner scanner) {
        System.out.println("Current profile: " + profiles.get(currentUser));
        System.out.print("New profile details: ");
        String newProfile = scanner.nextLine();
        profiles.put(currentUser, newProfile);
        System.out.println("Profile updated.");
    }

    private static void updatePassword(Scanner scanner) {
        System.out.print("Current password: ");
        String currentPassword = scanner.nextLine();
        if (users.get(currentUser).equals(currentPassword)) {
            System.out.print("New password: ");
            String newPassword = scanner.nextLine();
            users.put(currentUser, newPassword);
            System.out.println("Password updated.");
        } else {
            System.out.println("Incorrect current password.");
        }
    }

    private static void takeExam(Scanner scanner) {
        String[] questions = {
            "1. What is the capital of France?\n a) Paris\n b) London\n c) Rome\n d) Berlin",
            "2. What is 2 + 2?\n a) 3\n b) 4\n c) 5\n d) 6",
            "3. What is the capital of Japan?\n a) Beijing\n b) Tokyo\n c) Seoul\n d) Bangkok"
        };
        char[] answers = {'a', 'b', 'b'};
        char[] userAnswers = new char[questions.length];

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nTime's up! Auto-submitting your answers.");
                evaluateExam(userAnswers, answers);
                timer.cancel();
                userMenu(scanner);
            }
        };

        timer = new Timer();
        timer.schedule(task, timeLimit * 1000);

        System.out.println("You have " + timeLimit + " seconds to complete the exam.");
        for (int i = 0; i < questions.length; i++) {
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            userAnswers[i] = scanner.next().charAt(0);
        }

        timer.cancel();
        evaluateExam(userAnswers, answers);
    }

    private static void evaluateExam(char[] userAnswers, char[] correctAnswers) {
        int score = 0;
        for (int i = 0; i < correctAnswers.length; i++) {
            if (userAnswers[i] == correctAnswers[i]) {
                score++;
            }
        }
        System.out.println("Your score: " + score + " out of " + correctAnswers.length);
    }

    private static void logout() {
        currentUser = null;
        sessionActive = false;
        System.out.println("Logged out.");
    }
}
