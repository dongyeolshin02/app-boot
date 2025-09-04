package it.korea.app_boot.board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int brdId;

    private String title;

    private String contents;

    private String writer;

    private int readCount;

    private int  likeCount;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
