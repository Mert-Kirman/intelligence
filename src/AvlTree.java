// Class for implementing an AVL Tree
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvlTree {
    // Nested class for the AVL Tree Node
    private static class AvlTreeNode {
        String name;    // Variable that will hold the member's name
        Double value;    // Variable that will hold GMS value
        AvlTreeNode leftChild;
        AvlTreeNode rightChild;
        int height;    // Variable to store the height of the node
        boolean counted;    // Indicates a node is marked as counted while trying to find max individual member count

        // Constructors for the AVL Tree Node
        private AvlTreeNode(String name ,Double value) {
            this(name, value, null, null, false);
        }
        private AvlTreeNode(String name, Double value, AvlTreeNode leftChild, AvlTreeNode rightChild, boolean counted) {
            this.name = name;
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.height = 0;
            this.counted = counted;
        }
    }

    // AVL Tree data fields
    private static final int IMBALANCE_LIMIT = 1;    // Allowed imbalance for the AVL Tree, which is 1
    private AvlTreeNode root;    // Root of the AVL Tree
    private final FileWriter fWriter;

    // Tree constructor
    public AvlTree(FileWriter fWriter) {
        this.root = null;
        this.fWriter = fWriter;
    }

    // Find the node with the smallest GMS value in a subtree
    private AvlTreeNode findMin(AvlTreeNode node) {
        if(node.leftChild == null) {
            return node;
        }
        return findMin(node.leftChild);
    }

    // Find the height of a node
    private int findHeight(AvlTreeNode node) {
        return node == null ? -1 : node.height;
    }

    // Public method for inserting a new member to the family
    public void insert(String name, Double value) throws IOException {
        this.root = this.insert(name, value, this.root);
    }

    // Insert a new member to the family
    private AvlTreeNode insert(String name, Double value, AvlTreeNode node) throws IOException {
        if(node == null) {
            return new AvlTreeNode(name, value);
        }
        // A new member should be welcomed by all its superiors
        fWriter.write(node.name + " welcomed " + name + "\n");
        int comparison = value.compareTo(node.value);
        if(comparison < 0) {
            node.leftChild = insert(name, value, node.leftChild);
        }
        else if(comparison > 0) {
            node.rightChild = insert(name, value, node.rightChild);
        }
        return balance(node);
    }

    // Public method for removing an existing family member
    public void remove(Double value) throws IOException {
        this.root = this.remove(value, this.root, false);
    }

    // Remove a family member
    private AvlTreeNode remove(Double value, AvlTreeNode node, boolean found) throws IOException {
        if(node == null) {
            return null;
        }

        // Check whether the value to be removed is smaller or larger than the current node's value
        int comparison = value.compareTo(node.value);
        if(comparison < 0) {
            node.leftChild = remove(value, node.leftChild, found);
        }
        else if(comparison > 0) {
            node.rightChild = remove(value, node.rightChild, found);
        }
        else if(node.leftChild != null && node.rightChild != null) {    // The node has 2 children
            // Find the node with the smallest GMS value in the right subtree
            AvlTreeNode smallestNode = findMin(node.rightChild);
            fWriter.write(node.name + " left the family, replaced by " + smallestNode.name + "\n");
            node.name = smallestNode.name;
            node.value = smallestNode.value;
            node.rightChild = remove(node.value, node.rightChild, true);
        }
        else {
            if(node.leftChild != null) {    // The node has a left child
                if(!found) {    // No welcome message has been logged for any member so far
                    fWriter.write(node.name + " left the family, replaced by " + node.leftChild.name + "\n");
                }
                node = node.leftChild;
            }
            else {    // The node has either a right child or no child
                if(node.rightChild == null && !found) {
                    fWriter.write(node.name + " left the family, replaced by nobody\n");
                }
                else {
                    if(!found) {
                        fWriter.write(node.name + " left the family, replaced by " + node.rightChild.name + "\n");
                    }
                }
                node = node.rightChild;
            }
        }

        return balance(node);
    }

    // Method to check imbalance type in the AVL Tree and apply a suitable rotation
    private AvlTreeNode balance(AvlTreeNode node) {
        if(node == null) {
            return null;
        }
        // Difference of height between the children of the current node
        int difference = findHeight(node.leftChild) - findHeight(node.rightChild);
        if(difference > IMBALANCE_LIMIT) {
            if(findHeight(node.leftChild.leftChild) >= findHeight(node.leftChild.rightChild)) {
                // Case 1 imbalance, apply a single right rotation
                node = rightRotation(node);
            }
            else {
                // Case 2 imbalance, apply a double (left-right) rotation
                node = leftRightRotation(node);
            }
        }
        else if(-1 * difference > IMBALANCE_LIMIT) {
            if(findHeight(node.rightChild.rightChild) >= findHeight(node.rightChild.leftChild)) {
                // Case 4 imbalance, apply a single left rotation
                node = leftRotation(node);
            }
            else {
                // Case 3 imbalance, apply a double (right-left) rotation
                node = rightLeftRotation(node);
            }
        }

        // Update the height of the current node (alpha node)
        node.height = Math.max(findHeight(node.leftChild), findHeight(node.rightChild)) + 1;
        return node;
    }

    // Single Right Rotation to correct a Left-Left Imbalance
    private AvlTreeNode rightRotation(AvlTreeNode n2) {
        AvlTreeNode n1 = n2.leftChild;
        n2.leftChild = n1.rightChild;
        n1.rightChild = n2;
        n2.height = Math.max(findHeight(n2.leftChild), findHeight(n2.rightChild)) + 1;
        n1.height = Math.max(findHeight(n1.leftChild), findHeight(n1.rightChild)) + 1;
        // Return the new root node for the current subtree
        return n1;
    }

    // Single Left Rotation to correct a Right-Right Imbalance
    private AvlTreeNode leftRotation(AvlTreeNode n1) {
        AvlTreeNode n2 = n1.rightChild;
        n1.rightChild = n2.leftChild;
        n2.leftChild = n1;
        n1.height = Math.max(findHeight(n1.leftChild), findHeight(n1.rightChild)) + 1;
        n2.height = Math.max(findHeight(n2.leftChild), findHeight(n2.rightChild)) + 1;
        return n2;
    }

    // Double (Left-Right) Rotation to correct a Left-Right Imbalance
    private AvlTreeNode leftRightRotation(AvlTreeNode n3) {
        n3.leftChild = leftRotation(n3.leftChild);
        return rightRotation(n3);   // Return the new root node for the current subtree
    }

    // Double (Right-Left) Rotation to correct a Right-Left Imbalance
    private AvlTreeNode rightLeftRotation(AvlTreeNode n1) {
        n1.rightChild = rightRotation(n1.rightChild);
        return leftRotation(n1);
    }

    // Find the lowest ranking member that is the superior of two given members
    public void findSuperior(Double GMS1, Double GMS2) throws IOException {
        // Lists for storing all the superiors of a member
        List<String> superiors1 = new ArrayList<>();
        List<String> superiors2 = new ArrayList<>();

        tracePath(GMS1, this.root, superiors1);
        tracePath(GMS2, this.root, superiors2);

        // A superior can only be found above both members
        if(superiors1.size() >= superiors2.size()) {
            for(int i=0; i < superiors2.size(); i++) {
                String[] superior1 = superiors1.get(superiors1.size() - superiors2.size() + i).split(" ");
                String[] superior2 = superiors2.get(i).split(" ");
                if(superior1[0].equals(superior2[0])) {
                    Double originalValue = Double.parseDouble(superior2[1]);
                    String precisionValue = String.format("%.3f", originalValue);
                    fWriter.write("Target Analysis Result: " + superior2[0] + " " + precisionValue + "\n");
                    return;
                }
            }
        }
        else {
            for(int i=0; i < superiors1.size(); i++) {
                String[] superior1 = superiors1.get(i).split(" ");
                String[] superior2 = superiors2.get(superiors2.size() - superiors1.size() + i).split(" ");
                if(superior1[0].equals(superior2[0])) {
                    Double originalValue = Double.parseDouble(superior1[1]);
                    String precisionValue = String.format("%.3f", originalValue);
                    fWriter.write("Target Analysis Result: " + superior2[0] + " " + precisionValue + "\n");
                    return;
                }
            }
        }
    }

    // Find all the superiors of a member and return it in a list
    private void tracePath(Double GMS, AvlTreeNode node, List<String> path) {
        int comparison = GMS.compareTo(node.value);
        if(comparison < 0) {
            tracePath(GMS, node.leftChild, path);
        }
        else if(comparison > 0) {
            tracePath(GMS, node.rightChild, path);
        }
        String superior = node.name + " " + node.value;
        path.add(superior);
    }

    // Print the maximum number of independent members
    public void divide() throws IOException {
        int maxNumber = maxIndependent(this.root);
        fWriter.write("Division Analysis Result: " + maxNumber + "\n");
    }

    // Find the maximum number of independent members
    private int maxIndependent(AvlTreeNode node) {
        // Base case: Count all the leaf nodes in
        if(node.leftChild == null && node.rightChild == null) {
            node.counted = true;
            return 1;
        }
        int leftMemberSum = 0;
        int rightMemberSum = 0;
        if(node.leftChild != null) {
            leftMemberSum = maxIndependent(node.leftChild);

        }
        if(node.rightChild != null) {
            rightMemberSum = maxIndependent(node.rightChild);
        }

        // If current node's children are counted, pass this node
        if((node.leftChild != null && node.leftChild.counted) || (node.rightChild != null && node.rightChild.counted)) {
            if(node.leftChild != null && node.leftChild.counted) {
                node.leftChild.counted = false;
            }
            if(node.rightChild != null && node.rightChild.counted) {
                node.rightChild.counted = false;
            }
            return leftMemberSum + rightMemberSum;
        }
        else { // If current node's children are not counted, count it
            node.counted = true;
            return leftMemberSum + rightMemberSum + 1;
        }
    }

    // Print the members with the same rank as the input member
    public void monitorRank(Double GMS) throws IOException {
        int rank = findRank(GMS, this.root, 0);
        List<AvlTreeNode> sameRank = new ArrayList<>();
        monitor(rank, this.root, 0, sameRank);
        fWriter.write("Rank Analysis Result:");
        for(AvlTreeNode node:sameRank) {
            Double originalValue = node.value;
            String precisionValue = String.format("%.3f", originalValue);
            fWriter.write(" " + node.name + " " + precisionValue);
        }
        fWriter.write("\n");
    }

    // Find the members with the same rank as the input member
    private void monitor(int rank, AvlTreeNode node, int depth, List<AvlTreeNode> sameRank) {
        // Found the member with the same rank, add him to the list
        if(depth == rank) {
            sameRank.add(node);
            return;
        }

        if(node.leftChild != null) {
            monitor(rank, node.leftChild, depth + 1, sameRank);
        }
        if(node.rightChild != null) {
            monitor(rank, node.rightChild, depth + 1, sameRank);
        }
    }

    // Find the rank of a member via its GMS
    private int findRank(Double GMS, AvlTreeNode node, int depth) {
        int comparison = GMS.compareTo(node.value);
        if(comparison < 0) {
            depth = findRank(GMS, node.leftChild, depth) + 1;
            return depth;
        }
        else if(comparison > 0) {
            depth = findRank(GMS, node.rightChild, depth) + 1;
            return depth;
        }
        else {
            return depth;
        }
    }
}
