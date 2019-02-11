package visitor;

import syntaxTree.Args;
import syntaxTree.Body;
import syntaxTree.CompStat;
import syntaxTree.Decls;
import syntaxTree.DefDecl;
import syntaxTree.Expr;
import syntaxTree.ParDecls;
import syntaxTree.Programma;
import syntaxTree.Stat;
import syntaxTree.Statements;
import syntaxTree.VarDecl;
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
import syntaxTree.statOp.IfThenElseOp;
import syntaxTree.statOp.IfThenOp;
import syntaxTree.statOp.ReadOp;
import syntaxTree.statOp.WhileOp;
import syntaxTree.statOp.WriteOp;
import syntaxTree.wrapper.DeclsWrapper;
import syntaxTree.wrapper.ParDeclSon;
import syntaxTree.wrapper.VarDeclsInitWrapper;

public class TreePrinterVisitor implements Visitor<String> {
	
	@Override
	public String visit(Args n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(Expr e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(Body n) {
		String toReturn = "<"+n.getOp()+">\n ";
		toReturn += n.getVd().accept(this);
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(CompStat n) {
		String toReturn = "<"+n.getOp()+">\n ";
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(Decls n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(DeclsWrapper e: n.getChildList()) {
			if(e instanceof VarDecl)
				toReturn += ((VarDecl)e).accept(this);
			if(e instanceof DefDecl)
				toReturn += ((DefDecl)e).accept(this);
		}
			
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(DefDecl n) {
		String toReturn = "<"+n.getOp()+">\n ";
		toReturn += n.getB().accept(this);
		toReturn += n.getId().accept(this);
		toReturn += (n.getPd()!=null)?n.getPd().accept(this):"";
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	
	@Override
	public String visit(Expr n) {
		String toReturn = "<"+n.getOp()+">\n ";
		
		if(n instanceof AndOp)
			toReturn += ((AndOp)n).accept(this);
		if(n instanceof NotOp)
			toReturn += ((NotOp)n).accept(this);
		if(n instanceof OrOp)
			toReturn += ((OrOp)n).accept(this);
		
		if(n instanceof EqOp)
			toReturn += ((EqOp)n).accept(this);
		if(n instanceof GeOp)
			toReturn += ((GeOp)n).accept(this);
		if(n instanceof GtOp)
			toReturn += ((GtOp)n).accept(this);
		if(n instanceof LeOp)
			toReturn += ((LeOp)n).accept(this);
		if(n instanceof LtOp)
			toReturn += ((LtOp)n).accept(this);
		
		if(n instanceof AddOp)
			toReturn += ((AddOp)n).accept(this);
		if(n instanceof DivOp)
			toReturn += ((DivOp)n).accept(this);
		if(n instanceof MultOp)
			toReturn += ((MultOp)n).accept(this);
		if(n instanceof SubOp)
			toReturn += ((SubOp)n).accept(this);
		
		
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}
	
	@Override
	public String visit(ParDecls n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(ParDeclSon e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(Programma n) {
		String toReturn = "<"+n.getOp()+">\n ";
		toReturn += n.getD().accept(this);
		toReturn += n.getS().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	/*
	@Override
	public String visit(Stat n) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	@Override
	public String visit(Statements n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(Stat e: n.getChildList()) {
			if(e instanceof AssignOp)
				toReturn += ((AssignOp)e).accept(this);
			if(e instanceof CallOp)
				toReturn += ((CallOp)e).accept(this);
			if(e instanceof IfThenElseOp)
				toReturn += ((IfThenElseOp)e).accept(this);
			if(e instanceof IfThenOp)
				toReturn += ((IfThenOp)e).accept(this);
			if(e instanceof ReadOp)
				toReturn += ((ReadOp)e).accept(this);
			if(e instanceof WhileOp)
				toReturn += ((WhileOp)e).accept(this);
			if(e instanceof WriteOp)
				toReturn += ((WriteOp)e).accept(this);
		}
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(VarDecl n) {
		String toReturn = "<"+n.getOp()+">\n ";
		toReturn += n.getT().accept(this);
		toReturn += n.getVdi().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(VarDecls n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(VarDecl e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(VarDeclsInit n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(VarDeclsInitWrapper e: n.getChildList()) {
			toReturn += e.getId().accept(this);
			if(e.getOp().equals("VarInitOp"))
				toReturn += e.getViv().accept(this);
		}
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(VarInitValue n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(Vars n) {
		String toReturn = "<"+n.getOp()+">\n ";
		for(Leaf e: n.getChildList())
			toReturn += e.accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(AddOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(DivOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(MultOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(SubOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(UminusOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(AndOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(NotOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(OrOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(EqOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(GeOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(GtOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(LeOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(LtOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE1().accept(this);
		n.getE2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(AssignOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getId().accept(this);
		n.getE().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(CallOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getId().accept(this);
		if(n.getOp().equals("CallOpWithArgs"))
			n.getA().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(IfThenElseOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE().accept(this);
		n.getCs1().accept(this);
		n.getCs2().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(IfThenOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE().accept(this);
		n.getCs1().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(ReadOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getV().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(WhileOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getE().accept(this);
		n.getCs1().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	@Override
	public String visit(WriteOp n) {
		String toReturn = "<"+n.getOp()+">\n ";
		n.getA().accept(this);
		toReturn += "</"+n.getOp()+">\n ";
		return toReturn;
	}

	/*
	public String visit(DeclsWrapper n) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public String visit(ParDeclSon n) {
		String toReturn = "";
		toReturn += n.getParType().accept(this);
		toReturn += n.getType().accept(this);
		toReturn += n.getId().accept(this);
		return toReturn;
	}

	@Override
	public String visit(VarDeclsInitWrapper n) {
		String toReturn = "";
		toReturn += n.getId().accept(this);
		if(n.getOp().equals("VarInit"))
			toReturn += n.getViv().accept(this);
		return toReturn;
	}
	
	



}
