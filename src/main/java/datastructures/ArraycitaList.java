package datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArraycitaList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ArraycitaList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ArraycitaList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        T removedElement = (T) elements[index];
        
        // Shift elements to remove the gap
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; 
        
        return removedElement;
    }

    public void clear() {
        // Clear references to help GC
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Convert to standard Java List
    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T element = (T) elements[i];
            list.add(element);
        }
        return list;
    }

    // Add method to create from a standard Java List
    public static <E> ArraycitaList<E> fromList(java.util.List<E> list) {
        ArraycitaList<E> customList = new ArraycitaList<>(list.size());
        for (E item : list) {
            customList.add(item);
        }
        return customList;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[currentIndex++];
            }
        };
    }
}
