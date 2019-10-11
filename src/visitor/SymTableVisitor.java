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
	
	private Logger logger=Logger.getLogger("SymbolTable");
	private FileHandler fh;
	private SimpleFormatter formatter;
	private String pathToPrintScope;
	
	public SymTableVisitor(String pathToPrintScope) throws SecurityException, IOException {
		this.stack = new CustomStack();
		this.pathToPrintScope = pathToPrintScope;
		this.fh = new FileHandler(this.pathToPrintScope);
		logger.setUseParentHandlers(false); // remove log message from stdout
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
	}
	
	public SymTableVisitor() {
		this.stack = new CustomStack();
		this.pathToPrintScope = "";
	}
	
	@Override
	public Object visit(Args n) {
		for(Expr e : n.getChildList()) {
			if(checkExpr(e))
				e.accept(this);
		}
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
	public Object visit(Body n) {
		n.getVd().accept(this);
		n.getS().accept(this);
		if(pathToPrintScope.equals("")) {
			System.out.println(this.stack.pop().toString());
		}else {
			logger.info(this.stack.pop().toString());
		}
		this.actualScope = this.stack.top();
		return null;
	}
	
	@Override
	public Object visit(ParDecls n) {
		ArrayList<ParTuple> parArray = new ArrayList<>();
		for(ParDeclSon s : n.getChildList()) {
			parArray.add((ParTuple) s.accept(this));
			
		}
		return parArray;
	}
	
	@Override
	public Object visit(ParDeclSon n){
		String id = (String)n.getId().accept(this);
		checkAlreadyDeclared(id);
		ParType x = this.getValueOfParTypeLeaf((String)n.getParType().accept(this));
		ParTuple t = new ParTuple(Kind.VARDECL, x, this.getValueOfLeaf(n.getTypeLeaf()));
		this.actualScope.put(id, t);
		return t;
	}
	
	@Override
	public Object visit(VarDecl n){
		ArrayList<String> idList = (ArrayList<String>) n.getVdi().accept(this);
		for(String s: idList) {
			checkAlreadyDeclared(s);
			VarTuple t = new VarTuple(Kind.VARDECL, this.getValueOfLeaf(n.getT()));
			this.actualScope.put(s, t);
		}
		return null;
	}
	
	@Override
	public Object visit(VarDeclsInit n) {
		ArrayList<String> idList = new ArrayList<>();
		for(VarDeclsInitWrapper vdiw: n.getChildList()) {
			idList.add(0, (String)vdiw.accept(this));
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
		String defName = (String)n.getId().accept(this);
		this.checkAlreadyDeclared(defName);
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
		String defName = (String)n.getId().accept(this);
		this.checkAlreadyDeclared(defName);
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
		if(pathToPrintScope.equals("")) {
			System.out.println(this.stack.top().toString());
		}else {
			logger.info(this.stack.top().toString());
		}
		this.stack.pop();
		return n;
	}

	@Override
	public Object visit(Statements n) {
		ArrayList<Stat> list = n.getChildList();
		for(Stat s: list) {
			s.accept(this);
		}
		return null;
	}



	@Override
	public Object visit(VarDecls n) {
		for(VarDecl vd: n.getChildList()) {
			vd.accept(this);
		}
		return null;
	}



	@Override
	public Object visit(VarInitValue n) {
		if(checkExpr(n.getE()))
			n.getE().accept(this);
		return null;
	}

	@Override
	public Object visit(Vars n) {
		ArrayList<String> list = new ArrayList<>();
		for(IdConst id: n.getChildList()) {
			list.add(0, (String)id.accept(this));
		}
		return list;
	}

	@Override
	public Object visit(AddOp n) {
		
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(DivOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(MultOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(SubOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(UminusOp n) {
		if(checkExpr(n.getE())) {
			n.getE().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(AndOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(NotOp n) {
		if(checkExpr(n.getE())) {
			n.getE().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(OrOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(EqOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(GeOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(GtOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(LeOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
			n.getE2().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(LtOp n) {
		if(checkExpr(n.getE1())) {
			n.getE1().accept(this);
		}
		if(checkExpr(n.getE2())) {
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
		checkNotDeclared((String)n.getId().accept(this));
		if(checkExpr(n.getE()))
			n.getE().accept(this);
		return null;
	}

	@Override
	public Object visit(CallOp n) {
		checkNotDeclared((String)n.getId().accept(this));
		if(n.getA() != null) {
			n.getA().accept(this);
		}
		return null;
	}

	@Override
	public Object visit(IfThenElseOp n) {
		if(checkExpr(n.getE()))
			n.getE().accept(this);
		n.getCs1().accept(this);
		n.getCs2().accept(this);
		return null;
	}

	@Override
	public Object visit(IfThenOp n) {
		if(checkExpr(n.getE()))
			n.getE().accept(this);
		n.getCs().accept(this);
		return null;
	}

	@Override
	public Object visit(ReadOp n) {
		ArrayList<String> list = (ArrayList<String>) n.getV().accept(this);
		for(String s: list) {
			checkNotDeclared(s);
		}
		return null;
	}

	@Override
	//esempio aggiunta scope
	public Object visit(WhileOp n) {
		if(checkExpr(n.getE()))
			n.getE().accept(this);
		this.stack.push(new SymbolTable("WhileScope - hashCode: "+n.hashCode()));
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
	
	private SymbolTable.Type getValueOfLeaf(TypeLeaf t){
		switch (t.getValue()) {
		case "BOOL": return Type.BOOL;
		case "DOUBLE": return Type.DOUBLE;
		case "INT":return Type.INT;
		case "CHAR": return Type.CHAR;
		case "STRING": return Type.STRING;
		default: return null;
		}
	}
	
	private SymbolTable.ParType getValueOfParTypeLeaf(String t){
		switch (t) {
		case "IN": return ParType.IN;
		case "OUT": return ParType.OUT;
		case "INOUT":return ParType.INOUT;
		default: return null;
		}
	}
	
	private void checkAlreadyDeclared(String id) throws AlreadyDeclaredException {
		if(this.actualScope.containsKey(id)) {
			throw new AlreadyDeclaredException(id, this.actualScope.getName());
		}
	}
	
	private void checkNotDeclared(String id) throws NotDeclaredException {
		
		ArrayList<SymbolTable> temp = (ArrayList<SymbolTable>) stack.getStack(); 
		int index = temp.indexOf(actualScope);
		boolean find = false;
		int i = index;
		
		while(!find && i >= 0) {
			SymbolTable sb = temp.get(i);
			find = sb.containsKey(id);
			i--;
		}
		
		if(!find)
				throw new NotDeclaredException(id, this.actualScope.getName());
	}
	
	private boolean checkExpr(Expr e) {
		boolean flag = true;
		if(e instanceof IdConst) {
			String id = ""+e.accept(this);
			checkNotDeclared(id);
			flag = false;
		}
		return flag;
	}

}
