package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // public으로 두는 것 보다는 protected로 설정하는 것이 그나마 더 안전함.
    // 관례상 생성해서 값을 수정하는게 아니구나 라는 인식이있다는점.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
