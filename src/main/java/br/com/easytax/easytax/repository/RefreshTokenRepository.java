package br.com.easytax.easytax.repository;

import br.com.easytax.easytax.domain.RefreshToken;
import br.com.easytax.easytax.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByClient(Client client);
}