package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import net.datafaker.Faker;
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

import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private Faker faker;

    private TaskStatus testStatus = new TaskStatus();

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testStatus.setName(faker.animal().name());
        testStatus.setSlug(faker.lorem().word());
        taskStatusRepository.save(testStatus);

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("name").isEqualTo(testStatus.getName()),
                a -> a.node("slug").isEqualTo(testStatus.getSlug())
        );
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/task_statuses").with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("name", "hello", "slug", "hel"));

        MockHttpServletRequestBuilder request = post("/api/task_statuses").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        TaskStatus addedStatus = taskStatusRepository.findBySlug("hel").orElse(null);
        assertThat(addedStatus).isNotNull();
        assertThat(addedStatus.getName()).isEqualTo("hello");
        assertThat(addedStatus.getSlug()).isEqualTo("hel");
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("name", "hello2"));

        MockHttpServletRequestBuilder request = put("/api/task_statuses/" + testStatus.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        TaskStatus updatedStatus = taskStatusRepository.findBySlug(testStatus.getSlug()).orElse(null);
        assertThat(updatedStatus).isNotNull();
        assertThat(updatedStatus.getName()).isEqualTo("hello2");
        assertThat(updatedStatus.getSlug()).isEqualTo(testStatus.getSlug());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isNoContent());

        TaskStatus destroyedStatus = taskStatusRepository.findById(testStatus.getId()).orElse(null);
        assertThat(destroyedStatus).isNull();
    }






}
