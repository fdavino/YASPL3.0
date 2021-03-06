package visitor;

import syntaxTree.*;
import syntaxTree.arithOp.*;
import syntaxTree.comp.*;
import syntaxTree.declsOp.*;
import syntaxTree.leaf.*;
import syntaxTree.logicOp.*;
import syntaxTree.relOp.*;
import syntaxTree.statOp.*;
import syntaxTree.utils.*;
import syntaxTree.varDeclInitOp.*;
import syntaxTree.wrapper.DeclsWrapper;
import syntaxTree.wrapper.VarDeclsInitWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exception.*;
import semantic.*;
import semantic.SymbolTable.*;

public class SymTableVisitor implements Visitor<Object> {

	private CustomStack stack;
	private SymbolTable actualScope;

	private DefTuple lastCall;

	public SymTableVisitor(String pathToPrintScope) throws SecurityException, IOException {
		this.stack = new CustomStack();
	}

	public SymTableVisitor() {
		this.stack = new CustomStack();
	}

	@Override
	public Object visit(Args n) {
		for (Expr e : n.getChildList()) {
			if (checkExpr(e, e))
				e.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Decls n) {
		for (DeclsWrapper dw : n.getChildList()) {
			dw.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Body n) {
		n.getVd().accept(this);
		n.getS().accept(this);
		this.actualScope = this.stack.top();
		return null;
	}

	@Override
	public Object visit(ParDecls n) {
		ArrayList<ParTuple> parArray = new ArrayList<>();
		for (ParDeclSon s : n.getChildList()) {
			parArray.add((ParTuple) s.accept(this));

		}
		return parArray;
	}

	@Override
	public Object visit(ParDeclSon n) {
		String id = (String) n.getId().accept(this);
		checkAlreadyDeclared(id, n);
		ParType x = this.getValueOfParTypeLeaf((String) n.getParType().accept(this));
		ParTuple t = new ParTuple(Kind.VARDECL, x, this.getValueOfLeaf(n.getTypeLeaf()));
		this.actualScope.put(id, t);
		return t;
	}

	@Override
	public Object visit(VarDecl n) {
		ArrayList<String> idList = (ArrayList<String>) n.getVdi().accept(this);
		for (String s : idList) {
			checkAlreadyDeclared(s, n);
			VarTuple t = new VarTuple(Kind.VARDECL, this.getValueOfLeaf(n.getT()));
			this.actualScope.put(s, t);
		}	
		return null;
	}

	@Override
	public Object visit(VarDeclsInit n) {
		ArrayList<String> idList = new ArrayList<>();
		String id;
		for (VarDeclsInitWrapper vdiw : n.getChildList()) {
			id = (String) vdiw.accept(this);
			idList.add(0, id);
		}
		return idList;
	}

	@Override
	public Object visit(TypeLeaf n) {
		// TODO Auto-generated method stub
		return n.getValue();
	}

	@Override
	public Object visit(DefDeclNoPar n) {
		DefTuple t = new DefTuple(Kind.DEFDECL);
		String defName = (String) n.getId().accept(this);
		this.checkAlreadyDeclared(defName, n);
		this.actualScope.put(defName, t);
		SymbolTable sc = new SymbolTable(defName);
		this.stack.push(sc);
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getB().accept(this);
		return null;
	}

	@Override
	public Object visit(DefDeclPar n) {
		DefTuple t = new DefTuple(Kind.DEFDECL);
		String defName = (String) n.getId().accept(this);
		this.checkAlreadyDeclared(defName, n);
		this.actualScope.put(defName, t);
		SymbolTable sc = new SymbolTable(defName);
		this.stack.push(sc);
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		t.setParam((ArrayList<ParTuple>) n.getPd().accept(this));
		n.getB().accept(this);
		return null;
	}

	@Override
	public Object visit(CompStat n) {
		n.getS().accept(this);
		return null;
	}

	@Override
	public Object visit(Programma n) {
		this.stack.push(new SymbolTable("Globale"));
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getD().accept(this);
		n.getS().accept(this);
		this.stack.pop();
		return n;
	}

	@Override
	public Object visit(Statements n) {
		ArrayList<Stat> list = n.getChildList();
		for (Stat s : list) {
			s.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(VarDecls n) {
		for (VarDecl vd : n.getChildList()) {
			vd.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(VarInitValue n) {
		Expr e = n.getE();
		if(actualScope.getName().equals("Globale") && !isConst(e))
			throw new SemanticException("Nello scope globale � possibile assegnare unicamente valori costanti", n);	
		if (checkExpr(e, e))
			e.accept(this);
		return null;
	}

	@Override
	public Object visit(Vars n) {
		ArrayList<String> list = new ArrayList<>();
		for (IdConst id : n.getChildList()) {
			list.add(0, (String) id.accept(this));
		}
		return list;
	}
	
	@Override
	public Object visit(IncPostOp n) throws RuntimeException {
		checkExpr(n.getId(), n);
		return null;
	}
	
	@Override
	public Object visit(IncPreOp n) throws RuntimeException {
		checkExpr(n.getId(), n);
		return null;
	}

	@Override
	public Object visit(DecPostOp n) throws RuntimeException {
		checkExpr(n.getId(), n);
		return null;
	}

	@Override
	public Object visit(DecPreOp n) throws RuntimeException {
		checkExpr(n.getId(), n);
		return null;
	}

	@Override
	public Object visit(AddOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}
	
	@Override
	public Object visit(ModOp n) throws RuntimeException {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(DivOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(MultOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(SubOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(UminusOp n) {
		if (checkExpr(n.getE(), n)) {
			n.getE().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(AndOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(NotOp n) {
		if (checkExpr(n.getE(), n)) {
			n.getE().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(OrOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(EqOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(GeOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(GtOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(LeOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(LtOp n) {
		if (checkExpr(n.getE1(), n)) {
			n.getE1().accept(this);
		}
		if (checkExpr(n.getE2(), n)) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(BoolConst n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IdConst n) {
		return n.getId().accept(this);
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
		String id = (String) n.getId().accept(this);
		Tuple t = checkNotDeclared(id, n);

		checkInOutProp(id, t, ParType.IN, n);
		n.getA().accept(this);
		
		return null;
	}

	@Override
	public Object visit(CallOp n) {
		String id = (String) n.getId().accept(this);
		lastCall = (DefTuple) checkNotDeclared(id, n);
		if (n.getA() != null) {
			checkCallOpInOutProp(id, lastCall, n.getA(), n);
			n.getA().accept(this);
		}
		lastCall = null;
		return null;
	}

	@Override
	public Object visit(IfThenElseOp n) {
		if (checkExpr(n.getE(), n))
			n.getE().accept(this);
		this.stack.push(new SymbolTable("IfScope - hashCode: " + n.hashCode()));
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getB1().accept(this);
		this.stack.push(new SymbolTable("ElseScope - hashCode: " + n.hashCode()));
		this.actualScope = this.stack.top();
		n.setSymTableRefElse(actualScope);
		n.getB2().accept(this);
		return null;
	}

	@Override
	public Object visit(IfThenOp n) {
		if (checkExpr(n.getE(), n))
			n.getE().accept(this);
		this.stack.push(new SymbolTable("IfScope - hashCode: " + n.hashCode()));
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getB().accept(this);
		return null;
	}

	@Override
	public Object visit(ReadOp n) {
		ArrayList<String> list = (ArrayList<String>) n.getV().accept(this);
		Tuple t = null;
		for (String s : list) {
			t = checkNotDeclared(s, n);
			checkInOutProp(s, t, ParType.IN, n);
		}
		return null;
	}

	@Override
	// esempio aggiunta scope
	public Object visit(WhileOp n) {
		if (checkExpr(n.getE(), n))
			n.getE().accept(this);
		this.stack.push(new SymbolTable("WhileScope - hashCode: " + n.hashCode()));
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getB().accept(this);
		return null;
	}
	
	@Override
	public Object visit(ForOp n) throws RuntimeException {
		checkExpr(n.getId(), n.getId());
		if(checkExpr(n.getE1(), n.getE1()))
			n.getE1().accept(this);
		if(checkExpr(n.getE2(), n.getE2()))
			n.getE2().accept(this);
		
		this.stack.push(new SymbolTable("ForScope - hashCode: " + n.hashCode()));
		this.actualScope = this.stack.top();
		n.setSymTableRef(actualScope);
		n.getB().accept(this);
		
		return null;
	}

	@Override
	public Object visit(WriteOp n) {
		n.getA().accept(this);
		return null;
	}

	@Override
	public Object visit(Leaf n) {
		return n.getValue();
	}

	@Override
	public Object visit(VarInit n) {
		n.getViv().accept(this);
		return n.getId().accept(this);
	}

	@Override
	public Object visit(VarNotInit n) {
		return n.getId().accept(this);
	}

	@Override
	public Object visit(ParTypeLeaf n) {
		return n.getValue();
	}

	private SymbolTable.Type getValueOfLeaf(TypeLeaf t) {
		switch (t.getValue()) {
		case "BOOL":
			return Type.BOOL;
		case "DOUBLE":
			return Type.DOUBLE;
		case "INT":
			return Type.INT;
		case "CHAR":
			return Type.CHAR;
		case "STRING":
			return Type.STRING;
		default:
			return null;
		}
	}

	private SymbolTable.ParType getValueOfParTypeLeaf(String t) {
		switch (t) {
		case "IN":
			return ParType.IN;
		case "OUT":
			return ParType.OUT;
		case "INOUT":
			return ParType.INOUT;
		default:
			return null;
		}
	}

	private void checkAlreadyDeclared(String id, Node n) throws AlreadyDeclaredException {
		if (this.actualScope.containsKey(id)) {
			throw new AlreadyDeclaredException(id, this.actualScope.getName(), n);
		}
	}

	private Tuple checkNotDeclared(String id, Node n) throws NotDeclaredException {

		ArrayList<SymbolTable> temp = (ArrayList<SymbolTable>) stack.getStack();
		int index = temp.indexOf(actualScope);
		boolean find = false;
		int i = index;

		SymbolTable sb = temp.get(i);
		while (!find && i >= 0) {
			sb = temp.get(i);
			find = sb.containsKey(id);
			i--;
		}

		if (!find)
			throw new NotDeclaredException(id, this.actualScope.getName(), n);
		else
			return sb.get(id);
	}

	private void checkInOutProp(String id, Tuple t, ParType p, Node n) {
		if (t instanceof ParTuple) {
			if (((ParTuple) t).getParType() == p && p == ParType.IN)
				throw new SemanticException(String.format("Scrittura non permessa sulla variabile di input %s", id), n);
			if (((ParTuple) t).getParType() == p && p == ParType.OUT && lastCall == null)
				throw new SemanticException(String.format("Lettura non permessa dalla variabile di output %s", id), n);
		}
	}
	
	private void checkCallOpInOutProp(String id, DefTuple def, Args a, Node n) {
		Tuple t = null;
		int i = 0;
		ArrayList<ParTuple> sign = def.getParam();
		ArrayList<Expr> exprs = a.getChildList();
		
		if(sign.size() != exprs.size())
			throw new WrongArgumentNumberException(id, sign.size(), exprs.size(), n);
		
		Expr e = null;
		for(i = 0; i < sign.size(); i++) {
			e = exprs.get(i);
			if(e instanceof IdConst) {
				id = ((IdConst)e).getId().getValue();
				t = checkNotDeclared(id, n);
				if(t instanceof ParTuple && ((ParTuple)t).getParType() == ParType.OUT && sign.get(i).getParType() != ParType.OUT) 
					throw new SemanticException(String.format("La variabile %s � del tipo %s, tipo richiesto %s", 
							id, ((ParTuple)t).getParType().toString(), sign.get(i).getParType().toString()), n);
				if(t instanceof ParTuple && ((ParTuple)t).getParType() == ParType.IN && sign.get(i).getParType() != ParType.IN) 
					throw new SemanticException(String.format("La variabile %s � del tipo %s, tipo richiesto %s", 
							id, ((ParTuple)t).getParType().toString(), sign.get(i).getParType().toString()), n);
			}
		}
	}


	private boolean checkExpr(Expr e, Node n) {
		boolean flag = true;
		if (e instanceof IdConst) {
			String id = "" + e.accept(this);
			Tuple t = checkNotDeclared(id, n);
			checkInOutProp(id, t, ParType.OUT, n);
			flag = false;
		}
		return flag;
	}
	
	private boolean isConst(Expr e) {
		return (e instanceof IntConst)
				|| (e instanceof BoolConst)
				|| (e instanceof DoubleConst)
				|| (e instanceof StringConst)
				|| (e instanceof CharConst);
	}







}
