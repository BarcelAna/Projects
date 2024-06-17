package hr.fer.oprpp2.custom.scripting.exec;

import java.util.function.DoubleBinaryOperator;

import static java.lang.Double.isFinite;

/**
 * Class ValueWrapper is a wrapper for any kind of objects, including null.
 * This class allows as to perform operations on ValueWrapper objects, such as add, subtract, multiply and divide but only if
 * wrapped object are of type Integer, Double or String parsable to Integer or Double.
 * It is also possible to compare ValueWrapper objects, but also with the restriction on numeric types of objects.
 */
public class ValueWrapper {
    /**
     * wrapped value
     */
    private Object value;

    /**
     * Constructor which accepts value that should be wrapped.
     * @param value
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Adds given object value to the currently stored value.
     * Operation can be performed only if both values are numeric or can be parsed to numeric type of object.
     * @param incValue
     */
    public void add(Object incValue) {
       value = performOperation(value, incValue, (n1, n2)->n1+n2);
    }

    /**
     * Subtracts given object value from the currently stored value.
     * Operation can be performed only if both values are numeric or can be parsed to numeric type of object.
     * @param decValue
     */
    public void subtract(Object decValue) {
        value = performOperation(value, decValue, (n1, n2)->n1-n2);
    }

    /**
     * Multiplies given object value to the currently stored value.
     * Operation can be performed only if both values are numeric or can be parsed to numeric type of object.
     * @param mulValue
     */
    public void multiply(Object mulValue) {
        value = performOperation(value, mulValue, (n1, n2)->n1*n2);
    }

    /**
     * Devides currently stored value with the given value.
     * Operation can be performed only if both values are numeric or can be parsed to numeric type of object.
     * @param divValue
     */
    public void divide(Object divValue) {
        value = performOperation(value, divValue, (n1, n2)->n1/n2);
    }

    /**
     * Compares currently stored value to the given value.
     * Returns number less than 0 if stored value is smaller, 0 if values are the same or number greater than 0 if stored value
     * is greater than the given value.
     * @param withValue
     * @return 1, -1 or 0
     */
    public int numCompare(Object withValue) {
        return performOperation(value, withValue, (n1, n2)-> Double.compare(n1, n2))
                .intValue();
    }

    /**
     * Returns wrapped value.
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets wrapped value to the given value.
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Performs given operation on given objects and returns the result.
     * If any of given object is Double, result is also Double.
     * Otherwise, result is Integer.
     * @param value
     * @param other
     * @param op
     * @return operation result
     * @throws RuntimeException - if given objects are not correct type or if user tries to divide by zero.
     */
    private Number performOperation(Object value, Object other, DoubleBinaryOperator op) {
        checkValueType(value);
        checkValueType(other);

        Number n1 = setType(value);
        Number n2 = setType(other);

        Number result = op.applyAsDouble(n1.doubleValue(), n2.doubleValue());

        if(!isFinite((Double) result)) {
            throw new RuntimeException("Division by 0.");
        }
        if(n1 instanceof Double || n2 instanceof Double) {
            return result;
        }
        return result.intValue();

    }

    /**
     * Utility function to check if object is of correct type.
     * @param value
     * @throws RuntimeException - if object does not have correct type
     */
    private void checkValueType(Object value) {
        if(value==null || value instanceof Integer || value instanceof Double || value instanceof String) {
            return;
        }
        throw new RuntimeException("Value can be instance of Integer, Double, String or null, but it is " + value.getClass().getName());
    }

    /**
     * Converts the given object to the appropriate object of class Number.
     * If given object is a String, then it is parsed to the Double if String object contains "." or "E" or to Integer.
     * Otherwise, object is cast to the appropriate Number subclass.
     * @param value
     * @return
     */
    private Number setType(Object value) {
        if(value==null) {
            return Integer.valueOf(0);
        } else if(value instanceof String) {
            if(((String) value).contains(".") || ((String) value).contains("E")) {
                try{
                    return Double.parseDouble((String) value);
                } catch(NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try{
                    return Integer.parseInt((String)value);
                }catch(NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(value instanceof Integer) {
            return (Integer)value;
        } else {
            return (Double)value;
        }
    }

}
