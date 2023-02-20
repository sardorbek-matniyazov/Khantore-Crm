package khantorecrm.service.impl;

import khantorecrm.payload.dao.OwnResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 20 Feb 2023
 **/
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InputServiceTest {

    @Autowired
    @Mock
    InputService inputService;

    @Test
    void delete() {
        final OwnResponse delete = inputService.delete(1L);

        // assertion expected value
        assertThat(delete.getMessage(), is(OwnResponse.DELETED_SUCCESSFULLY.getMessage()));
    }
}