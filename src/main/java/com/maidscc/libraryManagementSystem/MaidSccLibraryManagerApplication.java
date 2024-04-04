package com.maidscc.libraryManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MaidSccLibraryManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaidSccLibraryManagerApplication.class, args);
	}

}
