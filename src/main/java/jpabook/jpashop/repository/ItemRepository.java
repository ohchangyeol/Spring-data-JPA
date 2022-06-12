package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository @RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save (Item item) {
        // 아이템이 새로 등록이 되면 DB에 들어가기전까지 id가 없기 때문에 신규 등록을 nuill로 판단하여 신규 등록.
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // TODO: 2022-06-09  업데이트 같은 느낌? 다시 검색하고 공부.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
