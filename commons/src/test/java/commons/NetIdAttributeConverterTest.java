package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class NetIdAttributeConverterTest {

    NetIdAttributeConverter nac;
    NetId netId;

    @BeforeEach
    void setUp() {
        nac = new NetIdAttributeConverter();
        netId = new NetId("ageist");
    }

    @Test
    void convertToDatabaseColumn() {
        assertThat(nac.convertToDatabaseColumn(netId)).isEqualTo("ageist");
    }

    @Test
    void convertToEntityAttribute() {
        assertThat(nac.convertToEntityAttribute("ageist")).isEqualTo(netId);
    }
}