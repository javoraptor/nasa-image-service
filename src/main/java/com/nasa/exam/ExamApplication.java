package com.nasa.exam;

import java.util.List;

import com.nasa.utils.Utils;


public class ExamApplication {

	public static void main(String[] args) {
		doSomethingAfterStartup();
	}

	final static void doSomethingAfterStartup() {
		List<String> dateList = Utils.readFileIntoDateArray("src/MarsDates.txt");

		dateList.forEach(e -> Utils.makeRestCall(e));
	}
}
