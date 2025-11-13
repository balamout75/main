import javax.swing.tree.DefaultMutableTreeNode;
public class ABSortTreeNode extends SortedTreeNode implements Comparable {
        public ABSortTreeNode(Object o) {
            super(o);
        }
        public int compareTo(Object o) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)o;
            return ((Comparable)getUserObject()).compareTo(node.getUserObject());
        }
    }