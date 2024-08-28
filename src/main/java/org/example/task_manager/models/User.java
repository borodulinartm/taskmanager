package org.example.task_manager.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    private String nickname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public final boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;

        Class<?> oOtherClass = otherObject instanceof HibernateProxy ?
                ((HibernateProxy) otherObject).getHibernateLazyInitializer().getImplementationClass() :
                otherObject.getClass();

        Class<?> oThisClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass() :
                this.getClass();

        if (oOtherClass != oThisClass) return true;

        User otherTask = (User) otherObject;
        return getUserID() != null && Objects.equals(getUserID(), otherTask.getUserID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass().hashCode() :
                this.getClass().hashCode();
    }
}
