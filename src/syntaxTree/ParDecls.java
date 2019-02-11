package syntaxTree;

import java.util.ArrayList;
import syntaxTree.comp.Internal;
import syntaxTree.wrapper.ParDeclSon;
import visitor.Visitable;
import visitor.Visitor;

public class ParDecls extends Internal implements Visitable {

	private ArrayList<ParDeclSon> childList;
	
	public ParDecls(String op) {
		super(op);
		childList = new ArrayList<ParDeclSon>();
	}
	
	public ParDecls addChild(ParDeclSon pd){
		this.childList.add(pd);
		return this;
	}

	@Override
	public Object accept(Visitor<?> visitor) {
		return visitor.visit(this);
	}

	public ArrayList<ParDeclSon> getChildList() {
		return childList;
	}

}
