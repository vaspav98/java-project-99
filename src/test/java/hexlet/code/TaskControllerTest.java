package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

    private TaskStatus testStatus;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();

        User testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);

        testStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        statusRepository.save(testStatus);

        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("title").isEqualTo(testTask.getName()),
                a -> a.node("index").isEqualTo(testTask.getIndex()),
                a -> a.node("content").isEqualTo(testTask.getDescription()),
                a -> a.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                a -> a.node("assignee_id").isEqualTo(testTask.getAssignee().getId())
        );
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of(
                "title", "newTitle", "content", "newContent", "status", testStatus.getSlug()));

        MockHttpServletRequestBuilder request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Task addedTask = taskRepository.findByName(data.get("title")).orElse(null);
        assertThat(addedTask).isNotNull();
        assertThat(addedTask.getName()).isEqualTo(data.get("title"));
        assertThat(addedTask.getIndex()).isNull();
        assertThat(addedTask.getDescription()).isEqualTo(data.get("content"));
        assertThat(addedTask.getTaskStatus()).isEqualTo(testStatus);
        assertThat(addedTask.getAssignee()).isNull();
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("title", "newTitle"));

        MockHttpServletRequestBuilder request = put("/api/tasks/" + testTask.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getName()).isEqualTo("newTitle");
        assertThat(updatedTask.getIndex()).isEqualTo(testTask.getIndex());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isNoContent());

        Task destroyedTask = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(destroyedTask).isNull();
    }

}
