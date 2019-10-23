package visitor;

import java.util.ArrayList;

import syntaxTree.Args;
import syntaxTree.Body;
import syntaxTree.CompStat;
import syntaxTree.Decls;
import syntaxTree.Expr;
import syntaxTree.ParDecls;
import syntaxTree.Programma;
import syntaxTree.Stat;
import syntaxTree.Statements;
import syntaxTree.VarDecls;
import syntaxTree.VarDeclsInit;
import syntaxTree.VarInitValue;
import syntaxTree.Vars;
import syntaxTree.arithOp.AddOp;
import syntaxTree.arithOp.DivOp;
import syntaxTree.arithOp.MultOp;
import syntaxTree.arithOp.SubOp;
import syntaxTree.arithOp.UminusOp;
import syntaxTree.comp.Leaf;
import syntaxTree.comp.Node;
import syntaxTree.declsOp.DefDeclNoPar;
import syntaxTree.declsOp.DefDeclPar;
import syntaxTree.declsOp.VarDecl;
import syntaxTree.leaf.BoolConst;
import syntaxTree.leaf.CharConst;
import syntaxTree.leaf.DoubleConst;
import syntaxTree.leaf.IdConst;
import syntaxTree.leaf.IntConst;
import syntaxTree.leaf.ParTypeLeaf;
import syntaxTree.leaf.StringConst;
import syntaxTree.leaf.TypeLeaf;
import syntaxTree.logicOp.AndOp;
import syntaxTree.logicOp.NotOp;
import syntaxTree.logicOp.OrOp;
import syntaxTree.relOp.EqOp;
import syntaxTree.relOp.GeOp;
import syntaxTree.relOp.GtOp;
import syntaxTree.relOp.LeOp;
import syntaxTree.relOp.LtOp;
import syntaxTree.statOp.AssignOp;
import syntaxTree.statOp.CallOp;
import syntaxTree.statOp.DecPostOp;
import syntaxTree.statOp.DecPreOp;
import syntaxTree.statOp.IfThenElseOp;
import syntaxTree.statOp.IfThenOp;
import syntaxTree.statOp.IncPostOp;
import syntaxTree.statOp.IncPreOp;
import syntaxTree.statOp.ReadOp;
import syntaxTree.statOp.WhileOp;
import syntaxTree.statOp.WriteOp;
import syntaxTree.utils.ParDeclSon;
import syntaxTree.varDeclInitOp.VarInit;
import syntaxTree.varDeclInitOp.VarNotInit;
import syntaxTree.wrapper.DeclsWrapper;
import syntaxTree.wrapper.VarDeclsInitWrapper;

public class TreePrinterVisitor implements Visitor<String> {
	
	@Override
	public String visit(Args n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(Expr e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(Body n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getVd().accept(this);
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(CompStat n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(Decls n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(DeclsWrapper e: n.getChildList()) 
			toReturn += e.accept(this);
			
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(DefDeclNoPar n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getId().accept(this);
		toReturn += n.getB().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}
	
	@Override
	public String visit(DefDeclPar n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getId().accept(this);
		toReturn += n.getB().accept(this);
		toReturn += n.getPd().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	/*
	@Override
	public String visit(Expr n) {
		String toReturn = "";
		
		if(n instanceof AndOp)
			toReturn += ((AndOp)n).accept(this);
		else if(n instanceof NotOp)
			toReturn += ((NotOp)n).accept(this);
		else if(n instanceof OrOp)
			toReturn += ((OrOp)n).accept(this);
		
		else if(n instanceof EqOp)
			toReturn += ((EqOp)n).accept(this);
		else if(n instanceof GeOp)
			toReturn += ((GeOp)n).accept(this);
		else if(n instanceof GtOp)
			toReturn += ((GtOp)n).accept(this);
		else if(n instanceof LeOp)
			toReturn += ((LeOp)n).accept(this);
		else if(n instanceof LtOp)
			toReturn += ((LtOp)n).accept(this);
		
		else if(n instanceof AddOp)
			toReturn += ((AddOp)n).accept(this);
		else if(n instanceof DivOp)
			toReturn += ((DivOp)n).accept(this);
		else if(n instanceof MultOp)
			toReturn += ((MultOp)n).accept(this);
		else if(n instanceof SubOp)
			toReturn += ((SubOp)n).accept(this);
		
		else if(n instanceof BoolConst)
			toReturn += ((BoolConst)n).accept(this);
		else if(n instanceof CharConst)
			toReturn += ((CharConst)n).accept(this);
		else if(n instanceof DoubleConst)
			toReturn += ((DoubleConst)n).accept(this);
		else if(n instanceof IdConst)
			toReturn += ((IdConst)n).accept(this);
		else if(n instanceof IntConst)
			toReturn += ((IntConst)n).accept(this);
		else if(n instanceof StringConst)
			toReturn += ((StringConst)n).accept(this);

		return toReturn;
	}
	
	*/
	
	@Override
	public String visit(ParDecls n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(ParDeclSon e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(Programma n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getD().accept(this);
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	/*
	@Override
	public String visit(Stat e) {
		String toReturn = "";
		
		if(e instanceof AssignOp)
			toReturn += ((AssignOp)e).accept(this);
		else if(e instanceof CallOp)
			toReturn += ((CallOp)e).accept(this);
		else if(e instanceof IfThenElseOp)
			toReturn += ((IfThenElseOp)e).accept(this);
		else if(e instanceof IfThenOp)
			toReturn += ((IfThenOp)e).accept(this);
		else if(e instanceof ReadOp)
			toReturn += ((ReadOp)e).accept(this);
		else if(e instanceof WhileOp)
			toReturn += ((WhileOp)e).accept(this);
		else if(e instanceof WriteOp)
			toReturn += ((WriteOp)e).accept(this);

		return toReturn;
	}
	
	*/
	
	@Override
	public String visit(Statements n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(Stat e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(VarDecl n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getT().accept(this);
		toReturn += n.getVdi().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(VarDecls n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(VarDecl e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(VarDeclsInit n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(VarDeclsInitWrapper e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(VarInitValue n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(Vars n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		for(IdConst e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(AddOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(DivOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(MultOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(SubOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(UminusOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(AndOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(NotOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(OrOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(EqOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(GeOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(GtOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(LeOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(LtOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE1().accept(this);
		toReturn += n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(AssignOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getId().accept(this);
		toReturn += n.getA().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(CallOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getId().accept(this);
		if(n.getOp().equals("CallOpWithArgs"))
			toReturn += n.getA().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(IfThenElseOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += n.getB1().accept(this);
		toReturn += n.getB2().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(IfThenOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += n.getB().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(ReadOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getV().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(WhileOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getE().accept(this);
		toReturn += n.getB().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(WriteOp n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += n.getA().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	
	public String visit(Leaf n) {
		String toReturn = "";
		toReturn += ""+n.getValue()+"\n";
		toReturn += "";
		return toReturn;
	}

	@Override
	public String visit(ParDeclSon n) {
		String toReturn = "";
		toReturn += n.getParType().accept(this);
		toReturn += n.getTypeLeaf().accept(this);
		toReturn += n.getId().accept(this);
		return toReturn;
	}

	@Override
	public String visit(VarInit n) {
		String toReturn = "";
		toReturn += n.getId().accept(this);
		toReturn += n.getViv().accept(this);
		return toReturn;
	}
	
	@Override
	public String visit(VarNotInit n) {
		String toReturn = "";
		toReturn += n.getId().accept(this);
		return toReturn;
	}

	@Override
	public String visit(BoolConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(IdConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(IntConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(DoubleConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(CharConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(StringConst n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getId().accept(this);
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(TypeLeaf n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getValue()+"\n";
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(ParTypeLeaf n) {
		String toReturn = "<"+n.getOp()+" "+addAttr(n)+">\n";
		toReturn += ""+n.getValue()+"\n";
		toReturn += "</"+n.getOp()+">\n";
		return toReturn;
	}
	
	private String addAttr(Node n) {
		String op1 = (n.getSymTableRef()!=null)?String.format("symTab=\"%d\" ", n.getSymTableRef().hashCode()):"";
		String op2 = (n.getType()!=null)?String.format("type=\"%s\"", n.getType()):"";
		return op1 + op2;
	}

	@Override
	public String visit(IncPostOp n) throws RuntimeException {
		String toReturn = "<"+n.getOp()+" "+ addAttr(n)+">\n";
		toReturn += "" + n.getId() +"\n";
		toReturn += "</"+ n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(IncPreOp n) throws RuntimeException {
		String toReturn = "<"+n.getOp()+" "+ addAttr(n)+">\n";
		toReturn += "" + n.getId() +"\n";
		toReturn += "</"+ n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(DecPostOp n) throws RuntimeException {
		String toReturn = "<"+n.getOp()+" "+ addAttr(n)+">\n";
		toReturn += "" + n.getId() +"\n";
		toReturn += "</"+ n.getOp()+">\n";
		return toReturn;
	}

	@Override
	public String visit(DecPreOp n) throws RuntimeException {
		String toReturn = "<"+n.getOp()+" "+ addAttr(n)+">\n";
		toReturn += "" + n.getId() +"\n";
		toReturn += "</"+ n.getOp()+">\n";
		return toReturn;
	}


	

}
