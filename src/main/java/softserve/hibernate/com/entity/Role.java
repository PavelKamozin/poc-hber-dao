package softserve.hibernate.com.entity;

import lombok.Data;
import softserve.hibernate.com.constant.RoleType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {
    private final String ENTITY_NAME = "ROLE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('GUEST','USER','ADMIN')", nullable = false)
    private RoleType roleType;

}
