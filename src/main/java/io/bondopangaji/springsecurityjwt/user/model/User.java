/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * POJO representing users record in database using Spring Data JPA,
 * this POJO also using Lombok library to reduce boilerplate code and Spring Validation as validator.
 *
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/">
 *     Spring Data JPA [Accesed: April 6, 2022]</a>
 * @see <a href="https://projectlombok.org/">Lombok Library [Accesed: April 6, 2022]</a>
 * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/validation.html">
 *     Spring Validation [Accesed: April 6, 2022]</a>
 *
 * @author Bondo Pangaji
 */
@Getter @Setter @Accessors(chain = true)
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 20)
    @Column
    private String name;

    @NotEmpty
    @Size(max = 20)
    @Column
    private String username;

    @NotEmpty
    @Size(max = 50)
    @Email
    @Column
    private String email;

    @NotEmpty
    @Size(max = 120)
    @Column
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
