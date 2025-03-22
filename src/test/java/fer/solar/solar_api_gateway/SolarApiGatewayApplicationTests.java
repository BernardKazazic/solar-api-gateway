package fer.solar.solar_api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SolarApiGatewayApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void applicationContext_should_load_when_applicationStarts() {
		// Verify the application context loads successfully
		assertThat(applicationContext).isNotNull();
	}
}
