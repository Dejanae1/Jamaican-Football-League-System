
import java.util.Scanner;

public class MainMenu {

    private static final Scanner input = new Scanner(System.in);

    private static final FootballLeague league =
            new FootballLeague();

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("====================================");
        System.out.println("   KINGSTON PREMIER FOOTBALL LEAGUE");
        System.out.println("       MANAGEMENT SYSTEM");
        System.out.println("====================================");

        while (running) {

            displayMenu();

            int choice = readInteger("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerTeam();
                    break;

                case 2:
                    recordMatchResult();
                    break;

                case 3:
                    displayStandings();
                    break;

                case 4:
                    running = false;
                    System.out.println(
                        "Thank you for using the KPFL System."
                    );
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please select 1-4."
                    );
            }
        }

        input.close();
    }

    private static void displayMenu() {

        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Register Football Team");
        System.out.println("2. Record Match Result");
        System.out.println("3. View League Standings");
        System.out.println("4. Exit");
        System.out.println("===============================");
    }

    private static void registerTeam() {

        try {

            System.out.println("\n--- REGISTER TEAM ---");

            System.out.print("Enter Team ID: ");
            String id = input.nextLine();

            System.out.print("Enter Team Name: ");
            String name = input.nextLine();

            System.out.print("Enter Home Town: ");
            String town = input.nextLine();

            FootballTeam team =
                    new FootballTeam(id, name, town);

            league.addTeam(team);

            System.out.println(
                "Team successfully registered!"
            );

        } catch (Exception e) {

            System.out.println(
                "Unable to register team: " + e.getMessage()
            );
        }
    }

    private static void recordMatchResult() {

        try {

            System.out.println("\n--- RECORD MATCH RESULT ---");

            System.out.print("Enter Fixture ID: ");
            String fixtureId = input.nextLine();

            int homeScore =
                    readInteger("Enter Home Team Score: ");

            int awayScore =
                    readInteger("Enter Away Team Score: ");

            league.recordResult(
                fixtureId,
                homeScore,
                awayScore
            );

            System.out.println(
                "Match result successfully recorded!"
            );

        } catch (LeagueException e) {

            System.out.println(
                "Unable to record result: " + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                "An unexpected error occurred: " + e.getMessage()
            );
        }
    }

    private static void displayStandings() {

        System.out.println("\n--- LEAGUE STANDINGS ---");

        System.out.println(league.standingsReport());
    }

    private static int readInteger(String message) {

        while (true) {

            System.out.print(message);

            try {

                int value = Integer.parseInt(input.nextLine());

                if (value < 0) {
                    System.out.println(
                        "Please enter a non-negative number."
                    );
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid input. Enter a whole number."
                );
            }
        }
    }
}
