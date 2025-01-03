/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.util;

import org.kosa.bookmanagement.model.dto.RentDTO;

import java.util.Calendar;

import java.sql.Date;

public class RentUtils {

    // 반납 예정일 계산 메서드
    public static Date calculateReturnDate(RentDTO rent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rent.getRentDate());
        int daysToAdd = 14 * (rent.getExtended() + 1); // 기본 2주 + 연장된 2주 * 연장 횟수
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new Date(calendar.getTimeInMillis());
    }

    // 연체 여부를 결정하는 메서드
    public static boolean isOverdue(RentDTO rent) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date returnDate = calculateReturnDate(rent);
        return currentDate.after(returnDate);
    }
}
