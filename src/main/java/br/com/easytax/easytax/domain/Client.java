package br.com.easytax.easytax.domain;

import br.com.easytax.easytax.dto.request.LoginRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user")
@SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    private Long id;

    private String name;

    @CPF
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_client_role",
            uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "role_id"}, name = "uk_client_role"),
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id", table = "t_client", foreignKey = @ForeignKey(name = "fk_client_role", value = ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "t_role", foreignKey = @ForeignKey(name = "fk_role_client", value = ConstraintMode.CONSTRAINT))
    )
    private List<Role> roles;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCredentialsValid(LoginRequestDTO loginRequestDTO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), this.password);
    }
}
