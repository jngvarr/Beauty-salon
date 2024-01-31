package ru.gb.hw5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.hw5.model.Task;
import ru.gb.hw5.model.TaskStatus;

import java.math.BigDecimal;
import java.util.List;

//@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT * FROM tasks WHERE status = status")
    List<Task> showByStatus(TaskStatus status);

    @Modifying
    @Query("UPDATE status SET status = :status WHERE id = :id")
    void changeStatus(long id, TaskStatus status);

    @Modifying
    @Query("DELETE FROM tasks WHERE name = :name")
    void deleteByName(String name);
}
