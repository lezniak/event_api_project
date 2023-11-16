package com.example.praca.model;

import com.example.praca.dto.user.CreateUserDto;
import com.example.praca.dto.user.InformationUserDto;
import com.example.praca.dto.user.UpdateUserDto;
import com.example.praca.service.ServiceFunctions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean locked = false;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private boolean newsletter = false;

    @Column(nullable = false)
    private Date birthdate;

    @Column(nullable = false)
    private String city;

    @Lob
    private byte[] userPhoto;

    @CreatedBy
    private String createdBy;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy="user")
    private List<EventPost> posts;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
            @JoinTable(
                    name = "user_hobbies",
                    joinColumns = @JoinColumn(name = "user_id"),
                    inverseJoinColumns = @JoinColumn(name = "hobby_id")
            )
    List<Hobby> hobbies;


    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "user")
    private UserLastLogin userLastLogin;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "eventPayment")
    private List<EventPayment> eventPayments;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user")
    private List<Ticket> ticket;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @OneToMany(mappedBy="user")
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    private List<QrCode> qrCodes;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserRole> roles;

    @OneToMany(mappedBy="owner")
    private List<Organization> organizations;

    @OneToMany(mappedBy="boss")
    private List<Employee> employees;
    public static User of(CreateUserDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRoles(dto.getUserRole());
        user.setCity(dto.getCity());
        user.setBirthdate(ServiceFunctions.stringToDate(dto.getBirthdate()));
        if (dto.getUserPhotoS() != null)
            user.setUserPhoto(ServiceFunctions.convertBaseToByteArray(dto.getUserPhotoS()));
        user.setUserPhoto(ServiceFunctions.convertBaseToByteArray(ServiceFunctions.getDefaultUserAvatar()));

        return user;
    }

    public static User of(UpdateUserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        if (dto.getUserPhoto() != null)
            user.setUserPhoto(ServiceFunctions.convertBaseToByteArray(dto.getUserPhoto()));

        user.setUserPhoto(ServiceFunctions.convertBaseToByteArray(ServiceFunctions.getDefaultUserAvatar()));

        return user;
    }

    public static User of(InformationUserDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setId(dto.getId());

        return user;
    }

    public static User updateUser(User user, UpdateUserDto dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", locked=" + locked +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }

}
