package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.*;

/**
 * A Doubly-Linked List is a list based on nodes that know of their successor and predecessor.
 * @author jfoley
 * updated by:
 * @author adrianabeltran
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
	 * This is the size of the linked list
	 * To make code more efficient and not have to look through the list all the time 
	 * Also enables the feature startingAtTheFront()
	 */
	int size;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
		this.size =0;
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
		if(this.start.after == null) {   // if there is only one thing in the list
			this.start = null;      // the start and end pointer will be null
			this.end = null;
			this.size -=1;         // one less size bc removing
			return removing;
			
		}
		this.start.after.before= null;    // the pointer in the second node looking before is null
		this.start = this.start.after;     // the start pointer now looks at the second node
		this.size-=1;                     // delete one to size because we are getting rid of one.
		return removing;
		
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removing = this.end.value;
		
		if(this.end.before == null) {  // if there is only one thing in the list
			this.end = null;   // there is no front or back
			this.start = null;
			this.size -= 1;       // delete one from size because we are removing
			return removing;
		}
		
		this.end.before.after =null;   // erase the pointer pointing to the last node from the penultima node
		this.end= this.end.before;    // the end is now the penultima node
		this.size -= 1;       // delete one from size because we are removing
		return removing;
	}

	// comment to test
	@Override
	public T removeIndex(int index) {
		checkNotEmpty();

		if(index == this.getSize()-1) {
			return this.removeBack();
		}
		if(index == 0) {
			return this.removeFront();
		}
		
		int iCounter= 0;
		T removedIndex = this.start.value; // just a placeholder
		if(startingAtTheFront(index)) {
			for( Node<T> n = this.start; n !=null ; iCounter++, n=n.after) {
				if(iCounter == index -1) {
					removedIndex = n.after.value;  // value you are gonna remove is after
					n.after = n.after.after;
					n.after.before = n; 
					this.size -=1;
					return removedIndex;
					
				}
			}
		}
		else {
			iCounter = getSize();
			for( Node<T> n = this.end; n !=null ; iCounter--, n=n.before) {// if its faster to start from the back then icounter is size and you go to before not after
				if(iCounter == index -1) {  // this will all be the same
					removedIndex = n.after.value;  // value you are gonna remove is after
					n.after = n.after.after;
					n.after.before = n; 
					this.size -=1;
					return removedIndex;
					
				}
			}
		}

		
		throw new BadIndexError(index);  // if it passes the loop without returning it is a bad index
		
	}

	@Override
	public void addFront(T item) {
		//checkNotEmpty();
		if(this.start == null) {                // if there is nothing in the linked list
			Node<T> newNode = new Node<T>(item);     // create a new node
			this.start = newNode;                      // set both start and end pointers to the new node
			this.end = newNode;
			this.size +=1;                            // add one to the size
			return;                               // return to not go any further
		}
		Node<T> secondItem = this.start;  // hold on to the now second item on the list
		System.out.println(secondItem);
		this.start = new Node<T>(item);  // create a new node at the start with the item
		this.start.after = secondItem;   // set the pointer after the first item to the second item
		this.start.after.before=this.start;  // set the before pointer to the second item to be the first
		this.size +=1;                            // add one to the size
	}

	@Override
	// JJFoley wrote this method, Me,Adriana, are just commenting on what his approach was
	public void addBack(T item) {
		if (end == null) {   // if there is nothing in the linked list
			start = end = new Node<T>(item);    // start and end equal the new node
			this.size+=1;                        // add 1 to the size because you are adding
		} else {
			Node<T> secondLast = end;    // hold onto the current last node
			end = new Node<T>(item);     //c reate a new node 
			end.before = secondLast;     // put the before pointer to the new end equal to the old last node
			secondLast.after = end;      // set the end pointer to the new node you made
			this.size +=1;               // add 1 to the size because you are adding
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
		if(index == this.getSize()) {  // no minus one because if we are adding to the last we need index after size-1
			this.addBack(item);
			return;
		}
		if(startingAtTheFront(index)) {   // check if it will be faster to go from the front or from the back
			for( Node<T> n = this.start;n != null; n = n.after, iCounter ++) {
				if(iCounter == index -1) {
					Node<T> newNode = new Node<T>(item);
					Node<T> pushedNode = n.after;
					n.after = newNode;            // before index node's after is the new node
					newNode.before = n;          // the new nodes before is n where we stopped
					newNode.after = pushedNode;   // new nodes after was our current n after node
					newNode.after.before = newNode; // the node after the new node before has to point to the new node
					this.size +=1;
					return;
				}
			}
		}else {
			iCounter= getSize();
			for( Node<T> n = this.end;n != null; n = n.before, iCounter --) {
				if(iCounter == index -1) {        // I think if I keep index -1 I dont have to change anything after because icounter and index will both be one before where I want to add
					Node<T> newNode = new Node<T>(item);
					Node<T> pushedNode = n.after;
					n.after = newNode;            // before index node's after is the new node
					newNode.before = n;          // the new nodes before is n where we stopped
					newNode.after = pushedNode;   // new nodes after was our current n after node
					newNode.after.before = newNode; // the node after the new node before has to point to the new node
					this.size +=1;
					return;
				}
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
	// Originally written by JJFoley but I added the if statement and modified the second part of his code for the else
	public T getIndex(int index) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		
		T returnVal = this.start.value;
		int iCounter=0;
		if(startingAtTheFront(index)) {     
			for(Node<T> n= this.start; n!= null; n = n.after, iCounter++) {
				if(iCounter == index) {
					returnVal = n.value;
				}
			}
			return returnVal;
		}else {
			iCounter = getSize();
			for(Node<T> n= this.end; n!= null; n = n.before, iCounter--) {
				if(iCounter == index) {
					returnVal = n.value; 
				}
			}
			return returnVal;
		}

	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		int iCounter = 0;
		if(startingAtTheFront(index)) {
			for(Node<T> n = this.start; n != null; n = n.after, iCounter ++) {
				if(iCounter == index) {  // if we find the index replace the value
					n.value = value;
				}
			}
		}else {
			iCounter = getSize();   // if it is better to start from the back then go from the end and find the index
			for(Node<T> n = this.end; n != null; n = n.before, iCounter --) {
				if(iCounter == index) {
					n.value = value;
				}
			}
		}

	}

	//@Override
	private int getSize() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}
	
	/**
	 * Check if we should check from the start or from the front. We will prioritize starting from the front if it is straight in the middle
	 * making index function more efficient
	 */
	protected boolean startingAtTheFront(int index) {
		
		float middle = this.size/2;
		
		if (index <= middle) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.size;
	}
}
