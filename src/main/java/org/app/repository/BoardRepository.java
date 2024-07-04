package org.app.repository;

import org.app.model.Board;
import org.app.model.TodoTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
    List<Board> findBoardsByUserId(Integer userId);

}
