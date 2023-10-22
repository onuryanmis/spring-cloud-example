package com.github.com.taskservice.repository;

import com.github.com.taskservice.entity.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task taskItem;

    @BeforeEach
    public void setUp() {
        taskItem = Task.builder()
                .title("Test task")
                .completed(false)
                .categoryId(1L)
                .build();
    }

    @AfterEach
    public void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("[TaskRepository] - save() method")
    public void testSaveTask() {
        Task savedTask = taskRepository.save(taskItem);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo(taskItem.getTitle());
        assertThat(savedTask.getCompleted()).isEqualTo(taskItem.getCompleted());
        assertThat(savedTask.getCategoryId()).isEqualTo(taskItem.getCategoryId());
    }

    @Test
    @DisplayName("[TaskRepository] - findAll() method")
    public void testFindAllTasks() {
        taskRepository.save(taskItem);

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("[TaskRepository] - findById() method")
    public void testFindTaskById() {
        Task task = taskRepository.save(taskItem);

        Optional<Task> foundTask = taskRepository.findById(task.getId());

        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo(taskItem.getTitle());
        assertThat(foundTask.get().getCompleted()).isEqualTo(taskItem.getCompleted());
        assertThat(foundTask.get().getCategoryId()).isEqualTo(taskItem.getCategoryId());
    }

    @Test
    @DisplayName("[TaskRepository] - deleteById() method")
    public void testDeleteTaskById() {
        Task task = taskRepository.save(taskItem);

        taskRepository.deleteById(task.getId());

        Optional<Task> deletedTask = taskRepository.findById(task.getId());

        assertThat(deletedTask).isEmpty();
    }
}
