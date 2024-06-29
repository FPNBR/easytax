package br.com.easytax.easytax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_refresh_token")
@SequenceGenerator(name = "seq_refresh_token", sequenceName = "seq_refresh_token", allocationSize = 1)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_refresh_token")
    private Long id;

    @OneToOne
    private Client client;

    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;
}
