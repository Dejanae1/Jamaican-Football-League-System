import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    // V-01
    @Test
    void requireText_normalText_returnsTrimmed() throws Exception {
        assertEquals("John", Validation.requireText("  John  ", "Name"));
    }

    // V-02
    @Test
    void requireText_null_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requireText(null, "Name"));
    }

    // V-03
    @Test
    void requireText_blank_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requireText("   ", "Name"));
    }

    // V-04
    @Test
    void requireText_comma_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requireText("Smith, John", "Name"));
    }

    // V-05
    @Test
    void requireRange_atMinimum_returnsValue() throws Exception {
        assertEquals(1, Validation.requireRange(1, 1, 10, "Value"));
    }

    // V-06
    @Test
    void requireRange_atMaximum_returnsValue() throws Exception {
        assertEquals(10, Validation.requireRange(10, 1, 10, "Value"));
    }

    // V-07
    @Test
    void requireRange_belowMinimum_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requireRange(0, 1, 10, "Value"));
    }

    // V-08
    @Test
    void requireRange_aboveMaximum_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requireRange(11, 1, 10, "Value"));
    }

    // V-09
    @Test
    void requirePastDate_yesterday_returnsDate() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        assertEquals(yesterday, Validation.requirePastDate(yesterday, "Date of birth"));
    }

    // V-10
    @Test
    void requirePastDate_today_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requirePastDate(LocalDate.now(), "Date of birth"));
    }

    // V-11
    @Test
    void requirePastDate_future_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requirePastDate(LocalDate.now().plusDays(1), "Date of birth"));
    }

    // V-12
    @Test
    void requirePastDate_null_throws() {
        assertThrows(InvalidInputException.class, () -> Validation.requirePastDate(null, "Date of birth"));
    }
}
