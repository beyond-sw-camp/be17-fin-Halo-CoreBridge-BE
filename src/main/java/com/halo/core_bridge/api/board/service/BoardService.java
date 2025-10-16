package com.halo.core_bridge.api.board.service;

import com.halo.core_bridge.api.board.model.dto.BoardDto;
import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.board.repository.BoardRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service                      // 서비스 계층 빈 등록
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**전체 게시글 조회 */
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    /** 특정 게시글 조회 */
    public BoardDto.Read findById(Long id) {
        Optional<Board> findBoard = boardRepository.findById(id);

        if (findBoard.isPresent()) {
            return BoardDto.Read.from(findBoard.get());
        }

        throw BaseException.from(BaseResponseStatus.NOT_FOUNT_BOARD);
    }

    /** 게시글 저장 (생성/수정 둘 다 처리) */
    @Transactional
    public Board save(BoardDto.Create createBoard) {
        Board boardEntity = createBoard.toEntity();
        return boardRepository.save(boardEntity);
    }

    /** 게시글 삭제 */
    @Transactional
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    /** 게시글 수정 */
    @Transactional
    public void updateById(Long id, BoardDto.Update updateBoard) {
        Optional<Board> result = boardRepository.findById(id);

        if (result.isPresent()) {
            Board findBoard = result.get();
            findBoard.updateTitle(updateBoard.getTitle());
            findBoard.updateContents(updateBoard.getContents());
        }
    }
}
