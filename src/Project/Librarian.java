package Project;

import org.apache.logging.log4j.LogManager;

import java.util.Date;
import java.util.Random;
import org.apache.logging.log4j.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Librarian implements ILibrarian {

    private Logger logger = LogManager.getLogger(Librarian.class.getName());

    private LibraryStore libraryStore;
    private LibraryStub lst;

    public Librarian(LibraryStore ls) {
        libraryStore = ls;
    }

    public Librarian() {
    }

    public void LibraryStubArray(LibraryStub lst) {
        this.lst = lst;
    }

    Member[] memberList = new Member[10];

    @Override

    public boolean createAccount(int pnummer, String fnamn, String lnamn, String role) {

        Member[] member = libraryStore.getAllMembers();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();
        Random rnd = new Random();
        int id = rnd.nextInt(9999 - 1000) + 1000;
        int counter = member.length;

        for (BannedMembers bm : bannedMembers) {
            if (bm.getPersonalNum() != pnummer) {
                for (Member m : member) {
                    if (m.getPersonalNum() == pnummer) {
                        System.out.println("This person already exist in the system");
                        break;
                    } else {
                        counter--;
                    }
                    if (counter == 0) {
                        libraryStore.addMember(id, pnummer, fnamn, lnamn, role);
                        System.out.println("Your library user has been created!");
                        logger.info("Success! A new member has been created to the library. The ID is: " + id + " and the name is: " + fnamn +
                                " " + lnamn + " and the members role at the university is: " + role);
                        return true;
                    }
                }
                break;
            } else {
                System.out.println("This person is banned. Dont come back!");
                logger.error("This user was already banned so a new account cannot be created!" + bm);
                return false;
            }
        }
        return false;
    }


    @Override
    public void checkBanned() {
        Member[] members = libraryStore.getAllMembers();
        for (Member m : members) {
            int ID = m.getID();
            if (ID == m.getID() && m.getNumOfSuspensions() > 2) {
                banMember(ID);
            }
        }
    }

    @Override
    public void getHasBook() {
        /*
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();
        for (HasBook hb:hasBooks){
            if (hb.getEnd_date())
        }

         */
    }

    @Override
    public boolean getMedlem(int ID) {
        Member[] members = libraryStore.getAllMembers();
        for (Member m : members) {
            if (m.getID() == ID) {
                logger.info("This method searches our database for all members. A list of personal ID's was found: " + ID);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getMedlemRole(String role) {

        Member[] members = libraryStore.getAllMembers();
        for (Member m : members) {
            if (m.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean banMember(int ID) {
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();
        Member[] members = libraryStore.getAllMembers();
        Book[] books = libraryStore.getAllBooks();
        for (HasBook hb : hasBooks) {
            String title = hb.getTitle();
            String isbn = hb.getISBN();
            if (hb.getID() == ID) {
                for (Member m : members) {
                    int numOfLoans = m.getNumOfLoans();
                    for (Book b : books) {
                        int numOfBorEx = b.getNumberOfBorrowedEx();
                        libraryStore.returnB(title, ID, isbn, numOfLoans, numOfBorEx);
                    }
                }
            }
        }
        for (Member m : members) {
            int pNumber = m.getPersonalNum();
            String firstName = m.getFirstName();
            String lastName = m.getLastName();
            if (m.getID() == ID) {
                libraryStore.addBannedMember(pNumber, firstName, lastName);
                logger.error("A member has been banned! The unlucky one is: " + firstName + lastName + " Good bye!");
                libraryStore.removeMember(ID);
            }
        }
        return true;
    }

    @Override
    public boolean suspendMember(int ID) {
        Member[] members = libraryStore.getAllMembers();
        for (Member m : members) {
            if (m.getID() == ID) {
                if (m.getStrikes() > 2) {
                    libraryStore.addSuspension(ID);
                    logger.info("A suspension was done, this ID: " + ID + " has been suspended for 15 days!");
                    return true;
                }
                if (m.getNumOfSuspensions() > 2) {
                    banMember(ID);
                }
            }
        }
        return false;
    }

    @Override
    public void checkSuspension() {
        Date date = new Date();
        Date suspendedDate;
        Date end_date;
        Member[] members = libraryStore.getAllMembers();
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();
        for (Member m : members) {
            for (HasBook hb : hasBooks) {
                int ID = m.getID();
                end_date = hb.getEnd_date();
                suspendedDate = m.getSuspendedDate();
                if (suspendedDate != null && date.compareTo(suspendedDate) > 0 && ID == hb.getID() && !(date.compareTo(end_date) < 0)) {
                    libraryStore.addSuspension(ID);
                } else if (suspendedDate != null && date.compareTo(suspendedDate) > 0) {
                    libraryStore.removeSuspension(ID);
                }
            }

        }
    }

    @Override
    public boolean deleteMember(int id) {
        Member[] members = libraryStore.getAllMembers();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();

        /*
        for (BannedMembers bm : bannedMembers) {
            if (bm.getPersonalNum() == id) {
                libraryStore.removeMember(id);
            }
        }
        */
        for (Member m : members) {
            if (m.getID() == id) {
                libraryStore.removeMember(id);
                System.out.println("The user has been removed from the system " + "where the id was: " + id);
                logger.info("A user has been deleted with this id: " + id);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean borrowBook(String title, int id) {

        Member[] members = libraryStore.getAllMembers();
        Book[] books = libraryStore.getAllBooks();
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();

        String isbn;
        int numOfLoanedEx = 0;

        /*
        for (HasBook hb : hasBooks) {    //Ta bort för test / Viktor har testat utan denna loop och fått positivt resultat
            isbn = hb.getISBN();
        }

         */
        for (Book b : books) {
            if (b.getTitle().equals(title)) {
                isbn = b.getIsbn();
                numOfLoanedEx = b.getNumberOfBorrowedEx();
                numOfLoanedEx++;
                b.setNumberOfBorrowedEx(numOfLoanedEx);
            }
        }
        for (Member m : members) {
            int numOfLoans = m.getNumOfLoans();

            if (m.getID() == id && m.isSuspended()) {
                System.out.println("You are suspended until " + m.getSuspendedDate());
                logger.info("Ops, you were suspended. This ID can't for the moment borrow a book" + " The ID which can't borrow is: " + id);
                continue;
            }
            if (m.getID() == id && !m.isSuspended()) {
                if (m.getRole().equalsIgnoreCase("Student")) {
                    if (numOfLoans < 3) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        logger.info("A loan was successful. The book title was:" + title + " and the user id was: " + id
                        + " and the user's role was: " + m.getRole());
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }
                } else if (m.getRole().equals("Postgraduate")) {
                    if (numOfLoans < 5) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        logger.info("A loan was successful. The book title was:" + title + " and the user id was: " + id
                                + " and the user's role was: " + m.getRole());
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }
                } else if (m.getRole().equals("PhD")) {
                    if (numOfLoans < 7) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        logger.info("A loan was successful. The book title was:" + title + " and the user id was: " + id
                                + " and the user's role was: " + m.getRole());
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }

                } else if (m.getRole().equals("Teacher")) {
                    if (numOfLoans < 10) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        logger.info("A loan was successful. The book title was:" + title + " and the user id was: " + id
                                + " and the user's role was: " + m.getRole());
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }
                    return false;


                }
            }
        }
        return false;
    }

    @Override
    public boolean returnBook(String title, String isbn, int ID) {

        String returnOfISBN = "";
        int numOfLoans = 0;
        int numOfBorrowedEx = 0;

        Date date = new Date();
        Date end_date;
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();
        Book[] books = libraryStore.getAllBooks();
        Member[] members = libraryStore.getAllMembers();

        for (HasBook hb : hasBooks) {
            if (hb.getTitle().equals(title) && hb.getID() == ID) {
                returnOfISBN = hb.getISBN();
                end_date = hb.getEnd_date();
                if (date.compareTo(end_date) > 0) {
                    libraryStore.setStrikes(ID);
                    logger.info("A user with the user id: " + ID + " was given a strike!");
                }
            }
        }
        for (Member m : members) {
            if (m.getID() == ID) {
                numOfLoans = m.getNumOfLoans();
                numOfLoans--;
            }

        }
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title) && b.getIsbn().equalsIgnoreCase(isbn)) {
                numOfBorrowedEx = b.getNumberOfBorrowedEx();
                numOfBorrowedEx--;
            }


        }
        libraryStore.returnB(title,ID,returnOfISBN,numOfLoans,numOfBorrowedEx);
        logger.info("A book was successful returned." + " The user who returned was: " + ID + ", And the book title was " + title);
        return true;
    }

    @Override
    public void checkDeletedMember() {
        //Förmodligen kan vi ta bort
    }

    @Override
    public boolean isItemAvailable(String title) {
        Book[] books = libraryStore.getAllBooks();
        int nrOfEx = 0;
        int nrOfBorrowedEx = 0;
        for (Book book : books) {
            if (title.equals(book.getTitle())) {
                nrOfEx = book.getNumberOfEx();
                nrOfBorrowedEx = book.getNumberOfBorrowedEx();
                if (nrOfBorrowedEx < nrOfEx) return true;
            }
        }
        return false;
    }

    @Override
    public boolean doesItemExist(String title) {

        Book[] books = libraryStore.getAllBooks();
        for (Book book : books) {

            if (title.equals(book.getTitle())) {
                return true;

            }
        }
        return false;
    }
}

