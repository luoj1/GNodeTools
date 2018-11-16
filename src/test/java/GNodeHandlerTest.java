import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.fail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Junit test of GNode tools
 *
 * @author Zhangqi Luo
 * @version 1.0
 */
public class GNodeHandlerTest {

    private class TestNode implements GNode {
        private String name;
        private GNode[] children = new GNode[]{};

        /**
         * constructor of testnode
         * @param name name of gnode
         */
        public TestNode (String name) {
            this.name = name;
        }

        /**
         * implement getName in GNode
         * @return
         */
        @Override
        public String getName(){
            return name;
        }
        /**
         * implement getChildren in GNode
         * @return
         */
        @Override
        public GNode[] getChildren(){
            return children;
        }
        /**
         * setChildren in testNode
         * @return
         */
        public void setChildren(GNode[] nodes){
            this.children = nodes;
        }
    }

    private GNodeHandler handler;
    private List<TestNode> testGraphNodes;
    private Set<String> nodeNames;

    /**
     * init graph for testing
     */
    @Before
    public void setUp() {
        handler = new GNodeHandler();
        nodeNames = new HashSet<String>();
        testGraphNodes = initTestGraph();


    }

    /**
     * test graph initializer
     * @return build the acyclic graph and append children
     */
    private List<TestNode> initTestGraph() {
        List<TestNode> nodes = new ArrayList<TestNode>();
        for(int i = 0; i < 10; i++) {
            nodeNames.add(Character.toString((char)('A'+i)));
            nodes.add(new TestNode(Character.toString((char)('A'+i))));
        }

        /*
        A is in 0 index of GNode list ... J is in 9 index of GNode list
        A
            B
                E
                F
            C
                G
                H
                I
            D
                J
         */
        int offset = 'A';

        nodes.get('A'- offset).setChildren(new GNode[]{nodes.get('B'- offset),nodes.get('C'- offset),nodes.get('D'- offset)});
        nodes.get('B'- offset).setChildren(new GNode[]{nodes.get('A'- offset),nodes.get('F'- offset),nodes.get('E'- offset)});
        nodes.get('C'- offset).setChildren(new GNode[]{nodes.get('G'- offset),nodes.get('A'- offset)
                ,nodes.get('H'- offset),nodes.get('I'- offset)});
        nodes.get('D'- offset).setChildren(new GNode[]{nodes.get('J'- offset),nodes.get('A'- offset)});
        nodes.get('E'- offset).setChildren(new GNode[]{nodes.get('B'- offset)});
        nodes.get('F'- offset).setChildren(new GNode[]{nodes.get('B'- offset)});
        nodes.get('G'- offset).setChildren(new GNode[]{nodes.get('C'- offset)});
        nodes.get('H'- offset).setChildren(new GNode[]{nodes.get('C'- offset)});
        nodes.get('I'- offset).setChildren(new GNode[]{nodes.get('C'- offset)});
        nodes.get('J'- offset).setChildren(new GNode[]{nodes.get('D'- offset)});

        return nodes;
    }

    /**
     * test walkGraph by a single node graph with zero children
     */
    @Test
    public void SingleNodeWalk() {
        GNode test = new TestNode("single");
        List<GNode> out = handler.walkGraph(test);
        assertEquals(1, out.size());
        assertTrue(out.get(0).getName().equals("single"));
    }
    /**
     * test walkGraph by walking from A
     */
    @Test
    public void GNodeWalkA() {
        GNodeWalkHelper('A');

    }
    /**
     * test walkGraph by walking from C
     */
    @Test
    public void GNodeWalkC() {
        GNodeWalkHelper('C');

    }
    /**
     * test walkGraph by walking from J
     */
    @Test
    public void GNodeWalkJ() {
        GNodeWalkHelper('J');

    }

    /**
     * helper function of walkGraph
     * @param c character to start
     */
    private void GNodeWalkHelper(char c) {
        List<GNode> out = handler.walkGraph(testGraphNodes.get(c -'A'));
        assertEquals(10, out.size());
        Set<String> checkOutput = new HashSet<String>(nodeNames);
        for (GNode node : out) {
            boolean check = checkOutput.remove(node.getName());
            if (!check) {
                fail("unknown component in the graph or duplicated element in the output of walkGraph");
            }
        }
        assertTrue(checkOutput.size() == 0);
    }

    /**
     * test path builder with single node that has no children
     */
    @Test
    public void SingleNodePath() {
        GNode test = new TestNode("single");
        ArrayList<ArrayList<GNode>> out = handler.paths(test);
        assertEquals(1, out.size());
        assertEquals(1, out.get(0).size());
        assertTrue(out.get(0).get(0).getName().equals("single"));
    }
    /**
     * test path construction from A
     */
    @Test
    public void GNodePathA() {
        // paths(A) = ( (A B E) (A B F) (A C G) (A C H) (A C I) (A D J) )
        ArrayList<ArrayList<GNode>> out = handler.paths(testGraphNodes.get('A'-'A'));
        assertEquals(6, out.size());
        Set<String> answers = new HashSet<String>();
        answers.add("ABE");
        answers.add("ABF");
        answers.add("ACG");
        answers.add("ACH");
        answers.add("ACI");
        answers.add("ADJ");
        for (ArrayList<GNode> path : out) {
            String pathInString = "";
            for (GNode n : path) {
                pathInString += n.getName();
            }
            boolean check = answers.remove(pathInString);
            if (!check) {
                fail(pathInString + " is incorrent path in the graph or is duplicated in the output of building path");
            }
        }
        assertTrue(answers.size() == 0);
    }

}
