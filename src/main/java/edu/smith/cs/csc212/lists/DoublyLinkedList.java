package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.*;

/**
 * A Doubly-Linked List is a list based on nodes that know of their successor and predecessor.
 * @author jfoley
 *
 * @param <T>
 */
public class DoublyLinkedList<T> extends ListADT<T> {
	/**
	 * This is a reference to the first node in this list.
	 */
	Node<T> start;
	/**
	 * This is a reference to the last node in this list.
	 */
	Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removing = this.start.value;
		if(this.start.after == null) {
			this.start = null;
			this.end = null;
			return removing;
			
		}
		this.start.after.before= null;
		this.start = this.start.after;
		return removing;
		
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removing = this.end.value;
		
		if(this.end.before == null) {  // if there is only one thing in the list
			this.end = null;   // there is no front or back
			this.start = null;
			return removing;
		}
		
		this.end.before.after =null;   // erase the pointer pointing to the last node from the penultima node
		this.end= this.end.before;    // the end is now the penultima node
		return removing;
	}

	// comment to test
	@Override
	public T removeIndex(int index) {
		checkNotEmpty();

		if(index == this.size()-1) {
			return this.removeBack();
		}
		if(index == 0) {
			return this.removeFront();
		}
		int iCounter= 0;
		T removedIndex = this.start.value; // just a placeholder
		for( Node<T> n = this.start; n !=null ; iCounter++, n=n.after) {
			if(iCounter == index -1) {
				removedIndex = n.after.value;  // value you are gonna remove is after
				n.after = n.after.after;
				n.after.before = n; 
				return removedIndex;
				
			}
		}
		
		throw new BadIndexError(index);  // if it passes the loop without returning it is a bad index
		
	}

	@Override
	public void addFront(T item) {
		//checkNotEmpty();
		if(this.start == null) {
			Node<T> newNode = new Node<T>(item);
			this.start = newNode;
			this.end = newNode;
			return;
		}
		Node<T> secondItem = this.start;  // hold on to the now second item on the list
		System.out.println(secondItem);
		this.start = new Node<T>(item);  // create a new node at the start with the item
		this.start.after = secondItem;   // set the pointer after the first item to the second item
		this.start.after.before=this.start;  // set the before pointer to the second item to be the first
	}

	@Override
	public void addBack(T item) {
		if (end == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondLast = end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		checkNotEmpty();
		int iCounter=0;
		if(index == 0) {
			this.addFront(item);
			return;
		}
		if(index == this.size()) {  // no minus one because if we are adding to the last we need index after size-1
			this.addBack(item);
			return;
		}
		
		for( Node<T> n = this.start;n != null; n = n.after, iCounter ++) {
			if(iCounter == index -1) {
				Node<T> newNode = new Node<T>(item);
				Node<T> pushedNode = n.after;
				n.after = newNode;            // before index node's after is the new node
				newNode.before = n;          // the new nodes before is n where we stopped
				newNode.after = pushedNode;   // new nodes after was our current n after node
				newNode.after.before = newNode; // the node after the new node before has to point to the new node
				return;
			}
		}
		throw new BadIndexError(index);  // if we went through forloop and it didnt return then we had bad index
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return this.end.value;
	}
	
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		
		T returnVal = this.start.value;
		int iCounter=0;
		for(Node<T> n= this.start; n!= null; n = n.after, iCounter++) {
			if(iCounter == index) {
				returnVal = n.value;
			}
		}
		return returnVal;
	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		int iCounter = 0;
		for(Node<T> n = this.start; n != null; n = n.after, iCounter ++) {
			if(iCounter == index) {
				n.value = value;
			}
		}
	}

	@Override
	public int size() {
		
		int iCounter=0;
		for(Node<T> n = this.start; n != null ; n= n.after ) {
			iCounter += 1;
		}
		return iCounter;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}
}
