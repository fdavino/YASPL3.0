package syntax_tree.wrappers;

import java.util.ArrayList;

import syntax_tree.comp.Internal;
import syntax_tree.comp.Node;

public class ListParent<E> extends Internal {
	
	private ArrayList<E> list;
	
	public ListParent(String op, Node...sons) {
		super(op, sons);
		list = new ArrayList<>();
	}
	
	public ListParent<E> addChild(E node) {
		list.add(node);
		return this;
	}
}
