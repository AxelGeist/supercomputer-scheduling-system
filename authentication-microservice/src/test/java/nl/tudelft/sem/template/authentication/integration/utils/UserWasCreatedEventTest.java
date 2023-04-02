package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import commons.NetId;
import nl.tudelft.sem.template.authentication.domain.user.UserWasCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserWasCreatedEventTest {
    NetId netId;
    UserWasCreatedEvent event;

    @BeforeEach
    public void init() {
        netId = new NetId("itomov");
        event = new UserWasCreatedEvent(netId);
    }

    @Test
    public void getNetIdTest() {
        assertThat(event.getNetId()).isEqualTo(new NetId("itomov"));
    }
}
