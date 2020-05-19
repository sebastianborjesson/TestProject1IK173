package Project;

public interface ILibrarian {
    void createAccount(int pnummer, String fnamne, String lname, String role);



    void checkBanned();

    void suspendMember(int ID);

    void removeSuspension();

    void banMember(int ID);

    boolean getMedlem(int ID);

    boolean getMedlemRole(String role);

    void deleteMember(int id);

    boolean borrowBook(String title, int id, String role);

    void returnBook(String title, String isbn, int ID);

    void checkDeletedMember();

    boolean isItemAvailable(String title);

    boolean doesItemExist(String title);
}
