package softserve.hibernate.com.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition="ENUM('GUEST','USER','ADMIN')", nullable = false)
    private RoleType roleType;

}
