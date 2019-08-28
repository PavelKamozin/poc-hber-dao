package softserve.hibernate.com.entity;

import lombok.Data;

import javax.persistence.*;
import java.lang.annotation.Annotation;

@Data
@Entity
@Table(name = "users")
public class User implements Entity {
    private final String ENTITY_NAME = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @Override
    public String name() {
        return ENTITY_NAME;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return User.class;
    }
}
