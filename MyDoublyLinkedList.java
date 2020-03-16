//Andrew Phillips CSC 364-001
//Assignment 1 MyDoublyLinkedList
//This program creates a class MyDoublyLinkedList which implements a Doubly Circular Linked List as well as an iterator
//for it. The class is a subclass of MyAbstractSequentialClass and inherits methods from MyAbstractSequentialList,
//MyAbstractList and MyList.

import java.util.*;

public class MyDoublyLinkedList<E> extends MyAbstractSequentialList<E> implements Cloneable
{
    private Node <E> head = new Node<>(null); //Dummy head node

    class Node<E>
    {
        E element;
        private Node<E> next;
        private Node<E> prev;

        //Single arg constructor for a new node taking the data to be stored as a parameter.
        public Node(E e)
        {
            element = e;
        }
    }

    @Override
    //Returns the first index of the node containing the specified element
    public int indexOf(E e)
    {
        Node<E> node = head.next;
        if (e == null)
        {
            for (int i = 0; i < size; i++)
            {
                if (node.element == null)
                    return i;
                else
                    node = node.next;
            }
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                if (e.equals(node.element))
                    return i;
                else
                    node = node.next;
            }
        }
        return -1;
    }

    @Override
    //Represents the contents of the list as a String via StringBuilder
    public String toString()
    {
        StringBuilder sb = new StringBuilder("[");
        Node <E> curr = head.next;
        String punct;
        for(int i = 0; i < size; i++)
        {
            sb.append(curr.element);
            curr = curr.next;
            //Circumvents an issue where the last element in the list would have a trailing comma.
            punct = (curr != head) ? ", " : "]";
            sb.append(punct);
        }
        return sb.toString();
    }

    @Override
    public E getFirst()
    {
        if (size == 0)
            return null;
        else
            return head.next.element;
    }

    @Override
    public E getLast()
    {
        if (size == 0)
            return null;
        else
            return head.prev.element;
    }

    @Override
    /*Logic differs depending on if there is a node(s) present in the list or not. If so, a swap using a temp
    variable to hold the first node in the list is necessary.*/
    public void addFirst(E e)
    {
        Node<E> alphaNode = new Node<>(e);
        if (size == 0)
        {
            head.next = alphaNode;
            head.prev = alphaNode;
            alphaNode.prev = head;
            alphaNode.next = head;
        }
        else
        {
            Node <E> temp = head.next;
            head.next = alphaNode;
            alphaNode.next = temp;
            alphaNode.prev = head;
            temp.prev = alphaNode;
        }
        size++;
    }

    @Override
    //Similar to the situation with the addFirst method, size is evaluated to see if a temp swap is necessary
    public void addLast(E e) {
        Node<E> omegaNode = new Node<>(e);
        if (size == 0)
        {
            head.next = omegaNode;
            head.prev = omegaNode;
            omegaNode.prev = head;
            omegaNode.next = head;
        }
        else
        {
            Node <E> temp = head.prev;
            head.prev = omegaNode;
            temp.next = head.prev;
            omegaNode.next = head;
            omegaNode.prev = temp;
        }
        size++;
    }

    @Override
    public E removeFirst()
    {
        if (size == 0)
            throw new NoSuchElementException();
        Node <E> alphaNode = head.next;
        Node <E> temp = alphaNode.next;
        head.next = temp;
        temp.prev = head;
        size--;
        return alphaNode.element;
    }

    @Override
    public E removeLast()
    {
        if (size == 0)
            throw new NoSuchElementException();
        Node <E> omegaNode = head.prev;
        Node <E> temp = omegaNode.prev;
        head.prev = temp;
        temp.next = head;
        size--;
        return omegaNode.element;
    }

    @Override
    public java.util.ListIterator<E> listIterator(int index)
    {
        return new MyDoublyLinkedListIterator(index);
    }

    @Override
    public Object clone()
    {
        try
        {
            MyDoublyLinkedList dll_clone = (MyDoublyLinkedList) super.clone();
            Node<E> c_head = new Node<>(null);
            c_head.next = c_head;
            c_head.prev = c_head;
            dll_clone.size = 0;
            dll_clone.head = c_head;
            for (int i = 0; i < size; i++)
                dll_clone.add(get(i));
            return dll_clone;
        }

        catch (CloneNotSupportedException ex)
        {
            return null;
        }
    }

    @Override
    //With no specified parameter for position, the implementation is the same as the addLast method.
    public void add(E e) {
        addLast(e);
    }

    @Override
    public void add(int index, E e)
    {
        Node<E> node = new Node<>(e);
        Node<E> curr = head.next;
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        else if (index == 0)
        {
            addFirst(e);
        }
        else if (index == size)
        {
            addLast(e);
        }
        else
        {
            //Already checked for index 0 of the list above
            for (int i = 1; i < index; i++)
            {
                curr = curr.next;
            }
            Node<E> temp = curr.next;
            node.prev = curr;
            node.next = temp;
            curr.next = node;
            temp.prev = node;
            size++;
        }
    }

    @Override
    //Deleting nodes in a linked list is done by moving the pointers.
    public void clear()
    {
        head.next = head;
        head.prev = head;
        size = 0;
    }

    public boolean contains(E e)
    {
        Node<E> node = head.next;
        if (e == null)
        {
            for (int i = 0; i < size; i++)
            {
                if (node.element == null)
                    return true;
                else
                    node = node.next;
            }
        }
        else
        {
            for (int i = 0; i < size; i++) {
                if (node.element.equals(e))
                    return true;
                else
                    node = node.next;
            }
        }
        return false;
    }

    public E get(int index) {
        Node<E> node = head.next;
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        for (int i = 0; i < size; i++) {
            if (i == index)
                return node.element;
            else
                node = node.next;
        }
        return null;
    }

    //Similar to IndexOf, however will traverse the list starting at the last index.
    public int lastIndexOf(E e) {
        Node<E> node = head.prev;
        if (e == null)
        {
            for (int i = size; i > 0; i--)
            {
                if (node.element == null)
                    return i-1;
                else
                    node = node.prev;
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                if (e.equals(node.element))
                    return i-1;
                else
                    node = node.prev;
            }
        }
        return -1;
    }

    public boolean remove(E e)
    {
        Node <E> node = head.next;
        Node <E> temp1, temp2;
        for (int i = 0; i < size; i++)
        {
            if (e == node.element)
            {
                temp1 = node.prev;
                temp2 = node.next;
                temp1.next = temp2;
                temp2.prev = temp1;
                size--;
                return true;
            }
            else
                node = node.next;
        }
        return false;
    }

    public E remove(int index) {
        Node<E> node = head.next;
        Node <E> temp1, temp2;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        else if (index == 0)
        {
            removeFirst();
        }
        else if (index == size)
        {
            removeLast();
        }
        else
        {
            for (int i = 1; i < index; i++)
            {
                node = node.next;
            }
            temp1 = node;
            temp2 = node.next.next;
            temp1.next = temp2;
            temp2.prev = temp1;
            size--;
            return node.element;
        }
        return null;
    }

    //Similar logic to add, however contents of the node in position index are replaced instead of adding a new node.
    public Object set(int index, E e) {
        Node<E> node = head.next;
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        else
        {
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            node.element = e;
        }
        return node;
    }

    //Getter method for the size variable, defined in the grandparent class, MyAbstractList
    public int size()
    {
        return size;
    }

    public boolean equals(Object other)
    {
        if (this == other)
            return true;
        else if (!(other instanceof MyList))
            return false;
        else if (((MyList<?>)other).size() != size())
            return false;
        else
        {
            java.util.Iterator<E> thisIter = this.iterator();
            java.util.Iterator<?> otherIter = ((MyList<?>)other).iterator();
            while (thisIter.hasNext())
            {
                E nextE = thisIter.next();
                Object otherE = otherIter.next();
                if (!(nextE == null ? otherE == null : nextE.equals(otherE)))
                    return false;
            }
            return true;
        }
    }

    private class MyDoublyLinkedListIterator implements java.util.ListIterator<E>
    {
        private Node <E> curr;
        private int indexOfNext;
        private Node <E> lastReturned = null;

        private MyDoublyLinkedListIterator(int index)
        {
            curr = head;
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException();
            else
            {
                for(int i = 0; i < index; i++)
                    curr = curr.next;
            }
            indexOfNext = index;
        }

        @Override
        /*lastReturned is used to ensure remove() is not called twice in a row, or called without a call to either
        next() or previous() first.*/
        public void remove()
        {
            if (lastReturned == null)
                throw new IllegalStateException();
            else
            {
                if (lastReturned.equals(curr.next))
                    curr = curr.next.next;
                else
                    indexOfNext--;
            }
            lastReturned.prev.next = lastReturned.next;
            lastReturned.next.prev = lastReturned.prev;
            lastReturned = null;
            size--;
        }

        @Override
        public boolean hasNext()
        {
            return indexOfNext < size;
        }

        @Override
        public E next()
        {
            if(!hasNext())
                throw new NoSuchElementException();
            lastReturned = curr.next;
            curr = curr.next;
            indexOfNext++;
            return lastReturned.element;
        }


        @Override
        public void add(E e)
        {
            Node <E> node = new Node<>(e);
            Node <E> temp1, temp2;
            if(size == 0)
            {
                head.next = node;
                head.prev = node;
                node.prev = head;
                node.next = head;
                curr = node;
                size++;
            }
            else
            {
                for(int i = 0; i < nextIndex(); i++)
                {
                    curr = curr.next;
                }
                temp1 = curr.prev;
                temp2 = curr;
                node.prev = temp1;
                node.next = temp2;
                temp1.next = node;
                temp2.prev = node;
                size++;
                indexOfNext++;
                lastReturned = null;
                curr = node;
            }
        }

        @Override
        //Must call next() or previous() first
        public void set(E e)
        {
            if(lastReturned == null)
                throw new IllegalStateException();
            else
                lastReturned.element = e;
        }

        @Override
        public boolean hasPrevious()
        {
            return indexOfNext > 0;
        }

        @Override
        public E previous()
        {
            if(!hasPrevious())
                throw new NoSuchElementException();
            lastReturned = curr;
            curr = curr.prev;
            indexOfNext--;
            return lastReturned.element;
        }

        @Override
        public int nextIndex()
        {
            return indexOfNext;
        }

        @Override
        public int previousIndex()
        {
            return (indexOfNext - 1);
        }
    }
}
