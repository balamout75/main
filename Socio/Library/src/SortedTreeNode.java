/*
 * Created on 18/10/2004
 *
 * Copyright (C) 2004 .useful community. All rights reserved.
 * ====================================================================
 * The Software License (based on Apache Software License, Version 1.1)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        .useful community (http://dotuseful.sourceforge.net/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "dot useful" and "dot useful community" must not be used to
 *    endorse or promote products derived from this software without
 *    prior written permission. For written permission, please
 *    contact dkrukovsky at yahoo.com.
 *
 * 5. Products derived from this software may not be called "dot useful",
 *    nor may "dot useful" appear in their name, without prior written
 *    permission of .useful community.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DOTUSEFUL COMMUNITY OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */


import java.util.Collections;
import java.util.Comparator;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.MutableTreeNode;

/**
 * A tree node which keeps its children sorted. It must be supplied with
 * <code>Comparator</code> by which the list is ordered, or its children must
 * implement <code>Comparable</code> interface.
 * <p>
 * <code>SortedTreeNode</code> must be notified on its child's change which
 * could have effect on child's sorting order. <code>SortedTreeNode</code>
 * must be notified on change BEFORE some other operation which depend on
 * children order or could have effect on sorting order.
 * 
 * @author dkrukovsky
 *  
 */
public class SortedTreeNode extends AutomatedTreeNode {
	/**
	 * The <code>Comparator</code> by which the list is ordered.
	 */
	protected Comparator comparator;

	/**
	 * Creates a <code>SortedTreeNode</code> that has no parent and no
	 * children, but which allows children. Its children must implement
	 * <code>Comparable</code> interface if no comparator is set.
	 */
	public SortedTreeNode() {
		this(null);
	}

	/**
	 * Creates a <code>SortedTreeNode</code> node with no parent, no children,
	 * but which allows children, and initializes it with the specified user
	 * object. Its children must implement <code>Comparable</code> interface
	 * if no comparator is set.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 */
	public SortedTreeNode(Object userObject) {
		this(userObject, null);
	}

	/**
	 * Creates a <code>SortedTreeNode</code> with no parent, no children,
	 * initialized with the specified user object, and that allows children only
	 * if specified. Its children must implement <code>Comparable</code>
	 * interface if no comparator is set.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param allowsChildren
	 *            if true, the node is allowed to have child nodes -- otherwise,
	 *            it is always a leaf node
	 */
	public SortedTreeNode(Object userObject, boolean allowsChildren) {
		this(userObject, allowsChildren, null);
	}

	/**
	 * Creates a <code>SortedTreeNode</code> node with no parent, no children,
	 * but which allows children, initializes it with the specified user object,
	 * and sets the comparator by which its children are ordered.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param newComparator
	 *            The comparator by which its children are ordered. A
	 *            <tt>null</tt> value indicates that the elements' <i>natural
	 *            ordering </i> should be used.
	 */
	public SortedTreeNode(Object userObject, Comparator newComparator) {
		this(userObject, true, newComparator);
	}

	/**
	 * Creates a <code>SortedTreeNode</code> with no parent, no children,
	 * initialized with the specified user object, that allows children only if
	 * specified, and sets the comparator by which its children are ordered.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param allowsChildren
	 *            if true, the node is allowed to have child nodes -- otherwise,
	 *            it is always a leaf node
	 * @param newComparator
	 *            The comparator by which its children are ordered. A
	 *            <tt>null</tt> value indicates that the elements' <i>natural
	 *            ordering </i> should be used.
	 */
	public SortedTreeNode(Object userObject, boolean allowsChildren,
			Comparator newComparator) {
		super(userObject, allowsChildren);
		comparator = newComparator;
	}

	/**
	 * Sets the comparator for this node to <code>newComparator</code>, and
	 * re-sorts children.
	 * 
	 * @param newComparator
	 *            The comparator to set.
	 */
	public void setComparator(Comparator newComparator) {
		comparator = newComparator;
		sortChildren(children.toArray());
	}

	/**
	 * Removes <code>newChild</code> from its parent and makes it a child of
	 * this node by adding it to this node's child array in a sorted order.
	 * 
	 * @see #insert
	 * @param newChild
	 *            node to add as a child of this node
	 * @exception IllegalArgumentException
	 *                if <code>newChild</code> is null
	 * @exception IllegalStateException
	 *                if this node does not allow children
	 */
	public void add(final MutableTreeNode newChild) {
		if (newChild != null && newChild.getParent() == this) {
			remove(newChild);
		}
		int index;
		if (children == null) {
			index = 0;
		} else {
			index = Collections.binarySearch(children, newChild, comparator);
		}
		if (index < 0) {
			index = -index - 1;
		}
		insert(newChild, index);
	}

	/**
	 * The node need to re-sort its changed children.
	 */
	public void treeNodesChanged(TreeModelEvent e) {
		super.treeNodesChanged(e);
		if (e.getTreePath().getLastPathComponent() == this) {
			sortChildren(e.getChildren());
		}
	}

	/**
	 * The node need to re-sort its changed children.
	 */
	public void treeStructureChanged(TreeModelEvent e) {
		super.treeStructureChanged(e);
		if (e.getTreePath().getLastPathComponent() == this) {
			sortChildren(children.toArray());
		}
	}

	/**
	 * <p>
	 * Keeps children in sorted order by removing given children and adding them
	 * back.
	 * </p>
	 */
	protected void sortChildren(Object[] changedChildren) {
		int cCount = changedChildren.length;
		if (cCount > 0) {
			int counter;
			for (counter = 0; counter < cCount; counter++) {
				remove((MutableTreeNode) (changedChildren[counter]));
			}
			for (counter = 0; counter < cCount; counter++) {
				add((MutableTreeNode) (changedChildren[counter]));
			}
		}
	}
}