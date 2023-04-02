package nl.tudelft.sem.template.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.NetId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class JsonUtilTest {
    Object object;
    JasonUtil jasonUtil;

    @MockBean
    ObjectMapper objectMapper;

    @BeforeEach
    public void util() {
        object = new NetId("itomov");
        jasonUtil = new JasonUtil();
    }

    @Test
    public void jsonSerializerTest() {
        assertThat(jasonUtil.jsonSerializer(object)).isEqualTo("{\"netIdValue\":\"itomov\"}");
    }

    @Test
    public void jsonSerializerTestNull() {
        assertThat(jasonUtil.jsonSerializer(null)).isEqualTo("null");
    }

    @Test
    public void jsonDeserializerTest() {
        assertThat(jasonUtil.jsonDeserialiser("11", Integer.class)).isEqualTo(11);
    }

    @Test
    public void jsonDeserializerTestNull() {
        assertThat(jasonUtil.jsonDeserialiser("11", Null.class)).isEqualTo(null);
    }
}
