import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.fail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class GNodeHandlerTest {

    private class TestNode implements GNode {
        private String name;
        private GNode[] children = new GNode[]{};
        public TestNode (String name) {
            this.name = name;
        }
        @Override
        public String getName(){
            return name;
        }
        @Override
        public GNode[] getChildren(){
            return children;
        }

        public void setChildren(GNode[] nodes){
            this.children = nodes;
        }
    }

    private GNodeHandler handler;
    private List<TestNode> testGraphNodes;
    private Set<String> nodeNames;

    @Before
    public void setUp() {
        handler = new GNodeHandler();
        nodeNames = new HashSet<String>();
        testGraphNodes = initTestGraph();


    }
    private List<TestNode> initTestGraph() {
        List<TestNode> nodes = new ArrayList<TestNode>();
        for(int i = 0; i < 10; i++) {
            nodeNames.add(Character.toString((char)('A'+i)));
            nodes.add(new TestNode(Character.toString((char)('A'+i))));
        }

        /*
        A is 0 ... J is 9
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


    @Test
    public void SingleNodeWalk() {
        GNode test = new TestNode("single");
        List<GNode> out = handler.walkGraph(test);
        assertEquals(1, out.size());
        assertTrue(out.get(0).getName().equals("single"));
    }
    @Test
    public void GNodeWalkA() {
        GNodeWalkHelper('A');

    }
    @Test
    public void GNodeWalkC() {
        GNodeWalkHelper('C');

    }
    @Test
    public void GNodeWalkJ() {
        GNodeWalkHelper('J');

    }
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
    @Test
    public void SingleNodePath() {
        GNode test = new TestNode("single");
        ArrayList<ArrayList<GNode>> out = handler.paths(test);
        assertEquals(1, out.size());
        assertEquals(1, out.get(0).size());
        assertTrue(out.get(0).get(0).getName().equals("single"));
    }
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
