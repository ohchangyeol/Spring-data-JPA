package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional(readOnly = false)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Item 저장
     * 쓰기권한
     */
    @Transactional
    public void saveItem (Item item) {
        itemRepository.save(item);
    }

    /**
     * Item List 조회
     */
    public List<Item> fimdItems() {
        return itemRepository.findAll();
    }

    /**
     * Item 조회
     */
    public Item findOne (Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
