package top.dabaibai.demo.biz.stream;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/12 15:39
 */
@Slf4j
public class Node {
    /**
     * 节点的值*
     */
    private int value;
    /**
     * 当前节点*
     */
    private Node node;
    /**
     * 此节点的左节点，类型为Node*
     */
    public Node left;
    /**
     * 此节点的右节点，数据类型为Node*
     */
    public Node right;

    public Node() {
    }

    public Node(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return this.value + " ";
    }
}
