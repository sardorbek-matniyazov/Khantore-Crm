package khantorecrm.utils.constants;

import khantorecrm.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
public class Statics {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
