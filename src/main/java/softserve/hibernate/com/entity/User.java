package softserve.hibernate.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    private String job;

    private Integer age;

    private Float weight;

    private LocalDateTime birthDay;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    public User() {
    }

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public User(String name, String lastName, String job, Integer age, Role role, Date created) {
        this.name = name;
        this.lastName = lastName;
        this.job = job;
        this.age = age;
        this.created = created;
        this.role = role;
    }

    public User(String name, String lastName, String job, Role role, Date created, Float weight, LocalDateTime birthDay) {
        this.name = name;
        this.lastName = lastName;
        this.job = job;
        this.age = LocalDateTime.now().getYear() - birthDay.getYear();
        this.weight = weight;
        this.birthDay = birthDay;
        this.created = created;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getName().equals(user.getName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getJob(), user.getJob()) &&
                Objects.equals(getAge(), user.getAge()) &&
                Objects.equals(getWeight(), user.getWeight()) &&
                Objects.equals(getBirthDay(), user.getBirthDay()) &&
                Objects.equals(getCreated(), user.getCreated()) &&
                getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLastName(), getJob(), getAge(), weight, birthDay, getCreated(), getRole());
    }
}
