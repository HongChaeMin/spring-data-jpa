package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void save() {
        // 새로운 엔티티를 판단하는 기본 전략
        // - 식별자가 객체일 때 null 로 판단
        // - 식별자가 자바 기본 타입일 때 0 으로 판단
        // - Persistable 인터페이스를 구현해서 판단 로직 변경 가능

        Item item = new Item("A");
        itemRepository.save(item);
    }

}
