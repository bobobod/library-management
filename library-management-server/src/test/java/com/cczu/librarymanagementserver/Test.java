package com.cczu.librarymanagementserver;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
public class Test {
	private String name;
	private int age;
	public Test(){}
	public Test(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name,this.age);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return  true;
		if (obj instanceof Test) {
			Test test = (Test) obj;
			return test.age == this.age && test.name.equals(this.getName());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Test{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}

	public static void main(String[] args) {
		Test a = new Test("小明", 1);
		Test b = new Test("小红", 3);
		Test c = new Test("小华", 2);
		Test d = new Test("小白", 4);
		List<Test> lists = new ArrayList<>();
		lists.add(a);
		lists.add(b);
		lists.add(c);
		lists.add(d);
		// filter使用 collect 使用
		List<Test> collect = lists.stream().filter((item) -> item.getAge() > 2).collect(Collectors.toList());
		lists.forEach(item -> System.out.println(item));
		collect.forEach(item -> System.out.println(item));
		System.out.println("---------------");
		// limit使用
		lists.stream().limit(2).forEach(item -> System.out.println(item));
		// sort使用
		lists.stream().sorted((o1, o2) -> o1.getAge() < o2.getAge() ? -1 : ((o1.getAge() == o2.getAge() ? 0 : 1))).forEach(item -> System.out.println(item));
		// reduce
		List<Integer> list = Arrays.asList(1,2,3,4,5);
		Integer sum = list.stream().reduce(0, (x, y) -> x + y);
		System.out.println(sum);
		// skip 删除
		list.stream().skip(3).forEach(System.out::println);
		// min使用
		System.out.println("----------------------");
		List<Test> collect1 = lists.stream().map((item) -> {
			String s = item.getName() + "地址";
			return new Test(s, item.getAge());
		}).collect(Collectors.toList());
		collect1.forEach(System.out::println);
		BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		System.out.println(encoder.encode("bobobod"));
	}

}