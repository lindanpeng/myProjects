package com.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MessageUtilTest {
@Test
public void testToString(){
List<String> str=new ArrayList<>();

System.out.println(str.toString());
}
@Test
public void testFormUserList(){
	List<String> str=new ArrayList<>();
	str.add("lindan");
	str.add("lin");
	String s=str.toString();
	System.out.println(s.substring(1,s.length()-1).split(", ")[0]);
}

}
