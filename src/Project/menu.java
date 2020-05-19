package Project;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.sql.*;

public class menu extends Librarian{
    public static void main(String[] args) {
        LibraryStore ls = new LibraryStore();
        Librarian lib = new Librarian(ls);
        Scanner scan = new Scanner(System.in);
        int choice;






        do {
            lib.removeSuspension();
            HasBook [] hasBooks = ls.getAllBorrowedBooks();
            Member[] members = ls.getAllMembers();
            lib.checkBanned();
            System.out.println("Welcome to the library! \n=============");
            System.out.println("In the menu below you will see the current options that the library has.");
            System.out.println("1. Register a new user to the library!");
            System.out.println("2. Rent book");
            System.out.println("3. Return book");
            System.out.println("4. Remove existing user");     //Tillfällig för hjälp
            System.out.println("5. Close the system");
            System.out.print("Make your choice: -> ");
            choice = scan.nextInt();

            if (choice == 1) {
                System.out.println("\nRegister a new user");
                System.out.print("Personal number (max 10 numbers): ");
                int personalnumber;
                personalnumber = scan.nextInt();

                System.out.print("First name: ");
                String firstName = scan.next();

                System.out.print("Last name: ");
                String lastName = scan.next();

                System.out.print("Your role at the university - Please enter one of these alternatives: \n 1 for student 2 for postgraduate 3 for PhD 4 for teacher");
                System.out.print("Student");
                String student="Student";
                System.out.print("Postgraduate");
                String postgraduate="Postgraduate";
                System.out.print("PhD");
                String phD="Phd";
                System.out.print("Teacher");
                String teacher ="Teacher";
                int val = scan.nextInt();

                   if (val==1){
                       lib.createAccount(personalnumber, firstName, lastName, student);
                   } else if (val==2){
                       lib.createAccount(personalnumber, firstName, lastName, postgraduate);
                   } else if (val==3){
                       lib.createAccount(personalnumber, firstName, lastName, phD);
                   } else if (val==4){
                       lib.createAccount(personalnumber, firstName, lastName, teacher);
                   } else {
                       System.out.println("Enter correct type of role");
                       continue;
                   }
               }


                /*if (!role.equals("Student") || !role.equals("Postgraduate") || !role.equals("PhD") || !role.equals("Teacher")) {
                    System.out.println("You need to enter correct type of role: ");
                    role = scan.next();
                    lib.createAccount(personalnumber, firstName, lastName, role);
                } else {
                    lib.createAccount(personalnumber,firstName,lastName,role);
                }*/




            if (choice == 2) {
                System.out.println("If you like to rent a book, here is the current available books that the library offers: ");

                      //Hämta böcker
                Book[] books = ls.getAllBooks();
                for (Book b: books) {
                    System.out.println("Book title: " + b.getTitle() + " |----| Author of the book: " + b.getAuthor());
                }


                String bookTitle;
                System.out.print("Write the books title that you wish to loan:");
                bookTitle= scan.nextLine();
                bookTitle = scan.nextLine();
                if (lib.doesItemExist(bookTitle)){
                    System.out.println("Boken fanns");
                }else {
                    System.out.println("Boken fanns inte");
                    continue;}

                if (lib.isItemAvailable(bookTitle)){
                    System.out.println("Boken finns tillgänglig");
                }else {
                    System.out.println("Alla böcker är utlånade");
                    continue;
                }

                System.out.print("Enter your identification code: ");
                int id = scan.nextInt();

                if (!lib.getMedlem(id)) {
                    System.out.println("This member doesn't exist!");
                    continue;
                }



                System.out.println("Enter your role: ");
                String role = scan.next();
                if (!lib.getMedlemRole(role)) {
                    System.out.println("Role doesn't exist");
                    continue;
                }

                    //Fortsättningsblock är arbeta på.
                if (lib.borrowBook(bookTitle, id, role)) {
                   System.out.println("Success! You have borrowed the book: " + bookTitle);
                }

            }
            if (choice == 3) {
                // Problem som återstår - du ska inte kunna låna en bok mer än 1 gång på samma id
                // Komma fram en annan sout ifall du inte lånat böcker

                int userId;
                String bookTitle;
                String isbn = "";
                Date date = new Date();
                date.getDate();
                Date end_date;
                System.out.println("If you like to return a book, please enter your userid: ");
                userId = scan.nextInt();
                // felhantering vid fel id <--
                System.out.println("Books you are currently borrowing: ");
                for (HasBook hb:hasBooks) {
                    if (hb.getID() == userId) {
                        bookTitle = hb.getTitle();
                        System.out.println(bookTitle);
                    }
                }
                System.out.println("Enter the book you wish to return: ");
                String buffert = scan.nextLine();
                String bookToReturn = scan.nextLine();

                for (HasBook hb:hasBooks) {
                    if (hb.getID() == userId) {
                        isbn = hb.getISBN();
                        end_date = hb.getEnd_date();
                        if (bookToReturn.equals(hb.getTitle())) {
                            lib.returnBook(bookToReturn, isbn, userId);
                            System.out.println("You have returned " + bookToReturn);
                        } // felhantering om du skriver fel (else)
                        if (date.compareTo(end_date) > 0) {
                            System.out.println("You have returned it to late, strike for you!");
                            lib.suspendMember(userId);
                        }
                    }
                }
            }
            if (choice == 4) {
                System.out.print("Enter the id of the user that you wish to remove: ");
                int deleteId;
                deleteId = scan.nextInt();
                if (!lib.getMedlem(deleteId)) {
                    System.out.println("Didn't find the user");
                    continue;
                }
                lib.deleteMember(deleteId);
            }
        }
        while (choice != 5);
        System.out.println("Good bye and welcome back!");
    }
}