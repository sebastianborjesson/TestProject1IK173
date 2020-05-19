package Project;

public interface ILibraryStore {
    BannedMembers[] getAllBannedMembers();

    Member[] getAllMembers();

    void borrow(int ID, String title, int numOfLoans, int numberOfBorrowedEx);

    void addSuspension(int ID);

    void removeSuspension(int ID);

    void addMember(int ID,int personalNum, String firstName, String lastName, String role );

    void removeMember(int id);

    Book[] getAllBooks();

    void addBannedMember(int pNumber, String firstName, String lastName);

}
