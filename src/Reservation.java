import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/* What this System does
1. Users can input the number of people in their reservation
2. Users can input the reservation time hour and minute and when the reservation was made
3. Users are added to a wait list after the first 5 reservations
4. Gives expected wait time for next user that wants a reservation
5. Lets users modify their group size and reservation time
6. Allows users to cancel their reservation
7. Writes reservation details on a file so that data can persist
 */

public class Reservation {

    int numOfPeople;
    int reservationTimeHour;
    int reservationTimeMin;
    int reservationMadeHour;
    int reservationMadeMin;

    public Reservation(int numOfPeople, int reservationTimeHour, int reservationTimeMin, int reservationMadeHour, int reservationMadeMin) {
        this.numOfPeople = numOfPeople;
        this.reservationTimeHour = reservationTimeHour;
        this.reservationTimeMin = reservationTimeMin;
        this.reservationMadeHour = reservationMadeHour;
        this.reservationMadeMin = reservationMadeMin;
    }

    public String toString() {
        return ("Reservation for " + numOfPeople + " people at " + reservationTimeHour + ":" + String.format("%02d", reservationTimeMin) + ", made at " + reservationMadeHour + ":" + String.format("%02d", reservationMadeMin));
    }

    static Reservation[] allReservations = new Reservation[5];
    static int numReservations = 0;
    static ArrayList<Reservation> waitlist = new ArrayList<>();

    public static void addReservation(Reservation newReservation) {
        if (numReservations == allReservations.length) {
            System.out.println("You will be added to the waitlist.");
            waitlist.add(newReservation);
            int waitTime = waitlist.size() * 10;
            System.out.println("Estimated wait time: " + waitTime + " minutes");
        } else {
            for (int i = 0; i < allReservations.length; i++) {
                if (allReservations[i] == null) {
                    allReservations[i] = newReservation;
                    numReservations++;
                    return;
                }
            }
        }
    }

    public static int getReservationIndex(int numOfPeople, int reservationTimeHour, int reservationTimeMin) {
        for (int i = 0; i < allReservations.length; i++) {
            if (allReservations[i] != null) {
                if (allReservations[i].numOfPeople == numOfPeople && allReservations[i].reservationTimeHour == reservationTimeHour && allReservations[i].reservationTimeMin == reservationTimeMin) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static String displayReservations() {
        String result = "Reservations: ";
        for (int i = 0; i < numReservations; i++) {
            result += allReservations[i].toString() + ". ";
        }
        result += "\n" + "Waitlist: " + waitlist;
        return result;
    }

    public static void sortReservations() {
        for (int i = 0; i < numReservations; i++) {
            Reservation temp = allReservations[i];
            for (int j = i + 1; j < numReservations; j++) {
                Reservation temp2 = allReservations[j];
                if (temp.reservationMadeHour > temp2.reservationMadeHour) {
                    allReservations[i] = temp2;
                    allReservations[j] = temp;
                } else if (temp.reservationMadeHour == temp2.reservationMadeHour) {
                    if (temp.reservationMadeMin > temp2.reservationMadeMin) {
                        allReservations[i] = temp2;
                        allReservations[j] = temp;
                    }
                }
            }
        }
    }

    public static void saveToFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        PrintWriter writer = new PrintWriter(f);
        for (int i = 0; i < allReservations.length; i++) {
            Reservation r = allReservations[i];
            if (allReservations[i] != null) {
                writer.println("R " + r.numOfPeople + " " + r.reservationTimeHour + " " + r.reservationTimeMin + " " + r.reservationMadeHour + " " + r.reservationMadeMin);
            }
        }
        for (int i = 0; i < waitlist.size(); i++) {
            Reservation w = waitlist.get(i);
            writer.println("W " + w.numOfPeople + " " + w.reservationTimeHour + " " + w.reservationTimeMin + " " + w.reservationMadeHour + " " + w.reservationMadeMin);
        }
        writer.close();
    }

    public static void Load(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        Scanner s = new Scanner(f);
        allReservations = new Reservation[5];
        numReservations = 0;
        waitlist = new ArrayList<>();

        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] tokens = line.split(" ");
            if (tokens[0].equals("R")) {
                int numOfPeople = Integer.parseInt(tokens[1]);
                int reservationTimeHour = Integer.parseInt(tokens[2]);
                int reservationTimeMin = Integer.parseInt(tokens[3]);
                int reservationMadeHour = Integer.parseInt(tokens[4]);
                int reservationMadeMin = Integer.parseInt(tokens[5]);
                Reservation r = new Reservation(numOfPeople, reservationTimeHour, reservationTimeMin, reservationMadeHour, reservationMadeMin);
                addReservation(r);
            } else if (tokens[0].equals("W")) {
                int numOfPeople = Integer.parseInt(tokens[1]);
                int reservationTimeHour = Integer.parseInt(tokens[2]);
                int reservationTimeMin = Integer.parseInt(tokens[3]);
                int reservationMadeHour = Integer.parseInt(tokens[4]);
                int reservationMadeMin = Integer.parseInt(tokens[5]);
                Reservation w = new Reservation(numOfPeople, reservationTimeHour, reservationTimeMin, reservationMadeHour, reservationMadeMin);
                waitlist.add(w);
            }
        }
        s.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 'e' to enter a new reservation");
            System.out.println("Enter 'm' to modify a reservation");
            System.out.println("Enter 'c' to cancel a reservation");
            System.out.println("Enter 's' to save all reservations");
            System.out.println("Enter 'l' to load all reservations");
            System.out.println("Enter 'p' to print all reservations and waitlist");
            System.out.println("Enter 'q' to quit adding reservations");
            System.out.print("Enter your option: ");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            } else if (input.equals("c")) {
                System.out.print("Enter the number of people in your reservation: ");
                int numOfPeople = scanner.nextInt();
                System.out.print("Enter the reservation hour (in 24 hour clock): ");
                int reservationTimeHour = scanner.nextInt();
                System.out.print("Enter the reservation minute: ");
                int reservationTimeMin = scanner.nextInt();
                scanner.nextLine();
                int index = getReservationIndex(numOfPeople, reservationTimeHour, reservationTimeMin);
                if (index == -1) {
                    System.out.println("No reservation with that time");
                } else {
                    allReservations[index] = null;
                    if (waitlist.size() > 0) {
                        allReservations[index] = waitlist.remove(0);
                    }
                }
            } else if (input.equals("m")) {
                System.out.print("Enter the number of people in your reservation: ");
                int numOfPeople = scanner.nextInt();
                System.out.print("Enter the reservation hour (in 24 hour clock): ");
                int reservationTimeHour = scanner.nextInt();
                System.out.print("Enter the reservation minute: ");
                int reservationTimeMin = scanner.nextInt();
                int index = getReservationIndex(numOfPeople, reservationTimeHour, reservationTimeMin);
                if (index == -1) {
                    System.out.println("No reservation with that time");
                } else {
                    System.out.print("Enter the NEW number of people in your reservation: ");
                    int newNumOfPeople = scanner.nextInt();
                    System.out.print("Enter the NEW reservation hour (in 24 hour clock): ");
                    int newReservationTimeHour = scanner.nextInt();
                    System.out.print("Enter the NEW reservation minute: ");
                    int newReservationTimeMin = scanner.nextInt();
                    scanner.nextLine();
                    Reservation obj = new Reservation(newNumOfPeople, newReservationTimeHour, newReservationTimeMin, allReservations[index].reservationMadeHour, allReservations[index].reservationMadeMin);
                    allReservations[index] = obj;
                }
            } else if (input.equals("e")) {
                System.out.print("Enter the number of people in your reservation: ");
                int numOfPeople = scanner.nextInt();

                System.out.print("Enter the reservation hour (in 24 hour clock): ");
                int reservationTimeHour = scanner.nextInt();
                System.out.print("Enter the reservation minute: ");
                int reservationTimeMin = scanner.nextInt();

                System.out.print("Enter the reservation made hour (in 24 hour clock): ");
                int reservationMadeHour = scanner.nextInt();
                System.out.print("Enter the reservation made minute: ");
                int reservationMadeMin = scanner.nextInt();
                scanner.nextLine();

                Reservation obj = new Reservation(numOfPeople, reservationTimeHour, reservationTimeMin, reservationMadeHour, reservationMadeMin);
                addReservation(obj);
            } else if (input.equals("s")) {
                System.out.print("Enter the name of the file: ");
                String fileName = scanner.nextLine();
                saveToFile(fileName);
            } else if (input.equals("l")) {
                System.out.print("Enter the name of the file: ");
                String fileName = scanner.nextLine();
                Load(fileName);
            } else if (input.equals("p")) {
                System.out.println("Reservations: ");
                for (int i = 0; i < allReservations.length; i++) {
                    if (allReservations[i] != null) {
                        System.out.println(allReservations[i].toString());
                    }
                }
                System.out.println("Waitlist: ");
                for (int i = 0; i < waitlist.size(); i++) {
                    System.out.println(waitlist.get(i).toString());
                }
            }
        }
        sortReservations();
        System.out.println(displayReservations());
    }
}