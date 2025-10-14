package com.halo.core_bridge.api.board.model.dto;

import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.users.model.entity.User;
import lombok.Builder;
import lombok.Getter;

public class BoardDto {

    @Getter
    public static class Create {

        private String title;
        private String contents;
        private Long userId;

        public Board toEntity() {
            return Board.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .user(
                            User.builder().id(userId).build()
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Read {
        private Long id;
        private String title;
        private String contents;
        private String writer;

        public static BoardDto.Read fromEntity(Board board) {
            return Read.builder()
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .writer(board.getUser().getName())
                    .build();
        }
    }

    @Getter
    public static class Update {
        private String title;
        private String contents;
    }
}
