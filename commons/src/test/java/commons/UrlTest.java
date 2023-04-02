package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UrlTest {

    Url url;

    @BeforeEach
    void setUp() {
        url = new Url();
    }

    @Test
    void getAuthenticationUrl() {
        assertThat(url.getAuthenticationUrl()).isEqualTo("http://localhost:8081");
        assertThat(Url.getAuthenticationUrl()).isEqualTo("http://localhost:8081");

    }

    @Test
    void getJobsUrl() {
        assertThat(url.getJobsUrl()).isEqualTo("http://localhost:8083");
        assertThat(Url.getJobsUrl()).isEqualTo("http://localhost:8083");

    }

    @Test
    void getSchedulerUrl() {
        assertThat(url.getSchedulerUrl()).isEqualTo("http://localhost:8084");
        assertThat(Url.getSchedulerUrl()).isEqualTo("http://localhost:8084");
    }

    @Test
    void getClustersUrl() {
        assertThat(url.getClustersUrl()).isEqualTo("http://localhost:8085");
        assertThat(Url.getClustersUrl()).isEqualTo("http://localhost:8085");
    }
}