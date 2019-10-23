package syntaxTree;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.comp.Internal;
import syntaxTree.comp.Leaf;
import syntaxTree.leaf.IdConst;
import visitor.Visitable;
import visitor.Visitor;

public abstract class Stat extends Internal{
	
	//ReadOp
	public Stat(Location left, Location right, String op, Vars v) {
		super(left, right, op, v);
	}
	//WriteOp
	public Stat(Location left, Location right, String op, Args a) {
		super(left, right,op, a);
	}
	//AssignOp
	public Stat(Location left, Location right,  String op, IdConst id, Expr e) {
		super(left, right,op, id, e);
	}
	//CallOp
	public Stat(Location left, Location right, String op, IdConst id, Args a) {
		super(left, right,op, id, a);
	}
	//CallOp
	public Stat(Location left, Location right, String op, IdConst id) {
		super(left, right,op, id);
	}
	/*
	//IfThenElseOp
	public Stat(Location left, Location right, String op, Expr e, CompStat cs1, CompStat cs2) {
		super(left, right,op, e, cs1, cs2);
	}
	//IfThenOp && WhileOp
	public Stat(Location left, Location right, String op, Expr e, CompStat cs) {
		super(left, right,op, e, cs);
	}
	*/
	//IfThenOp && WhileOp con scope
	public Stat(Location left, Location right, String op, Expr e, Body b) {
		super(left, right,op, e, b);
	}
	
	//IfThenOp && WhileOp con scope
	public Stat(Location left, Location right, String op, Expr e, Body b1, Body b2) {
		super(left, right,op, e, b1, b2);
	}
	
	public abstract Object accept(Visitor<?> visitor);
	
	
	
	
	
	
}