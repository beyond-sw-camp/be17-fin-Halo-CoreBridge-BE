package com.halo.core_bridge.api.board.controller;

import com.halo.core_bridge.api.board.contents.SwaggerBoardContents;
import com.halo.core_bridge.api.board.model.dto.BoardDto;
import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.board.service.BoardService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사내 공지사항", description = "공지사항 등록, 조회, 수정, 삭제, 전체 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(
            summary = "전체 게시글 조회",
            description = "모든 게시글을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerBoardContents.LIST_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<BaseResponse<List<Board>>> getBoards() {
        List<Board> boards = boardService.findAll();
        return ResponseEntity.ok(BaseResponse.success(boards));
    }

    @Operation(
            summary = "특정 게시글 조회",
            description = "ID로 특정 게시글을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "게시글 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerBoardContents.READ_RESPONSE)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BoardDto.Read>> getBoard(@PathVariable Long id) {
        BoardDto.Read findBoard = boardService.findById(id);
        return ResponseEntity.ok(BaseResponse.success(findBoard));
    }

    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.Create.class),
                            examples = @ExampleObject(value = SwaggerBoardContents.CREATE_REQUEST)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerBoardContents.CREATE_RESPONSE)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<Board>> createBoard(@RequestBody BoardDto.Create createBoard) {
        Board board = boardService.save(createBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(board));
    }

    @Operation(
            summary = "게시글 수정",
            description = "ID로 특정 게시글을 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.Update.class),
                            examples = @ExampleObject(value = SwaggerBoardContents.UPDATE_REQUEST)
                    )
            ),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "수정할 게시글 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerBoardContents.UPDATE_RESPONSE)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateBoard(@PathVariable Long id, @RequestBody BoardDto.Update updateBoard) {
        boardService.updateById(id, updateBoard);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "게시글 삭제",
            description = "ID로 특정 게시글을 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "삭제할 게시글 ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(value = SwaggerBoardContents.DELETE_RESPONSE)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteBoard(@PathVariable Long id) {
        boardService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
