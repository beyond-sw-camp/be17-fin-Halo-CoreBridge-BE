package com.halo.core_bridge.api.board.repository;

import com.halo.core_bridge.api.board.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
