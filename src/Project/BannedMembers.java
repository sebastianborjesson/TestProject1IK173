package Project;

public class BannedMembers {
    private int personalNum;
    private String firstName;
    private String lastName;

    public BannedMembers(int personalNum, String firstName, String lastName) {
        this.personalNum = personalNum;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getPersonalNum() {
        return personalNum;
    }

    public void setPersonalNum(int personalNum) {
        this.personalNum = personalNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
