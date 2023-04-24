package khantorecrm.utils.constants;

import khantorecrm.model.User;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
public class Statics {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isNonDeletable(long time) {
        long minute = System.currentTimeMillis() - time;
        minute /= 60 * 1000L;
        return minute > Constants.DELETE_TIME;
    }

    public static Timestamp getTime(String time) throws TypesInError {
        if (time == null) return null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new TypesInError("Date format is not correct");
        }
    }
}
