import java.util.ArrayList;
import java.util.HashSet;
class GNodeHandler {
	public ArrayList<GNode> walkGraph(GNode gnode){
		HashSet<GNode> set = new HashSet<GNode>();
		walkGraphDFS(gnode, set);
		ArrayList<GNode> res = new ArrayList<GNode>();
		for (GNode node : set) {
			res.add(node);
		}
		return res;
	}
	private void walkGraphDFS (GNode gnode, HashSet<GNode> set) {
		if (gnode == null || set.contains(gnode)) {
			return;
		}
		set.add(gnode);
		for (int i = 0; i < gnode.getChildren().length; i++) {
			walkGraphDFS(gnode.getChildren()[i], set);
		}
	}


	public ArrayList<ArrayList<GNode>> paths (GNode gnode){
		ArrayList<ArrayList<GNode>> out = new ArrayList<ArrayList<GNode>>();
		pathsBuilder(new ArrayList<GNode>(), gnode,
	 			new HashSet<GNode>(), out);
		return out;
	}

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