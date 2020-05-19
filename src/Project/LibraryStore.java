package Project;


import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;




public class LibraryStore implements ILibraryStore {


        private List<BannedMembers> bm1;
        private List<Member> mem;
        private List<HasBook> hb;

        public LibraryStore(){
            bm1 = new ArrayList<BannedMembers>();
            mem = new ArrayList<Member>();
            hb = new ArrayList<HasBook>();
        }

        ArrayList<Member> memberArrayList = new ArrayList<>();
        ArrayList<Book> arrayListBooks = new ArrayList<>();
        ArrayList<BannedMembers> bannedMembersArrayList = new ArrayList<>();
        ArrayList<HasBook> hasBookArrayList = new ArrayList<>();

    @Override
    public BannedMembers[] getAllBannedMembers() {
        bannedMembersArrayList.clear();

        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?allowPublicKeyRetrieval=true&useSSL=false",
                "root", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT pNumber, firstName, lastName FROM bannedmembers");
            while (result.next()) {
                BannedMembers BM = new BannedMembers(result.getInt("pNumber") , result.getString("firstName"), result.getString("lastName"));
                bm1.add(BM);
            }

            stmt.close();

        } catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }

        BannedMembers[] bannedMembers = new BannedMembers[bm1.size()];
        return bm1.toArray(bannedMembers);
    }


    @Override
    public Member[] getAllMembers() {
        memberArrayList.clear();

        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/1ik173project?useSSL=false",

                "root", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from member");
            while (result.next()) {
                Member m = new Member(result.getInt(1) , result.getInt(2), result.getString(3),result.getString(4), result.getString(5), result.getInt(6), result.getInt(7), result.getBoolean(8), result.getInt(9), result.getDate(10));
                memberArrayList.add(m);
            }
            stmt.close();

        } catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }

        Member[] members = new Member[memberArrayList.size()];
        return memberArrayList.toArray(members);

    }

    @Override
    public void addSuspension(int ID) {
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {

            PreparedStatement ps1 = conn.prepareStatement("UPDATE member SET isSuspended = true WHERE ID = " + ID + ";");
            PreparedStatement ps2 = conn.prepareStatement("UPDATE member SET numOfSusp = numOfSusp + 1 WHERE ID = " + ID + ";");
            PreparedStatement ps3 = conn.prepareStatement("UPDATE member SET suspendedDate = DATE_ADD(CURRENT_DATE, INTERVAL 15 DAY) WHERE ID = " + ID + ";");
            PreparedStatement ps4 = conn.prepareStatement("UPDATE member SET strikes = strikes = 0 WHERE ID = " + ID + ";");
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            ps4.executeUpdate();


        } catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
    }

    @Override
    public void removeSuspension(int ID) {
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {

            PreparedStatement ps1 = conn.prepareStatement("UPDATE member SET isSuspended = false WHERE ID = ?;");
            ps1.setInt(1, ID);
            PreparedStatement ps2 = conn.prepareStatement("UPDATE member SET suspendedDate = null WHERE ID = ?;");
            ps2.setInt(1, ID);
            ps1.executeUpdate();
            ps2.executeUpdate();


        } catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
    }

    @Override
    public void addMember(int ID,int personalNum, String firstName, String lastName, String role ){
        int numbOfLoans = 0;
        boolean isSuspended = false;
        int numOfSusp = 0;
        int strikes = 0;
        //Date suspendedDate = null;


        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO member value (?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, ID);
            ps.setInt(2, personalNum);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, role);
            ps.setInt(6,numbOfLoans);
            ps.setInt(7, strikes);
            ps.setBoolean(8, isSuspended);
            ps.setInt(9,  numOfSusp);
            ps.setDate(10, null);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
    }

    @Override
    public Book[] getAllBooks(){
        arrayListBooks.clear();     //Clear array before select, else duplicates.

        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?allowPublicKeyRetrieval=true&useSSL=false",
                "root", "1234")) {

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * from book");

            while(result.next()) {

                Book book = new Book(result.getString(1), result.getString(2), result.getString(3),result.getInt(4), result.getInt(5));
                arrayListBooks.add(book);
                //System.out.println("Book: " + result.getString(1) + " | Author: " + result.getString(2));
            }
            //statement.close();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }
        Book[] books = new Book[arrayListBooks.size()];
        return arrayListBooks.toArray(books);
    }

    public HasBook[] getAllBorrowedBooks(){
        hasBookArrayList.clear();
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {
            Statement statement = conn.createStatement();
            //statement.executeUpdate("SELECT * FROM hasbook");
            ResultSet result=statement.executeQuery("SELECT  * from hasbook");
            while (result.next()){
                HasBook hasBook=new HasBook(result.getString("book"),result.getInt("ID"), result.getString("isbn"), result.getDate("start_date"), result.getDate("end_date"));
                hasBookArrayList.add(hasBook);
            }

            statement.close();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }

        HasBook[] hasBooks=new HasBook[hasBookArrayList.size()];
        return hasBookArrayList.toArray(hasBooks);
    }

    @Override
    public void removeMember(int id) {
        try(Connection conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {
            Statement statement = conn.createStatement();
             statement.executeUpdate("DELETE FROM member WHERE member.ID = " + id + ";");

            statement.close();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }
    }

    public void borrow(int ID, String title, int numOfLoans, int numberOfBorrowedEx) {
    String isbn=null;
    Book [] books=getAllBooks();

        for (Book b:books) {
            if (b.getTitle().equals(title)){
                isbn=b.getIsbn();
            }
        }

    try(Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
            "root", "1234")) {

        PreparedStatement ps1 = conn.prepareStatement("UPDATE member set numOfLoans = ? where ID = ?");

        ps1.setInt(1, numOfLoans);
        ps1.setInt(2, ID);
        ps1.executeUpdate();

        PreparedStatement ps2 = conn.prepareStatement("INSERT into hasbook values (?,?,?, CURRENT_DATE , DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY))");

        ps2.setString(1, title);
        ps2.setInt(2, ID);
        ps2.setString(3, isbn);
        ps2.executeUpdate();

        PreparedStatement ps3 = conn.prepareStatement("UPDATE book SET numberOfBorrowedEx = ? WHERE isbn = ?");
        ps3.setInt(1, numberOfBorrowedEx);
        ps3.setString(2, isbn);
        ps3.executeUpdate();
    }
        catch (SQLException ex) {
        System.out.println("Something went wrong...");
        }
    }
    public void returnB(String title, int ID, String isbn, int numOfLoans, int numOfBorrowedEx){
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {

            PreparedStatement pst=conn.prepareStatement("DELETE from hasbook where ID=? AND isbn=?");
            pst.setInt(1,ID);
            pst.setString(2, isbn);
            pst.executeUpdate();
            PreparedStatement pst1=conn.prepareStatement("UPDATE member set numOfLoans=? where ID=?");
            pst1.setInt(1,numOfLoans);
            pst1.setInt(2,ID);
            pst1.executeUpdate();
            PreparedStatement pst2=conn.prepareStatement("UPDATE book set numberOfBorrowedEx=? where isbn=?");
            pst2.setInt(1,numOfBorrowedEx);
            pst2.setString(2,isbn);
            pst2.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }
    }

    public void setStrikes(int id) {
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE member SET strikes = strikes + 1 WHERE member.ID = " + id + ";");

            statement.close();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }
    }

    public void addBannedMember(int pNumber, String firstName, String lastName) {
        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/1ik173project?useSSL=false",
                "root", "1234")) {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO bannedmembers VALUES (?,?,?)");
            ps.setInt(1, pNumber);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println("Something went wrong...");
        }
    }
}