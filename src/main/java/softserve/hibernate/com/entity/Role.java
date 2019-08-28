package softserve.hibernate.com.entity;

import lombok.Data;
import softserve.hibernate.com.constant.RoleType;

import javax.persistence.*;
import java.lang.annotation.Annotation;

@Data
@Entity
@Table(name = "roles")
public class Role implements Entity{
    private final String ENTITY_NAME = "ROLE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition="ENUM('GUEST','USER','ADMIN')", nullable = false)
    private RoleType roleType;

    @Override
    public String name() {
        return ENTITY_NAME;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Role.class;
    }
}
