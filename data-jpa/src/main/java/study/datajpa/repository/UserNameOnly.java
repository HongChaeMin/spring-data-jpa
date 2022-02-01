package study.datajpa.repository;

public interface UserNameOnly {

    // 이렇게 하면 이 값대로 결과 뽑을 수 있음
    // @Value("#{target.userName + ' ' + target.age}")
    String getUserName();

}
