#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ${modelPrefix}UnmarshallerPoolTest {

    private ${modelPrefix}UnmarshallerPool pool;

    @BeforeEach
    void setUp() {
        pool = new ${modelPrefix}UnmarshallerPool();
    }

    @Test
    void nullXmlThrowsUnmarshalException() {
        assertThatThrownBy(() -> pool.unmarshalAndValidate(null))
                .isInstanceOf(${modelPrefix}UnmarshallerPool.${modelPrefix}UnmarshalException.class)
                .hasMessageContaining("null or empty");
    }

    @Test
    void emptyXmlThrowsUnmarshalException() {
        assertThatThrownBy(() -> pool.unmarshalAndValidate("   "))
                .isInstanceOf(${modelPrefix}UnmarshallerPool.${modelPrefix}UnmarshalException.class)
                .hasMessageContaining("null or empty");
    }

    @Test
    void invalidXmlThrowsUnmarshalException() {
        assertThatThrownBy(() -> pool.unmarshalAndValidate("<not-valid/>"))
                .isInstanceOf(${modelPrefix}UnmarshallerPool.${modelPrefix}UnmarshalException.class);
    }

    // TODO: add parameterized tests for valid and invalid XML samples
    // Place valid XML files in src/test/resources/${modelName}-valid/
    // Place invalid XML files in src/test/resources/${modelName}-invalid/
}
