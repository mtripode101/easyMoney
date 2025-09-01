package com.easymoney.easymoney;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Desactivado temporalmente: requiere conexión a ScyllaDB")
class EasymoneyApplicationTests {

	@Test
	void contextLoads() {
	}

}
