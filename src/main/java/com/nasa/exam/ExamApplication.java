package com.nasa.exam;

import java.util.List;

import com.nasa.utils.Utils;


public class ExamApplication {

	public static void main(String[] args) {
		ImageServiceImpl.executeMultipleRestCalls(ImageServiceImpl.fetchDateList());
	}
}
