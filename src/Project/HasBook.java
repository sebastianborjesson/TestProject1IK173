package Project;

import java.util.Date;

public class HasBook {
    private String title;
    private int ID;
    private String ISBN;
    private Date start_date;
    private Date end_date;




    public HasBook(String title, int ID, String ISBN, Date start_date, Date end_date) {
        this.title = title;
        this.ID = ID;
        this.ISBN = ISBN;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public HasBook() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
