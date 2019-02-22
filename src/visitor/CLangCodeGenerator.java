package visitor;

import java.util.List;

import com.sun.javafx.geom.transform.GeneralTransform3D;

import exception.NotDefinedElementException;
import semantic.DefTuple;
import semantic.ParTuple;
import semantic.SymbolTable;
import semantic.Tuple;
import semantic.VarTuple;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;
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

public class CLangCodeGenerator implements Visitor<String>{

	private CustomStack stack;
	private boolean isWrite;
	
	public CLangCodeGenerator() {
		stack = new CustomStack();
	}
	
	@Override
	public String visit(Args n) throws RuntimeException {
		if(isWrite) {
		String format= "";
		String value = "";
		int i=0;
		Expr e;
		int size = n.getChildList().size();
		
		for(i = 0; i<size; i++) {
			e = n.getChildList().get(i);
			format += "%s";
			value += stringComposer(e);
			if(i != size - 1)
				value+=",";
		}
		return format+"\","+value;
		}
		else {
			String toReturn = "", id;
			Expr e;
			List<Expr> args = n.getChildList();
			int i, size = args.size();
			for(i = 0; i < size; i++) {
				e = args.get(i);
				id = (String)e.accept(this);
				toReturn += id;
				if(i != size - 1)
					toReturn += ",";
			}			
			return toReturn;
		}
	}

	@Override
	public String visit(Body n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getVd().accept(this);
		toReturn += n.getS().accept(this);
		return toReturn;
	}

	@Override
	public String visit(CompStat n) throws RuntimeException {
		return (String) n.getS().accept(this);
	}

	@Override
	public String visit(Decls n) throws RuntimeException {
		String toReturn = "";
		for(DeclsWrapper dw : n.getChildList()) {
			if(dw instanceof VarDecl)
				toReturn += dw.accept(this);
		}
		for(DeclsWrapper dw : n.getChildList()) {
			if(!(dw instanceof VarDecl))
				toReturn += dw.accept(this);
		}
		toReturn += "\nint main(){\n";
		return toReturn;
	}

	@Override
	public String visit(DefDeclNoPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		String toReturn = "void ";
		toReturn += n.getId().accept(this)+"(){\n";
		toReturn += n.getB().accept(this);
		toReturn += "\n}";
		stack.pop();
		return toReturn;
	}

	@Override
	public String visit(DefDeclPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		String toReturn = "void ";
		toReturn += n.getId().accept(this)+"(";
		toReturn += n.getPd().accept(this)+"){\n";
		toReturn += n.getB().accept(this);
		toReturn += "\n}";
		stack.pop();
		return toReturn;
	}

	@Override
	public String visit(ParDecls n) throws RuntimeException {
		int i;
		int size = n.getChildList().size();
		String toReturn = "";
		ParDeclSon pson;
		for(i = 0; i<size; i++) {
			pson = n.getChildList().get(i);
			toReturn += pson.accept(this);
			if(i != size -1)
				toReturn += ",";
		}
		return toReturn;
	}

	@Override
	public String visit(Programma n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		String toPrint = "#include<stdio.h>\n";
		toPrint += "#include<string.h>\n";
		toPrint += "#include<stdlib.h>\n";
		toPrint += "typedef int bool;\n";
		toPrint += "#define false 0\n";
		toPrint += "#define true 1\n";
		toPrint += "typedef char* string;\n";
		toPrint += "char *concatUtils;\n";
		toPrint += "char *convertUtils;\n";
		toPrint += concatUtils();
		
		toPrint += n.getD().accept(this);
		toPrint += n.getS().accept(this);
		
		toPrint += "free(concatUtils);\n";
		toPrint += "free(convertUtils);\n";
		toPrint += "return 0;\n}";
		stack.pop();
	
		return toPrint;
	}

	@Override
	public String visit(Statements n) throws RuntimeException {
		String toReturn = "";
		for(Stat s : n.getChildList()) {
			toReturn += s.accept(this);
		}
		return toReturn;
	}

	@Override
	public String visit(VarDecl n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getT().accept(this);
		toReturn += n.getVdi().accept(this);
		return toReturn;
	}

	@Override
	public String visit(VarDecls n) throws RuntimeException {
		String toReturn = "";
		for(VarDecl vd : n.getChildList()){
			toReturn += vd.accept(this);
		}
		return toReturn;
	}

	@Override
	public String visit(VarDeclsInit n) throws RuntimeException {
		String toReturn = "";
		int size = n.getChildList().size();
		VarDeclsInitWrapper vdiw;
		for (int i = 0; i<size; i++) {
			vdiw = n.getChildList().get(i);
			toReturn += vdiw.accept(this);
			if(i != size - 1)
				toReturn +=",";
		}
		return toReturn+";\n";
	}

	@Override
	public String visit(VarInitValue n) throws RuntimeException {
		return (String) n.getE().accept(this);
	}

	@Override
	public String visit(Vars n) throws RuntimeException {
		String format= "";
		String value = "";
		int i=0;
		IdConst id;
		int size = n.getChildList().size();
		
		for(i = 0; i<size; i++) {
			id = n.getChildList().get(i);
			format += getEscapeByType(id.getType());
			value += "&"+id.accept(this);
			if(i != size - 1)
				value+=",";
		}
		return format+"\","+value;
	}

	@Override
	public String visit(AddOp n) throws RuntimeException {
		String toReturn = "";
		if(n.getE1().getType() == Type.STRING || n.getE2().getType() == Type.STRING) {
			toReturn += "ccUtils(";
			toReturn += stringComposer(n.getE1()) + ",";
			toReturn += stringComposer(n.getE2()) +" )";
		}else {
			toReturn += n.getE1().accept(this);
			toReturn += " + ";
			toReturn += n.getE2().accept(this);
		}
		
		return toReturn;
	}

	@Override
	public String visit(DivOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " / ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(MultOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " * ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(SubOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " - ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}
	

	@Override
	public String visit(UminusOp n) throws RuntimeException {
		String toReturn = "-";
		toReturn += n.getE().accept(this);
		return toReturn;
	}

	@Override
	public String visit(AndOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " && ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(NotOp n) throws RuntimeException {
		String toReturn = "!(";
		toReturn += n.getE().accept(this)+")";
		return toReturn;
	}

	@Override
	public String visit(OrOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " || ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(EqOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " == ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(GeOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " >= ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(GtOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " > ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(LeOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " <= ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(LtOp n) throws RuntimeException {
		String toReturn = "(";
		toReturn += n.getE1().accept(this);
		toReturn += " < ";
		toReturn += n.getE2().accept(this) + ")";
		return toReturn;
	}

	@Override
	public String visit(BoolConst n) throws RuntimeException {
		return (String) n.getId().accept(this);
	}

	@Override
	public String visit(IdConst n) throws RuntimeException {
		String toReturn = "";
		String key = (String) n.getId().accept(this);
		Tuple t = lookup(key);
		if(t instanceof ParTuple) {
			ParTuple pt = (ParTuple) t;
			if(pt.getParType() == ParType.INOUT || pt.getParType() == ParType.OUT)
				toReturn += "*";
		}
		toReturn += key;
		return toReturn;
	}

	@Override
	public String visit(IntConst n) throws RuntimeException {
		return (String) n.getId().accept(this);
	}

	@Override
	public String visit(DoubleConst n) throws RuntimeException {
		return (String) n.getId().accept(this);
	}

	@Override
	public String visit(CharConst n) throws RuntimeException {
		return (String) n.getId().accept(this);
	}

	@Override
	public String visit(StringConst n) throws RuntimeException {
		return (String) n.getId().accept(this);
	}

	@Override
	public String visit(AssignOp n) throws RuntimeException {
		String toReturn = "";
		if(n.getId().getType() == Type.STRING || n.getE().getType() == Type.STRING) {
			toReturn += "strcpy(";
			toReturn += n.getId().accept(this) + ",";
			toReturn += n.getE().accept(this) + ");\n";
		}
		else {
			toReturn += n.getId().accept(this);
			toReturn += " = ";
			toReturn += n.getE().accept(this)+ ";\n";
		}
		return toReturn;
	}

	@Override
	public String visit(CallOp n) throws RuntimeException {
		isWrite = false;
		int i = 0;
		String id = (String) n.getId().accept(this);
		DefTuple t = (DefTuple) lookup(id);
		String toReturn = id + "(";
		String args[] = ((String)n.getA().accept(this)).split(",");
		for(ParTuple v : t.getParam()) {
			if(v.getParType() == ParType.OUT || v.getParType() == ParType.INOUT)
				args[i] = "&"+args[i];
			toReturn += args[i];
			if(i != args.length - 1)
				toReturn += ",";
			i++;
		}
		toReturn += ");\n";
		return toReturn;
	}

	@Override
	public String visit(IfThenElseOp n) throws RuntimeException {
		String toReturn = "if(";
		toReturn += n.getE().accept(this) + "){\n";
		toReturn += n.getCs1().accept(this) + "}\n";
		toReturn += "else{\n";
		toReturn += n.getCs2().accept(this) + "}\n";
		return toReturn;
	}

	@Override
	public String visit(IfThenOp n) throws RuntimeException {
		String toReturn = "if(";
		toReturn += n.getE().accept(this) + "){\n";
		toReturn += n.getCs().accept(this) + "}\n";
		return toReturn;
	}

	@Override
	public String visit(ReadOp n) throws RuntimeException {
		String toReturn = "scanf(\"";
		toReturn += n.getV().accept(this);
		toReturn += ");\n";
		return toReturn;
	}

	@Override
	public String visit(WhileOp n) throws RuntimeException {
		String toReturn = "while(";
		toReturn += n.getE().accept(this) + "){\n";
		toReturn += n.getCs().accept(this) + "}\n";
		return toReturn;
	}

	@Override
	public String visit(WriteOp n) throws RuntimeException {
		isWrite = true;
		String toReturn = "";
		toReturn += "printf(\"";
		toReturn += n.getA().accept(this);
		toReturn += ");\n";
		return toReturn;
	}

	@Override
	public String visit(Leaf n) throws RuntimeException {
		return n.getValue();
	}

	@Override
	public String visit(ParDeclSon n) throws RuntimeException {
		return n.getTypeLeaf().accept(this)
				+ " " + n.getParType().accept(this) 
				+ "" + n.getId().accept(this);
	}

	@Override
	public String visit(VarInit n) throws RuntimeException {
		String toReturn = "";
		if(n.getId().getType() == Type.STRING) {
			toReturn += n.getId().accept(this) + "=\"";
			toReturn += n.getViv().accept(this) + "\"";
		}
		else {
			toReturn += n.getId().accept(this);
			toReturn += "=";
			toReturn += n.getViv().accept(this);
		}
		
		return toReturn;
	}

	@Override
	public String visit(VarNotInit n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getId().accept(this);
		return toReturn;
	}

	@Override
	public String visit(TypeLeaf n) throws RuntimeException {
		return n.getValue().toLowerCase()+" ";
	}

	@Override
	public String visit(ParTypeLeaf n) throws RuntimeException {
		return "";
	}
	
	private String getEscapeByType(Type t) {
		switch(t) {
		case INT: return "%d";
		case DOUBLE : return "%lf";
		case BOOL : return "%s";
		case CHAR : return "%c";
		case STRING : return "%s";
		case VOID: return null;
		}
		return null;
	}
	
	private Tuple lookup(String id) throws NotDefinedElementException{
		List<SymbolTable> list = stack.getStack();
		int i = list.size() - 1;
		while(i >= 0 && !list.get(i).containsKey(id))
			i--;
		if(i < 0)
			throw new NotDefinedElementException(id);
		else
			return list.get(i).get(id);
	}
	
	private String stringComposer(Expr e) {
		String toReturn = "";
		String dq = "\"";
		String sq = "'";
		if(requireQuotes(e)) {
			if(e instanceof CharConst)
				toReturn += sq + e.accept(this) + sq;
			else
				toReturn += dq + e.accept(this) + dq;
		}
		else {
			switch(e.getType()) {
			case INT : toReturn += "itsUtils("+e.accept(this)+")"; break;
			case DOUBLE : toReturn += "dtsUtils("+e.accept(this)+")"; break;
			case CHAR : toReturn += "ctsUtils("+e.accept(this)+")"; break;
			case BOOL : toReturn += "btsUtils("+e.accept(this)+")"; break;
			case STRING : toReturn += e.accept(this); break;
			}	
		}
		
		return toReturn;
	}
	
	private boolean requireQuotes(Expr e) {
		if(e instanceof BoolConst || 
				e instanceof IntConst ||
				e instanceof DoubleConst ||
				e instanceof CharConst ||
				e instanceof StringConst)
			return true;
		return false;
	}
	
	private String concatUtils() {
		String toReturn = "char *itsUtils(int var) {"
				+ "convertUtils = malloc(512);"
				+ "sprintf(convertUtils,\"%d\",var);"
				+ "return convertUtils;"
				+ "}\n";
		
		toReturn += "char *dtsUtils(double var) {"
				+ "convertUtils = malloc(512);"
				+ "sprintf(convertUtils,\"%lf\",var);"
				+ "return convertUtils;"
				+ "}\n";
		
		toReturn += "char *btsUtils(bool var) {"
				+ "convertUtils = malloc(512);"
				+ "if(var)"
				+ "sprintf(convertUtils,\"%s\",\"true\");"
				+ "else "
				+ "sprintf(convertUtils,\"%s\",\"false\");"
				+ "return convertUtils;"
				+ "}\n";
		
		toReturn += "char *ctsUtils(char var) {"
				+ "convertUtils = malloc(512);"
				+ "sprintf(convertUtils,\"%c\",var);"
				+ "return convertUtils;"
				+ "}\n";

		toReturn += "char *ccUtils(char *var1, char *var2) {"
				+ "concatUtils = malloc(512);"
				+ "sprintf(concatUtils,\"%s%s\",var1,var2);"
				+ "return concatUtils;"
				+ "}\n";
		
		return toReturn;
	}
	

}
