import java.util.ArrayList;
import java.util.HashSet;
/**
 * handler class that include two GNode tool
 *
 * @author Zhangqi Luo
 * @version 1.0
 */
class GNodeHandler {
	/**
	 * walk through acyclic graph by recursion
	 * @param gnode input node in graph
	 * @return all nodes in a graph
	 */
	public ArrayList<GNode> walkGraph(GNode gnode){
		HashSet<GNode> set = new HashSet<GNode>();
		walkGraphDFS(gnode, set);
		ArrayList<GNode> res = new ArrayList<GNode>();
		for (GNode node : set) {
			res.add(node);
		}
		return res;
	}

	/**
	 * helper function of walk graph recursion
	 * @param gnode current node
	 * @param set hashset for preventing unnnecessary traverse
	 */
	private void walkGraphDFS (GNode gnode, HashSet<GNode> set) {
		if (gnode == null || set.contains(gnode)) {
			return;
		}
		set.add(gnode);
		for (int i = 0; i < gnode.getChildren().length; i++) {
			walkGraphDFS(gnode.getChildren()[i], set);
		}
	}

	/**
	 * paths builder starting from given node
	 * @param gnode gnode to start
	 * @return arraylist of GNode path
	 */
	public ArrayList<ArrayList<GNode>> paths (GNode gnode){
		ArrayList<ArrayList<GNode>> out = new ArrayList<ArrayList<GNode>>();
		pathsBuilder(new ArrayList<GNode>(), gnode,
	 			new HashSet<GNode>(), out);
		return out;
	}

	/**
	 * path builder that support DFS
	 * @param stack path builder
	 * @param current current node
	 * @param set prevent traverse back
	 * @param out arraylist for storing path
	 */
	private void pathsBuilder (ArrayList<GNode> stack, GNode current,
	 HashSet<GNode> set, ArrayList<ArrayList<GNode>> out) {
		if (current.getChildren().length <= 1) {
			stack.add(current);
			out.add(new ArrayList<GNode>(stack));
			stack.remove(stack.size()-1);
			return;
		}
		if (set.contains(current)) {
			return;
		}
		set.add(current);
		stack.add(current);
		for (int i = 0; i < current.getChildren().length; i++) {
			pathsBuilder(stack, current.getChildren()[i],
	 			set, out);
		}
		set.remove(current);
		stack.remove(stack.size()-1);
	}
}
