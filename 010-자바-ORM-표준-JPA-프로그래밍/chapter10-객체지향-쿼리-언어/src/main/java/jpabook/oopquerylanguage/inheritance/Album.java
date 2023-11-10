package jpabook.oopquerylanguage.inheritance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
    private String etc;

    public Album() {
    }

    public Album(int price, int stockQuantity, String artist, String etc) {
        super(price, stockQuantity);
        this.artist = artist;
        this.etc = etc;
    }

    public String getArtist() {
        return artist;
    }

    public String getEtc() {
        return etc;
    }
}
