package TestUtil;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;

public class TestUtil {
    public static void assertThrows(CheckedFunction fn, Class<? extends Exception> exceptionType, Matcher<Exception>
            exceptionMatcher) throws Exception {
        try {
            fn.apply();
            Assert.fail();
        } catch (Exception maybeExpected) {
            if (!exceptionType.isInstance(maybeExpected)) {
                throw maybeExpected;
            }
            Assert.assertThat(maybeExpected, exceptionMatcher);
        }
    }

    public static Matcher<Exception> exceptionMessageMatches(String pattern) {
        return new BaseMatcher<Exception>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Should match pattern " + pattern);
            }

            @Override
            public boolean
            matches(Object item) {
                return ((Exception) item).getMessage().matches(pattern);
            }
        };
    }

    @FunctionalInterface
    public interface CheckedFunction {
        void apply() throws Exception;
    }

    private TestUtil() {}
}
