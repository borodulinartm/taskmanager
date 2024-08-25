package org.example.task_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Book extends BaseCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "The name cannot be blank")
    @Column(nullable = false)
    private String bookName;

    private String bookDescription;

    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    private List<Task> listTasks;

    @Override
    public final boolean equals(Object otherObject) {
        // Basic comparisons
        if (otherObject == null) return false;
        if (this == otherObject) return true;

        // Get the real class of our objects. It may be a proxy
        Class<?> oEffectiveClass = otherObject instanceof HibernateProxy ?
                ((HibernateProxy) otherObject).getHibernateLazyInitializer().getImplementationClass() :
                otherObject.getClass();

        Class<?> oCurrClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass() :
                this.getClass();

        if (oEffectiveClass != oCurrClass) return false;

        Book otherBook = (Book) otherObject;
        return getId() != null && Objects.equals(getId(), otherBook.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getImplementationClass().hashCode() :
                this.getClass().hashCode();
    }
}
