package Project;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Meny extends Librarian {
    public static void main(String[] args) {

        LibraryStore lbs= new LibraryStore();
        Librarian lb = new Librarian(lbs);
        /*for (BannedMembers bm: lbs.getAllBannedMembers()) {
            System.out.println( "PM.nummer "+  bm.getPersonalNum());

        }*/
        //lb.deleteMember(2);

        lb.createAccount(23135, "sda", "sdd", "Student");





        //lb.createAccount(23123, "sda", "sda", "rsdas");
       /* for (Member m : lbs.getAllMembers()) {
            System.out.println(m.getPersonalNum());
        }

        */

    }


        //lbs.getAllBannedMembers(1234567890);


        }

