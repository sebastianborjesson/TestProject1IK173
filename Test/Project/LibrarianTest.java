package Project;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class LibrarianTest {

    private LibraryStore ls = mock(LibraryStore.class);
    private Librarian cut = new Librarian(ls);


    @Test
    void createAccount() {


        when(ls.getAllBannedMembers())
                .thenReturn(new BannedMembers[]{
                        new BannedMembers(1, "Nicklas", "Eberhagen")
                });

        when(ls.getAllMembers())
                .thenReturn(new Member[]{
                        new Member(1, 910217, "Test", "Testsson", "Student", 0, 0, false, 0, null),

                });

        assertTrue(cut.createAccount(10217, "Test", "Testsson", "Student"));
        ls.addMember(1, 10217, "Test", "Testsson","Student");

        verify(ls,times(1)).addMember(1, 10217, "Test", "Testsson","Student" );
        //Inparametrar måste stämma överens ifall en metod ska funka. ID kommer alltid att vara unikt. Där av ls.addMember anropet.

    }

    @Test
    void banMember() {

        Date date1 = new Date(2020-5-21);
        Date date2 = new Date(2020-6-22);

        when(ls.getAllBorrowedBooks())
                .thenReturn(new HasBook[]{
                        new HasBook("A C",1,"123",date1,date2)
                });
        when(ls.getAllMembers()).thenReturn(new Member[]{
                new Member(1, 910217, "Test", "Testsson", "Student", 0, 1, false, 1, null)
        });
        when(ls.getAllBooks()).thenReturn(new Book[]{
                new Book("A Dance with Dragons", "George R.R. Martin", "978-0553801477", 3 , 1)
        });

        ls.returnB("A Dance with Dragons",1,"978-0553801477",1,1);
        ls.addBannedMember(910217,"Test", "Testsson");
        ls.removeMember(5);
        assertTrue(cut.banMember(3));
        verify(ls,times(1)).returnB("A Dance with Dragons",1,"978-0553801477",1,1);


    }

    @Test
    void suspendMember() {



        when(ls.getAllMembers())
                .thenReturn(new Member[]{
                        new Member(1, 910217, "Test", "Testsson", "Student", 0, 3, false, 0, null)
                });
        assertTrue(cut.suspendMember(1));


        verify(ls,times(1)).addSuspension(1);
    }


    @Test
    void deleteMember() {

        when(ls.getAllMembers())
                .thenReturn(new Member[]{
                        new Member(1, 910217, "Test", "Testsson", "Student", 0, 0, false, 0, null)
                });

        assertTrue(cut.deleteMember(1));


        verify(ls).removeMember(1);



    }

    @Test
    void borrowBook() {
        when(ls.getAllMembers())
                .thenReturn(new Member[]{
                        new Member(1, 990101, "Test", "Testsson", "Student", 0, 0, false, 1, null)
                });

        when(ls.getAllBooks())
                .thenReturn(new Book[]{
                        new Book("A Dance with Dragons", "George R.R. Martin", "978-0553801477", 3 , 0)
                });

        assertTrue(cut.borrowBook("A Dance with Dragons", 1));

        verify(ls, times(1)).borrow(1, "A Dance with Dragons",1,1);
    }

    @Test
    void returnBook() {

        LocalDate date = LocalDate.now();
        LocalDate date1 = date.plusDays(15);
        java.util.Date loanDate = java.sql.Date.valueOf(date);
        Date returnDate = java.sql.Date.valueOf(date1);

        when(ls.getAllMembers())
                .thenReturn(new Member[]{
                        new Member(1, 990101, "Test", "Testsson", "Student", 1, 0, false, 1, null)
                });

        when(ls.getAllBooks()).thenReturn(new Book[]{
                new Book("a b", "Pontus" , "123", 2,1 )
        });

        when(ls.getAllBorrowedBooks())
                .thenReturn(new HasBook[]{
                        new HasBook("a b", 1, "123" , loanDate, returnDate)
                });

        assertTrue(cut.returnBook("a b", "123", 1));

        verify(ls, Mockito.times(1)).returnB("a b", 1, "123",0,0);


    }

}