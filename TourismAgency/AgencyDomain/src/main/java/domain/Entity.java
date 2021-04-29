package domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/*@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)*/
//@javax.persistence.Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Table(name="users")
//@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)
//@DiscriminatorValue(value = "entity")
@MappedSuperclass
public class Entity implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //@Column(name = "idUser")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}