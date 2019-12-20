package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.model.SalvoGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalvoGameRepository extends JpaRepository<SalvoGame, Long> {
}
