package visitor;

import java.util.ArrayList;
import java.util.List;

import exception.NotDeclaredException;
import exception.SemanticException;
import exception.TypeMismatchException;
import exception.WrongArgumentNumberException;
import semantic.DefTuple;
import semantic.ParTuple;
import semantic.VarTuple;
import semantic.SymbolTable;
import semantic.SymbolTable.Kind;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;
import semantic.Tuple;
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

public class TypeCheckerVisitor implements Visitor<Object>{

	private CustomStack stack;
	private SymbolTable currentST;

	private int gIFT(SymbolTable.Type t) {
		
		switch(t) {
		case INT : return 0;
		case STRING : return 1;
		case DOUBLE : return 2;
		case CHAR : return 3;
		case BOOL : return 4;
		case VOID : return 5;
		}
		
		return -1;
	}
	
	private SymbolTable.Type[][] sumCompTable = {
			{Type.INT, Type.STRING, Type.DOUBLE, null, null, null},
			{Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.STRING, null},
			{Type.DOUBLE, Type.STRING, Type.DOUBLE, null, null, null},
			{null, Type.STRING, null, Type.STRING, null, null},
			{null, Type.STRING, null, null, null, null},
			{null, null, null, null, null, null}
	};

	private SymbolTable.Type[][] arithOpCompTable = {
			{Type.INT, null, Type.DOUBLE, null, null, null},
			{null, null, null, null, null, null},
			{Type.DOUBLE, null, Type.DOUBLE, null, null, null},
			{null, null, null, null, null, null},
			{null, null, null, null, null, null},
			{null, null, null, null, null, null}
	};

	private SymbolTable.Type[][] compareOpCompTable = {
			{Type.BOOL, null, Type.BOOL, null, null, null},
			{null, Type.BOOL, null, null, null, null},
			{Type.BOOL, null, Type.BOOL, null, null, null},
			{null, null, null, Type.BOOL, null, null},
			{null, null, null, null, null, null},
			{null, null, null, null, null, null}
	};
	
	private SymbolTable.Type[][] equityOpCompTable = {
			{Type.BOOL, null, Type.BOOL, null, null, null},
			{null, Type.BOOL, null, null, null, null},
			{Type.BOOL, null, Type.BOOL, null, null, null},
			{null, null, null, Type.BOOL, null, null},
			{null, null, null, null, Type.BOOL, null},
			{null, null, null, null, null, null}
	};
	
	private SymbolTable.Type[][] assignOpCompTable = {
			{Type.INT, null, Type.INT, null, null, null},
			{null, Type.STRING, null, Type.STRING, null, null},
			{Type.DOUBLE, null, Type.DOUBLE, null, null, null},
			{null, null, null, Type.CHAR, null, null},
			{null, null, null, null, Type.BOOL, null},
			{null, null, null, null, null, null}
	};
	
	private SymbolTable.Type[] uminusCompTable = {Type.INT, null, Type.DOUBLE, null, null, null};
	
	
	private Tuple lookup(String id){
		ArrayList<SymbolTable> temp = (ArrayList<SymbolTable>) stack.getStack(); 
		int index = temp.indexOf(currentST);
		boolean find = false;
		SymbolTable sb = temp.get(index);
		
		while(!find && index >= 0) {
			sb = temp.get(index);
			find = sb.containsKey(id);
			index--;
		}
		
		return sb.get(id);
	}

	public TypeCheckerVisitor() {
		stack = new CustomStack();
	}

	@Override
	public Object visit(Args n) throws RuntimeException {
		ArrayList<Type> parType = new ArrayList<>();
		for(Expr e : n.getChildList()) {
			parType.add((Type)e.accept(this));
		}
		n.setType(Type.VOID);
		return parType;
	}

	@Override
	public Object visit(Body n) throws RuntimeException {
		n.getVd().accept(this);
		n.getS().accept(this);
		stack.pop();
		currentST = stack.top();
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(CompStat n) throws RuntimeException {
		n.getS().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(Decls n) throws RuntimeException {
		for(DeclsWrapper dw : n.getChildList()) {
			dw.accept(this);
		}
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(DefDeclNoPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();
		n.getId().accept(this);
		n.getB().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(DefDeclPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();
		n.getId().accept(this);
		n.getB().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(ParDecls n) throws RuntimeException {
		return null;
	}
	
	@Override
	public Object visit(ParDeclSon n) throws RuntimeException {
		return null;
	}

	@Override
	public Object visit(Programma n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();
		n.getD().accept(this);
		n.getS().accept(this);
		stack.pop();
		n.setType(Type.VOID);
		return n;
	}

	@Override
	public Object visit(Statements n) throws RuntimeException {
		for(Stat s : n.getChildList()) {
			s.accept(this);
		}
		n.setType(Type.VOID);
		return n.getType();
	}
	
	@Override
	public Object visit(VarDecls n) throws RuntimeException {
		for(VarDecl vd : n.getChildList()) {
			vd.accept(this);
		}
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(VarDecl n) throws RuntimeException {
		n.getVdi().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(VarDeclsInit n) throws RuntimeException {
		for(VarDeclsInitWrapper vdiw : n.getChildList()) {
			vdiw.accept(this);
		}
		n.setType(Type.VOID);
		return n.getType();
	}
	
	@Override
	public Object visit(VarInit n) throws RuntimeException {
		Type t1 = (Type)n.getId().accept(this);
		Type t2 = (Type)n.getViv().accept(this);
		Type tt = assignOpCompTable[gIFT(t1)][gIFT(t2)]; 
		if(tt != null) {
			n.setType(tt);
			return tt;
		}else {
			throw new TypeMismatchException(n.getOp(), t1, t2);
		}
	}

	@Override
	public Object visit(VarNotInit n) throws RuntimeException {
		n.setType((Type) n.getId().accept(this));
		return n.getType();
	}


	@Override
	public Object visit(VarInitValue n) throws RuntimeException {
		n.setType((Type)n.getE().accept(this));
		return n.getType();
	}

	@Override
	public Object visit(Vars n) throws RuntimeException {
		for(IdConst id : n.getChildList()) {
			id.accept(this);
		}
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(AddOp n) throws RuntimeException {
		Type t1 = (Type) n.getE1().accept(this);
		Type t2 = (Type) n.getE2().accept(this);
		Type tt = sumCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(DivOp n) throws RuntimeException {
		Type t1 = (Type) n.getE1().accept(this);
		Type t2 = (Type) n.getE2().accept(this);
		Type tt = arithOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(MultOp n) throws RuntimeException {
		Type t1 = (Type) n.getE1().accept(this);
		Type t2 = (Type) n.getE2().accept(this);
		Type tt = arithOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(SubOp n) throws RuntimeException {
		Type t1 = (Type) n.getE1().accept(this);
		Type t2 = (Type) n.getE2().accept(this);
		Type tt = arithOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(UminusOp n) throws RuntimeException {
		Type t1 = (Type) n.getE().accept(this);
		Type tt = uminusCompTable[gIFT(t1)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1);
	}

	@Override
	public Object visit(AndOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = (t1 == t2)&&(t2 == Type.BOOL)?Type.BOOL:null;
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(NotOp n) throws RuntimeException {
		Type t1 = (Type) n.getE().accept(this);
		Type tt = (t1 == Type.BOOL)?Type.BOOL:null;
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1);
	}

	@Override
	public Object visit(OrOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = (t1 == t2)&&(t2 == Type.BOOL)?Type.BOOL:null;
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(EqOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = equityOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(GeOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = compareOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(GtOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = compareOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(LeOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = compareOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(LtOp n) throws RuntimeException {
		Type t1 = (Type)n.getE1().accept(this);
		Type t2 = (Type)n.getE2().accept(this);
		Type tt = compareOpCompTable[gIFT(t1)][gIFT(t2)];
		if(tt != null) {
			n.setType(tt);
			return tt;
		}
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
	}

	@Override
	public Object visit(BoolConst n) throws RuntimeException {
		n.setType(Type.BOOL);
		return Type.BOOL;
	}
	
	@Override
	public Object visit(IntConst n) throws RuntimeException {
		n.setType(Type.INT);
		return Type.INT;
	}

	@Override
	public Object visit(DoubleConst n) throws RuntimeException {
		n.setType(Type.DOUBLE);
		return Type.DOUBLE;
	}

	@Override
	public Object visit(CharConst n) throws RuntimeException {
		n.setType(Type.CHAR);
		return Type.CHAR;
	}

	@Override
	public Object visit(StringConst n) throws RuntimeException {
		n.setType(Type.STRING);
		return Type.STRING;
	}

	@Override
	public Object visit(IdConst n) throws RuntimeException {
		Tuple t = lookup(n.getId().getValue());
		
		if(t instanceof DefTuple)
			n.setType(Type.VOID);
		else if(t instanceof ParTuple)
			n.setType(((ParTuple)t).getType());
		else
			n.setType(((VarTuple)t).getType());
		
		return n.getType();
	}

	@Override
	public Object visit(AssignOp n) throws RuntimeException {
		Type t1 = (Type) n.getE().accept(this);
		Type t2 = (Type) n.getId().accept(this);
		if(assignOpCompTable[gIFT(t1)][gIFT(t2)]!=null)
			n.setType(Type.VOID);
		else
			throw new TypeMismatchException(n.getOp(), t1, t2);
		return n.getType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(CallOp n) throws RuntimeException {
		n.setType(Type.VOID);
		
		String id = (String) n.getId().getId().getValue();
		
		ArrayList<Type> given; 
		ArrayList<Expr> givenExpr;
		
		given = (n.getA()!=null)?(ArrayList<Type>)n.getA().accept(this):new ArrayList<Type>();
		givenExpr = (n.getA()!=null)?n.getA().getChildList():new ArrayList<Expr>();
		
		DefTuple def = (DefTuple)lookup(id);
		ArrayList<ParTuple> par = def.getParam();
		
		if(given.size() != par.size())
			throw new WrongArgumentNumberException(id, par.size(), given.size());
		int size = par.size();
		for(int i = 0; i < size; i++) {
			if(((par.get(i).getParType() == ParType.INOUT || 
					par.get(i).getParType() == ParType.OUT)) &&
					!(givenExpr.get(i) instanceof IdConst))
				throw new SemanticException("parametri OUT e INOUT devono essere variabili");
			
			if(assignOpCompTable[gIFT(par.get(i).getType())][gIFT(given.get(i))] == null)
				throw new TypeMismatchException(id, par.get(i).getType(), given.get(i));
		}

		return null;
	}

	@Override
	public Object visit(IfThenElseOp n) throws RuntimeException {
		Type expr = (Type) n.getE().accept(this);
		n.getCs1().accept(this);
		n.getCs2().accept(this);
		if(expr == Type.BOOL)
			n.setType(Type.VOID);
		else
			throw new TypeMismatchException(n.getOp(), Type.BOOL, expr);
		return n.getType();
	}

	@Override
	public Object visit(IfThenOp n) throws RuntimeException {
		Type expr = (Type) n.getE().accept(this);
		n.getCs().accept(this);
		if(expr == Type.BOOL)
			n.setType(Type.VOID);
		else
			throw new TypeMismatchException(n.getOp(), Type.BOOL, expr);
		return n.getType();
	}

	@Override
	public Object visit(ReadOp n) throws RuntimeException {
		n.getV().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(WhileOp n) throws RuntimeException {
		Type expr = (Type) n.getE().accept(this);
		stack.push(n.getSymTableRef());
		currentST = stack.top();
		n.getB().accept(this);
		if(expr == Type.BOOL)
			n.setType(Type.VOID);
		else
			throw new TypeMismatchException(n.getOp(), Type.BOOL, n.getE().getType());
		return n.getType();
	}

	@Override
	public Object visit(WriteOp n) throws RuntimeException {
		n.getA().accept(this);
		n.setType(Type.VOID);
		return n.getType();
	}

	@Override
	public Object visit(Leaf n) throws RuntimeException {
		return n.getValue();
	}


	@Override
	public Object visit(TypeLeaf n) throws RuntimeException {
		return null;
	}

	@Override
	public Object visit(ParTypeLeaf n) throws RuntimeException {
		return null;
	}



}
