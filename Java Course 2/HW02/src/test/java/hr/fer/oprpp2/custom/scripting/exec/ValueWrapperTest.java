package hr.fer.oprpp2.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueWrapperTest {

    @Test
    public void testConstructor() {
        ValueWrapper vw = new ValueWrapper(Integer.valueOf(1));
        assertEquals(Integer.class.getName(), vw.getValue().getClass().getName());
    }

    @Test
    public void getValueTest() {
        ValueWrapper vw = new ValueWrapper((Integer.valueOf(1)));
        assertEquals(1, vw.getValue());
    }

    @Test
    public void setValueTest() {
        ValueWrapper vw = new ValueWrapper(Integer.valueOf(1));
        vw.setValue("Ana");
        assertEquals("Ana", vw.getValue());
    }

    @Test
    public void addWithNull() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue());
        assertEquals(0, v1.getValue());
        assertEquals(Integer.class.getName(), v1.getValue().getClass().getName());
    }

    @Test
    public void stringConversionTest() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(0));
        v1.add(v2.getValue());
        assertEquals(12.0, v1.getValue());
        assertEquals(Double.class.getName(), v1.getValue().getClass().getName());
    }

    @Test
    public void addDoubleAndInteger(){
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
        v1.add(v2.getValue());
        assertEquals(13.0, v1.getValue());
        assertEquals(Double.class.getName(), v1.getValue().getClass().getName());
    }

    @Test
    public void addIntegerAndInteger(){
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
        v1.add(v2.getValue());
        assertEquals(13, v1.getValue());
        assertEquals(Integer.class.getName(), v1.getValue().getClass().getName());
    }

    @Test
    public void exceptionTest(){
        ValueWrapper v1 = new ValueWrapper("Ana");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
        assertThrows(RuntimeException.class, ()->{
            v1.add(v2.getValue());
        });
    }

    @Test
    public void divWithZeroTest(){
        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(0));
        assertThrows(RuntimeException.class, ()->{
            v1.divide(v2.getValue());
        });
    }

    @Test
    public void compareGreaterTest() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
        v1.add(v2.getValue());
        assertTrue(v1.numCompare(v2.getValue())>0);
    }

    @Test
    public void compareLessTest() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
        assertTrue(v1.numCompare(v2.getValue())<0);
    }

    @Test
    public void compareEqualsTest() {
        ValueWrapper v1 = new ValueWrapper("-1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(-1));
        assertEquals(0, v1.numCompare(v2.getValue()));
    }

    @Test
    public void compareEqualsWithNullTest() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        assertEquals(0, v1.numCompare(v2.getValue()));
    }

    @Test
    public void compareExceptionTest() {
        ValueWrapper v1 = new ValueWrapper("Ana");
        ValueWrapper v2 = new ValueWrapper("Sara");
        assertThrows(RuntimeException.class,()-> v1.numCompare(v2));
    }

}