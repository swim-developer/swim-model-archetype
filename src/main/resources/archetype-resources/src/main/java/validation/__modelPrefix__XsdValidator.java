#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.validation;

public class ${modelPrefix}XsdValidator {

    private final ${modelPrefix}UnmarshallerPool pool = new ${modelPrefix}UnmarshallerPool();

    public void validateAndUnmarshal(String xml) throws ValidationException {
        try {
            pool.unmarshalAndValidate(xml);
        } catch (${modelPrefix}UnmarshallerPool.${modelPrefix}UnmarshalException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
