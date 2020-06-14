package com.example.common;

import java.time.LocalDate;

public class DateTools {

	public static LocalDate getNextDay(String year, String month, String day) {
		LocalDate date =
				LocalDate.of(
						Integer.valueOf(year),
						Integer.valueOf(month),
						Integer.valueOf(day));
		return date.plusDays(1);
	}
}
