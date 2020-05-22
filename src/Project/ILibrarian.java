package Project;

public interface ILibrarian {
    boolean createAccount(int pnummer, String fnamne, String lname, String role);

    void getHasBook();

    void checkBanned();

    boolean suspendMember(int ID);

    void checkSuspension();

    boolean banMember(int ID);

    boolean getMedlem(int ID);

    boolean getMedlemRole(String role);

    boolean deleteMember(int id);

    boolean borrowBook(String title, int id);

    boolean returnBook(String title, String isbn, int ID);

    void checkDeletedMember();

    boolean isItemAvailable(String title);

    boolean doesItemExist(String title);
}
