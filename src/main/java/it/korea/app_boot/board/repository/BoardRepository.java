package it.korea.app_boot.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_boot.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

}
