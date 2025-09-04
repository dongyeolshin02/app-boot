package it.korea.app_boot.board.dto;

import java.time.LocalDateTime;


import it.korea.app_boot.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardDTO {


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
       	private int brdId;
		private String title;
		private String writer;
		private int readCount;
		private int likeCount;
        private LocalDateTime createDate;
		private LocalDateTime updateDate;

        public static Response of (BoardEntity entity) {
           
            return Response.builder()
                   .brdId(entity.getBrdId())
                   .title(entity.getTitle())
                   .writer(entity.getWriter())
                   .readCount(entity.getReadCount())
                   .likeCount(entity.getLikeCount())
                   .createDate(entity.getCreateDate())
                   .updateDate(entity.getUpdateDate())
                   .build();

        }
    }
}
