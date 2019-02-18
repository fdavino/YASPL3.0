package visitor;

import java.util.ArrayList;

import exception.AlreadyDeclaredException;
import semantic.SymbolTable;
import semantic.SymbolTable.Kind;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;
import semantic.Tuple;
import syntaxTree.Args;
import syntaxTree.Body;
import syntaxTree.CompStat;
import syntaxTree.Decls;
import syntaxTree.ParDecls;
import syntaxTree.Programma;
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
import syntaxTree.statOp.IfThenElseOp;
import syntaxTree.statOp.IfThenOp;
import syntaxTree.statOp.ReadOp;
import syntaxTree.statOp.WhileOp;
import syntaxTree.statOp.WriteOp;
import syntaxTree.utils.CustomStack;
import syntaxTree.utils.ParDeclSon;
import syntaxTree.varDeclInitOp.VarInit;
import syntaxTree.varDeclInitOp.VarNotInit;
import syntaxTree.wrapper.DeclsWrapper;
import syntaxTree.wrapper.VarDeclsInitWrapper;

public class SymbolTableFillerVisitor implements Visitor<Object>{

	private CustomStack stack;
	private SymbolTable actualST;


	public SymbolTableFillerVisitor() {
		stack = new CustomStack();
	}

	@Override
	public Object visit(Body n) {
		n.getVd().accept(this);
		System.out.println(stack.pop().toString()+"\n");
		actualST = stack.top();
		return null;
	}

	@Override
	public Object visit(Decls n) {
		for(DeclsWrapper dw : n.getChildList()) {
			dw.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(DefDeclNoPar n) {
		SymbolTable nst = new SymbolTable();
		
		Tuple t = new Tuple(Kind.DEFDECL);
		actualST.put((String)n.getId().accept(this), t);
		
		stack.push(nst);
		actualST = stack.top();
		n.setSymTableRef(actualST);

		n.getB().accept(this);

		return null;
	}

	@Override
	public Object visit(DefDeclPar n) {
		SymbolTable nst = new SymbolTable();
		
		Tuple t = new Tuple(Kind.DEFDECL);
		actualST.put((String)n.getId().accept(this), t);
		
		stack.push(nst);
		actualST = stack.top();
		n.setSymTableRef(actualST);

		n.getPd().accept(this);
		n.getB().accept(this);

		return null;
	}

	@Override
	public Object visit(ParDecls n) {
		for(ParDeclSon pd : n.getChildList()) {
			pd.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Programma n) {
		stack.push(new SymbolTable());
		actualST = stack.top();
		n.setSymTableRef(actualST);
		
		n.getD().accept(this);
		System.out.println(stack.top().toString());
		
		return n;
	}

	@Override
	public Object visit(VarDecl n) throws AlreadyDeclaredException {
		@SuppressWarnings("unchecked")
		ArrayList<String> listOfId = (ArrayList<String>) n.getVdi().accept(this);
		for(String s: listOfId) {
			if(!actualST.containsKey(s)) {
				Tuple t = new Tuple(Kind.VARDECL, getTypeFromLeaf(n.getT()));
				actualST.put(s, t);
			}
			else {
				throw new AlreadyDeclaredException("Var "+s+" already declared in this scope");
			}
		}

		return null;
	}

	@Override
	public Object visit(VarDecls n) {
		for(VarDecl vd : n.getChildList()) {
			vd.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(VarDeclsInit n) {
		ArrayList<String> listOfId = new ArrayList<>();
		for(VarDeclsInitWrapper i: n.getChildList()) {
			listOfId.add(0,(String)i.accept(this));
		}
		return listOfId;
	}
	
	@Override
	public Object visit(Leaf n) {
		return n.getValue();
	}

	@Override
	public Object visit(ParDeclSon n) {
		Tuple t = new Tuple(Kind.VARDECL, getTypeFromLeaf(n.getTypeLeaf()), getParTypeFromLeaf((String) n.getParType().accept(this)));
		actualST.put((String) n.getId().accept(this), t);
		return null;
	}

	@Override
	public Object visit(VarInit n) {
		return n.getId().accept(this);
	}

	@Override
	public Object visit(VarNotInit n) {
		return n.getId().accept(this);
	}
	

	@Override
	public Object visit(IdConst n) {
		return n.getId().accept(this);
	}
	
	@Override
	public Object visit(ParTypeLeaf n) {
		return n.getValue();
	}

	@Override
	public Object visit(VarInitValue n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Vars n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AddOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DivOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MultOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(SubOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UminusOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AndOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NotOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(OrOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(EqOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(GeOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(GtOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LeOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LtOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(BoolConst n) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Args n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IntConst n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DoubleConst n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CharConst n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StringConst n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AssignOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IfThenElseOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IfThenOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ReadOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(WhileOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(WriteOp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(TypeLeaf n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CompStat n) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object visit(Statements n) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SymbolTable.Type getTypeFromLeaf(TypeLeaf tl){
		switch(tl.getValue()) {
		case "BOOL": return Type.BOOL;
		case "INT" : return Type.INT;
		case "DOUBLE" : return Type.DOUBLE;
		case "STRING" : return Type.STRING;
		case "CHAR" : return Type.CHAR;
		}
		return null;
	}
	
	private SymbolTable.ParType getParTypeFromLeaf(String tl){
		switch(tl) {
		case "IN": return ParType.IN;
		case "OUT" : return ParType.OUT;
		case "INOUT" : return ParType.INOUT;
		}
		return null;
	}

}
