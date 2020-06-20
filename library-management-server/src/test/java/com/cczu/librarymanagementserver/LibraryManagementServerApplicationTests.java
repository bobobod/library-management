package com.cczu.librarymanagementserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class LibraryManagementServerApplicationTests {
	@Value("${user.home}")
	private String home;

	@Test
	void test() {
		System.out.println(home);
		BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		System.out.println(encoder.encode("bobobod"));
	}

}
