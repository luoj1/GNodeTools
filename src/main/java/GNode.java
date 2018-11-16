public interface GNode {
    /**
     * node name getter
     * @return name of node
     */
    public String getName();

    /**
     * node children getter
     * @return array of children node
     */
    public GNode[] getChildren();

}
