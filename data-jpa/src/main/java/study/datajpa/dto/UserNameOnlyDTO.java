package study.datajpa.dto;

public class UserNameOnlyDTO {

    private final String userName;

    public UserNameOnlyDTO(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

}
