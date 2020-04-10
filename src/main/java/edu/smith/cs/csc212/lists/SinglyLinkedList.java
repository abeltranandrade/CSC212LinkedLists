package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * A Singly-Linked List is a list that has only knowledge of its very first
 * element. Elements after that are chained, ending with a null node.
 * 
 * @author jfoley
 *
 * @param <T> - the type of the item stored in this list.
 */
public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removing = this.start.value;   // get the value(node) we want to delete
		this.start = this.start.next;    // make the beginning 
		return removing;
	}

	@Override
	public T removeBack() {
		if(this.start == null) {// if the linked list is empty then throw error
			throw new EmptyListError();
		}else if(this.size() == 1){
			return this.removeFront();
		}
		
		T removing =this.start.value;  // made it equal to this as a placeholder
		for(Node<T> n = this.start; n != null ; n = n.next) {
			if(n.next.next == null) {   // stop at the value before the last value 
				removing = n.next.value;   // assign the value that we will remove to a var
				n.next = null;       // remove the value
				break;         //stop going through the loop 
			}
		}
		return removing;
	}

	@Override
	public T removeIndex(int index) {
		if(index == 0) {              // if it is the first index add to front
			return this.removeFront();
			
		}else if(index == this.size()-1){  // if it is the last index then remove the back
			return this.removeBack();
			
		}else if(this.size() == 0) {    // bad index error if the index is out of range
			throw new EmptyListError();
		}else if (index > this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		
		T removeValue= this.start.value;
		int iCounter = 0;
		
		for(Node<T> n = this.start; n != null; n=n.next, iCounter++) {
			if(iCounter == index-1) {
				removeValue = n.next.value;  // get the value we will be removing into a variable
				n.next = n.next.next;   // 
				break;
			}
		}
		return removeValue;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) {
		if(this.start == null) {
			this.addFront(item);
			return;
		}
		
		
		for(Node<T> n = this.start; n != null; n=n.next) {
			if(n.next == null) {  // if the node we are in has a next that is null we are at the back
				n.next = new Node<T>(item,null);
				return;    // to terminate the for loop
			}
			
		}
	}

	@Override
	public void addIndex(int index, T item) {
		int Icounter = 0;
		
		if(index == 0) {
			this.addFront(item);
			return;
			
		}else if(index == this.size()) {
			this.addBack(item);	
			return;
		}else if(index >= this.size() || index < 0) {
			throw new BadIndexError(index);
		
	}
		for(Node<T> n = this.start; n !=null; n =n.next, Icounter++ ) {
			if(Icounter != index -1 ) {
				continue;
			}else {
				Node<T> originalNextVal = n.next;
				n.next = new Node<T>(item,originalNextVal);
			}
		}
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;   // the start is just a pointer but the value 
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		T finalvalue = this.start.value;                // make a variable for where I am going to store the value in gonna return
		for(Node<T> n = this.start; n !=null; n=n.next) {    // while the current node exists(not null)
			if(n.next == null) {  // if the next node is null that means that that node is the last node
				finalvalue = n.value;   // get the value of that node 
				break;   // break off the for loop
			}
		
		}
		return finalvalue;
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		if(index >=this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		
		
		int iCounter=0;
		for(Node<T> n = this.start; n != null ; n=n.next,iCounter++) {
			if(index == iCounter) {
				n.value = value;
			}
		}
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with a friend.
		 * @param value - the value to put in it.
		 * @param next - the friend of this node.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Alternate constructor; create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.next = null;
		}
	}

}
