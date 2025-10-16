package com.halo.core_bridge.api.board.model.dto;

import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.users.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class BoardDto {

    @Getter
    public static class Create {

        @Schema(description = "게시글 제목", example = "첫 번째 공지")
        private String title;

        @Schema(description = "게시글 본문", example = "CoreBridge 공지사항 본문입니다.")
        private String contents;

        @Schema(description = "작성자 사용자 ID", example = "1")
        private Long userId;

        public Board toEntity() {
            return Board.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .user(User.builder().id(userId).build())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Read {
        @Schema(description = "게시글 ID", example = "10")
        private Long id;

        @Schema(description = "게시글 제목", example = "첫 번째 공지")
        private String title;

        @Schema(description = "게시글 본문", example = "CoreBridge 공지사항 본문입니다.")
        private String contents;

        @Schema(description = "작성자 이름", example = "관리자")
        private String writer;

        public static Read from(Board board) {
            return Read.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .writer(board.getUser().getName())
                    .build();
        }
    }

    @Getter
    public static class Update {
        @Schema(description = "수정할 제목", example = "수정된 제목")
        private String title;

        @Schema(description = "수정할 본문", example = "수정된 본문 내용입니다.")
        private String contents;
    }
}
