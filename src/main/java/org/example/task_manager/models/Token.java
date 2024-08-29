package org.example.task_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Token {
    @Id
    @GeneratedValue
    private Long tokenID;

    @NotBlank(message = "Token cannot be null")
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

        Token otherTask = (Token) otherObject;
        return getTokenID() != null && Objects.equals(getTokenID(), otherTask.getTokenID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass().hashCode() :
                this.getClass().hashCode();
    }
}
