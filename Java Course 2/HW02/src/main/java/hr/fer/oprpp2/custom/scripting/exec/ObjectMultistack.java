package hr.fer.oprpp2.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMultistack represents custom data structure which behaves as collection of stacks which are mapped to the key of type String.
 * More specific, stack-like objects are represented with objects of inner class MultistackEntry.
 * Class allows typical stack operations such as push, pop or peek.
 */
public class ObjectMultistack {

    /**
     * inner data structure for mapping keys to it's stacks
     */
    Map<String, MultistackEntry> map = new HashMap<>();

    /**
     * Pushes given ValueWrapper object to the stack assigned to given key.
     * @param keyName
     * @param valueWrapper
     */
    public void push(String keyName, ValueWrapper valueWrapper) {
        MultistackEntry head = map.get(keyName);
        if(head==null) {
            head = new MultistackEntry(valueWrapper);
            map.put(keyName, head);
        } else {
            MultistackEntry newHead = new MultistackEntry(valueWrapper);
            newHead.next = head;
            map.put(keyName, newHead);
        }
    }

    /**
     * Pops one object from stack assigned to the given key.
     * @param keyName
     * @return popped ValueWrapper object
     * @throws EmptyStackException
     */
    public ValueWrapper pop(String keyName) {
        if(isEmpty(keyName)) throw new EmptyStackException();
        MultistackEntry head = map.get(keyName);
        MultistackEntry newHead = head.next;
        map.put(keyName, newHead);
        return head.value;
    }

    /**
     * Peeks one element from the stack assigned to the given key.
     * @param keyName
     * @return element from the top of the stack
     */
    public ValueWrapper peek(String keyName) {
        if(isEmpty(keyName)) throw new EmptyStackException();
        return map.get(keyName).value;
    }

    /**
     * Returns true if stack assigned to the given key is empty, false otherwise.
     * @param keyName
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty(String keyName) {
        return map.get(keyName) == null ? true : false;
    }

    /**
     * MultistackEntry represents stack-like object which is used in ObjectMultistack to represent stack of ValueWrappers.
     * Essentially, it is a node of linked list with value and pointer to the next element.
     */
    private static class MultistackEntry {
        private ValueWrapper value;
        private MultistackEntry next;

        public MultistackEntry(ValueWrapper value) {
            this.value = value;
            this.next = null;
        }
    }
}

