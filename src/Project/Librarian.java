package Project;

import java.util.Date;
import java.util.Random;


public class Librarian implements ILibrarian {

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

    public void createAccount(int pnummer, String fnamn, String lnamn, String role) {

        Member[] member = libraryStore.getAllMembers();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();
        Random rnd = new Random();
        int id = rnd.nextInt(9999 - 1000) + 1000;
        int counter = member.length;

        for (BannedMembers bm : bannedMembers) {
            if (bm.getPersonalNum() == pnummer) {
                System.out.println("This account is banned. Dont come back");
            }
        }


        for (Member m : member) {
            if (m.getPersonalNum() == pnummer && m.isSuspended()) {
                System.out.println("This person is suspended from entering the system! The process will not be allowed to continue.");
                counter--;
            } else if (m.getPersonalNum() == pnummer) {
                System.out.println("This person already exist in the system");
                counter--;
            } else {
                counter--;
            }
            if (counter == 0) {
                libraryStore.addMember(id, pnummer, fnamn, lnamn, role);
            }

        }

    }


    @Override
    public void checkBanned() {
        Member[] members = libraryStore.getAllMembers();
       for (Member m :members) {
           int ID = m.getID();
           if (ID == m.getID() && m.getNumOfSuspensions() > 2) {
               banMember(ID);
           }
       }
    }

    @Override
    public boolean getMedlem(int ID) {
        Member[] members = libraryStore.getAllMembers();
        for (Member m: members) {
            if (m.getID() == ID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getMedlemRole(String role) {

        Member[] members = libraryStore.getAllMembers();
        for (Member m: members) {
            if (m.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void banMember(int ID) {
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();
        Member[] members = libraryStore.getAllMembers();
        Book[] books = libraryStore.getAllBooks();
        for (HasBook hb: hasBooks) {
            String title = hb.getTitle();
            String isbn = hb.getISBN();
            if (hb.getID() == ID) {
                for (Member m: members) {
                    int numOfLoans = m.getNumOfLoans();
                    for (Book b: books) {
                        int numOfBorEx = b.getNumberOfBorrowedEx();
                        libraryStore.returnB(title, ID, isbn, numOfLoans, numOfBorEx);
                    }
                }
            }
        }
        for (Member m: members) {
            int pNumber = m.getPersonalNum();
            String firstName = m.getFirstName();
            String lastName = m.getLastName();
            if (m.getID() == ID) {
                libraryStore.addBannedMember(pNumber, firstName, lastName);
                libraryStore.removeMember(ID);
            }
        }
    }

    @Override
    public void suspendMember(int ID) {
        Member[] members = libraryStore.getAllMembers();
        for (Member m: members) {
            if (m.getID() == ID) {
                if (m.getStrikes() > 2) {
                    libraryStore.addSuspension(ID);
                }
                /*
                if (m.getNumOfSuspensions() > 2) {
                    banMember(ID);
                }

                 */

            }
        }
    }

    @Override
    public void removeSuspension() {
        Date date = new Date();
        Date suspendedDate;
        Member[] members = libraryStore.getAllMembers();
        for (Member m: members) {
            int ID = m.getID();
            suspendedDate = m.getSuspendedDate();
            if (suspendedDate != null && date.compareTo(suspendedDate) > 0) {
                libraryStore.removeSuspension(ID);
            }
        }
    }

    @Override
    public void deleteMember(int id) {
        Member[] members = libraryStore.getAllMembers();
        BannedMembers[] bannedMembers = libraryStore.getAllBannedMembers();

        for (BannedMembers bm : bannedMembers) {
            if (bm.getPersonalNum() == id) {
                libraryStore.removeMember(id);
            }
        }
        for (Member m : members) {
            if (m.getID() == id) {
                libraryStore.removeMember(id);
                System.out.println("The user has been removed from the system " + "where the id was: " + id);
            }
        }
    }

    @Override
    public boolean borrowBook(String title, int id, String role) {

        Member[] members = libraryStore.getAllMembers();
        Book[] books = libraryStore.getAllBooks();
        HasBook[] hasBooks = libraryStore.getAllBorrowedBooks();

        String isbn;
        int numOfLoanedEx = 0;

        for (HasBook hb: hasBooks) {
            isbn = hb.getISBN();


        }
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
                continue;
            }
            if (m.getID() == id && !m.isSuspended()) {
                if (m.getRole().equalsIgnoreCase("Student")) {
                    if (numOfLoans < 3) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }
                } else if (m.getRole().equals("Postgraduate")) {
                    if (numOfLoans < 5) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }
                } else if (m.getRole().equals("PhD")) {
                    if (numOfLoans < 7) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
                        return true;
                    } else {
                        System.out.println("You have exceded the number of loans!");
                    }

                } else if (m.getRole().equals("Teacher")) {
                    if (numOfLoans < 10) {
                        numOfLoans++;
                        m.setNumOfLoans(numOfLoans);
                        libraryStore.borrow(id, title, numOfLoans, numOfLoanedEx);
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
    public void returnBook(String title, String isbn, int ID) {

        String returnOfISBN="";
        int numOfLoans=0;
        int numOfBorrowedEx=0;

        Date date = new Date();
        Date end_date;
        HasBook[] hasBooks= libraryStore.getAllBorrowedBooks();
        Book[] books=libraryStore.getAllBooks();
        Member[] members=libraryStore.getAllMembers();

        for (HasBook hb:hasBooks) {
            if (hb.getTitle().equals(title) && hb.getID()==ID){
                returnOfISBN=hb.getISBN();
                end_date = hb.getEnd_date();
                if (date.compareTo(end_date) > 0) {
                    libraryStore.setStrikes(ID);
                }
            }
        }
        for (Member m: members) {
            if (m.getID()==ID){
                numOfLoans=m.getNumOfLoans();
                numOfLoans--;
            }

        }
        for (Book b: books) {
            if (b.getTitle().equalsIgnoreCase(title) && b.getIsbn().equalsIgnoreCase(isbn)){
                numOfBorrowedEx=b.getNumberOfBorrowedEx();
                numOfBorrowedEx--;
            }



        }
        libraryStore.returnB(title,ID,returnOfISBN,numOfLoans,numOfBorrowedEx);
    }

    @Override
    public void checkDeletedMember() {
        //Förmodligen kan vi ta bort
    }

    @Override
    public boolean isItemAvailable(String title) {
        Book[] books = libraryStore.getAllBooks();
        int nrOfEx=0;
        int nrOfBorrowedEx=0;
        for (Book book : books) {
            if (title.equals(book.getTitle())) {
                nrOfEx=book.getNumberOfEx();
                nrOfBorrowedEx=book.getNumberOfBorrowedEx();
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

