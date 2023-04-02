package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class NetIdTest {

    NetId netId;

    @BeforeEach
    void setUp() {
        netId = new NetId("ageist");
    }

    @Test
    void testToString() {
        assertThat(netId.toString()).isEqualTo("ageist");
    }
}