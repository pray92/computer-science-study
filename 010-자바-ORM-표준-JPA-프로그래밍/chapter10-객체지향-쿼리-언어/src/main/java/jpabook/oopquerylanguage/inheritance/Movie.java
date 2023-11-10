package jpabook.oopquerylanguage.inheritance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
public class Movie extends Item {

    private String actor;
    private String director;

    public Movie() {
    }

    public Movie(int price, int stockQuantity, String actor, String director) {
        super(price, stockQuantity);
        this.actor = actor;
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public String getDirector() {
        return director;
    }
}
