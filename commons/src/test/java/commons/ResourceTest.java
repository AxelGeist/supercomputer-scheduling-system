package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ResourceTest {
    int cpu;
    int gpu;
    int memory;

    Resource resource;

    @BeforeEach
    void setUp() {
        cpu = 1;
        gpu = 1;
        memory = 1;

        resource = new Resource(cpu, gpu, memory);
    }

    @Test
    void getCpuTest() {
        assertThat(resource.getCpu()).isEqualTo(1);
    }

    @Test
    void getGpuTest() {
        assertThat(resource.getGpu()).isEqualTo(1);
    }

    @Test
    void getMemoryTest() {
        assertThat(resource.getMem()).isEqualTo(1);
    }
}