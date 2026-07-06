package com.openclassrooms.mddapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

class BaseApplicationTests {

	@Test
  void constructorCanBeInstantiated() {
    new BaseApplication();
  }

	@Test
  void mainDelegatesToSpringApplicationRun() {
    String[] args = new String[] {"--spring.main.banner-mode=off"};

    try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      BaseApplication.main(args);
      mocked.verify(() -> SpringApplication.run(BaseApplication.class, args));
    }
  }
}
