package az.mingle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String bio = "";

    @Column(name = "phone_number", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(name = "profile_image")
    private String profileImage;

    private boolean locked;

    private boolean enabled;

    private LocalDateTime modifiedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 👇 Bunlar `UserDetails` interface-indən gələn metodlardır:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Hal-hazırda rol sistemi yoxdur
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hesab müddəti bitməyib
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked; // Əgər `locked` true-dursa, bloklanıb
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Şifrə müddəti bitməyib
    }

    @Override
    public boolean isEnabled() {
        return enabled; // İstifadəçi aktivdirsə true qaytar
    }
}