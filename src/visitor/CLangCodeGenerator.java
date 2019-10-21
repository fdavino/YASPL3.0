package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

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

public class CLangCodeGenerator implements Visitor<String> {

	private boolean isWrite;
	private CustomStack stack;
	private SymbolTable currentST;

	private DefTuple def; // sign of the function in exam
	private TreeMap<String, String> stringInitValue; // list od init value for string variable

	public CLangCodeGenerator() {
		isWrite = false;
		stack = new CustomStack();
		stringInitValue = new TreeMap<>();
		def = null;
	}

	@Override
	public String visit(Args n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();

		List<Expr> list = n.getChildList();
		int size = list.size();
		if (isWrite) {
			StringBuilder format = new StringBuilder();
			StringBuilder value = new StringBuilder();

			for (int i = 0; i < size; i++) {
				Expr e = list.get(i);
				value.append(needQuotes(e));
				if (i != size - 1)
					value.append(",");
				format.append(escapeC(e.getType()));
			}

			sb.append(format.toString());
			sb.append("#");
			sb.append(value.toString());
		} else {
			List<ParTuple> pars = def.getParam();
			boolean flag = false;
			for (int i = 0; i < size; i++) { // size di expr è perforza ugule alla size di pars (per controlli
												// precedenti)
				flag = pars.get(i).getParType() != ParType.IN;
				if (flag)
					sb.append("&(");
				sb.append(list.get(i).accept(this));
				if (flag)
					sb.append(")");
				if (i != size - 1)
					sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public String visit(Body n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getVd().accept(this));
		sb.append(allocateString());
		sb.append(n.getS().accept(this));
		sb.append(freeString());
		stack.pop();
		currentST = stack.top();
		return sb.toString();
	}

	@Override
	public String visit(CompStat n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getS().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(Decls n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		for (DeclsWrapper dw : n.getChildList()) {
			if (dw instanceof VarDecl)
				sb.append(dw.accept(this));
		}
		sb.append("\n");
		for (DeclsWrapper dw : n.getChildList()) {
			if (!(dw instanceof VarDecl))
				sb.append(dw.accept(this));
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String visit(DefDeclNoPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();

		StringBuilder sb = new StringBuilder();
		sb.append("void ");
		sb.append(n.getId().accept(this));
		sb.append("(){\n");
		sb.append(n.getB().accept(this));
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String visit(DefDeclPar n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();

		StringBuilder sb = new StringBuilder();
		sb.append("void ");
		sb.append(n.getId().accept(this).toString());
		sb.append("(");
		sb.append(n.getPd().accept(this).toString());
		sb.append("){\n");
		sb.append(n.getB().accept(this).toString());
		sb.append("}\n");
		return sb.toString();

	}

	@Override
	public String visit(ParDecls n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		List<ParDeclSon> list = n.getChildList();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			sb.append(list.get(i).accept(this));
			if (i != size - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	@Override
	public String visit(Programma n) throws RuntimeException {
		stack.push(n.getSymTableRef());
		currentST = stack.top();

		StringBuilder sb = new StringBuilder();
		sb.append("#include<stdio.h>\n");
		sb.append("#include<stdlib.h>\n");
		sb.append("#include<string.h>\n");
		sb.append("#define true 1\n");
		sb.append("#define false 0\n");
		sb.append("#define PB(x)((x)?\"true\":\"false\")\n");
		sb.append("typedef int bool;\n");
		sb.append("typedef char* string;\n\n");
		sb.append(n.getD().accept(this));
		sb.append("int main(void){\n");
		sb.append(allocateString());
		sb.append(n.getS().accept(this));
		sb.append(freeString());
		sb.append("return 0;\n}\n");

		stack.pop();
		return sb.toString();
	}

	@Override
	public String visit(Statements n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		for (Stat s : n.getChildList()) {
			sb.append(s.accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(VarDecl n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getT().accept(this));
		sb.append(" ");
		sb.append(n.getVdi().accept(this));
		sb.append(";\n");
		return sb.toString();
	}

	@Override
	public String visit(VarDecls n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		for (VarDecl vd : n.getChildList()) {
			sb.append(vd.accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(VarDeclsInit n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		List<VarDeclsInitWrapper> vdiw = n.getChildList();
		int size = vdiw.size();
		for (int i = 0; i < size; i++) {
			sb.append(vdiw.get(i).accept(this));
			if (i != size - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	@Override
	public String visit(VarInitValue n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(" = ");
		sb.append(n.getE().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(Vars n) throws RuntimeException {
		StringBuilder format = new StringBuilder();
		StringBuilder value = new StringBuilder();
		StringBuilder res = new StringBuilder();
		for (IdConst id : n.getChildList()) {
			if (id.getType() != null) {
				format.append(escapeC(id.getType()));
				if (!id.getType().equals(Type.STRING))
					value.append("&");
				value.append(id.accept(this));
				value.append(",");
			}
		}
		value.deleteCharAt(value.lastIndexOf(","));
		res.append(format.toString());
		res.append("#");
		res.append(value.toString());

		return res.toString();
	}

	@Override
	public String visit(AddOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" + ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(DivOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" / ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(MultOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" * ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(SubOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" - ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(UminusOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(" - ");
		sb.append(n.getE().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(AndOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" && ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(NotOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append("!(");
		sb.append(n.getE().accept(this));
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String visit(OrOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" || ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(EqOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (n.getE1().getType() == Type.STRING && n.getE2().getType() == Type.STRING)
			sb.append(createStringCompare(n.getE1(), n.getE2(), "=="));
		else {
			sb.append(n.getE1().accept(this));
			sb.append(" == ");
			sb.append(n.getE2().accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(GeOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (n.getE1().getType() == Type.STRING && n.getE2().getType() == Type.STRING)
			sb.append(createStringCompare(n.getE1(), n.getE2(), ">="));
		else {
			sb.append(n.getE1().accept(this));
			sb.append(" >= ");
			sb.append(n.getE2().accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(GtOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (n.getE1().getType() == Type.STRING && n.getE2().getType() == Type.STRING)
			sb.append(createStringCompare(n.getE1(), n.getE2(), ">"));
		else {
			sb.append(n.getE1().accept(this));
			sb.append(" > ");
			sb.append(n.getE2().accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(LeOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (n.getE1().getType() == Type.STRING && n.getE2().getType() == Type.STRING)
			sb.append(createStringCompare(n.getE1(), n.getE2(), "<="));
		else {
			sb.append(n.getE1().accept(this));
			sb.append(" <= ");
			sb.append(n.getE2().accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(LtOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (n.getE1().getType() == Type.STRING && n.getE2().getType() == Type.STRING)
			sb.append(createStringCompare(n.getE1(), n.getE2(), "<"));
		else {
			sb.append(n.getE1().accept(this));
			sb.append(" < ");
			sb.append(n.getE2().accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(BoolConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(IdConst n) throws RuntimeException {
		String id = n.getId().accept(this).toString();
		Tuple t = lookup(id);
		if (t instanceof ParTuple && ((ParTuple) t).getParType() != ParType.IN)
			return String.format("*%s", id);
		else
			return id;
	}

	@Override
	public String visit(IntConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(DoubleConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(CharConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(StringConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(AssignOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		List<Expr> list = n.getA().getChildList();
		String id = n.getId().accept(this).toString();
		if(list.size() == 1) {
			Expr e = list.get(0);
			if(e instanceof StringConst || (e instanceof IdConst && e.getType() == Type.STRING)) {
				sb.append(String.format("strcpy(%s,%s);\n",id, e.accept(this)));
			}
			else {
				sb.append(id);
				sb.append("=");
				sb.append(list.get(0).accept(this));
				sb.append(";\n");
			}
		}else {
			isWrite = true;
			String[] temp = n.getA().accept(this).toString().split("#");
			String format = temp[0];
			String value = temp[1];
			sb.append(String.format("sprintf(%s,\"%s\",%s);\n", id, format, value));
			
			isWrite = false;
		}
		return sb.toString();
	}

	@Override
	public String visit(CallOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		String id = n.getId().accept(this).toString();
		this.def = (DefTuple) lookup(id);
		sb.append(id);
		sb.append("(");
		if (n.getA() != null)
			sb.append(n.getA().accept(this));
		sb.append(");\n");
		this.def = null;
		return sb.toString();
	}

	@Override
	public String visit(IfThenElseOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append("if(");
		sb.append(n.getE().accept(this));
		sb.append("){\n");
		sb.append(n.getCs1().accept(this));
		sb.append("}\nelse{\n");
		sb.append(n.getCs2().accept(this));
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String visit(IfThenOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append("if(");
		sb.append(n.getE().accept(this));
		sb.append("){\n");
		sb.append(n.getCs().accept(this));
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String visit(ReadOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		String vars = n.getV().accept(this).toString();
		String[] temp = vars.split("#");
		String format = temp[0];
		String value = temp[1];
		sb.append("scanf(\"");
		sb.append(format);
		sb.append("\", ");
		sb.append(value);
		sb.append(");\n");
		return sb.toString();
	}

	@Override
	public String visit(WhileOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append("while(");
		sb.append(n.getE().accept(this));
		sb.append("){\n");

		stack.push(n.getSymTableRef());
		currentST = stack.top();

		sb.append(n.getB().accept(this));
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String visit(WriteOp n) throws RuntimeException {
		isWrite = true;

		StringBuilder sb = new StringBuilder();
		String args = n.getA().accept(this).toString();
		String[] temp = args.split("#");
		String format = temp[0];
		String value = temp[1];
		sb.append("printf(\"");
		sb.append(format);
		sb.append("\",");
		sb.append(value);
		sb.append(");\n");

		isWrite = false;

		return sb.toString();
	}

	@Override
	public String visit(Leaf n) throws RuntimeException {
		return n.getValue();
	}

	@Override
	public String visit(ParDeclSon n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getTypeLeaf().accept(this).toString().toLowerCase());
		sb.append(n.getParType().accept(this));
		sb.append(n.getId().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(VarInit n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		String id = n.getId().accept(this).toString();
		sb.append(id);
		String s = n.getViv().accept(this).toString();
		if (n.getViv().getType() == Type.STRING)
			stringInitValue.put(id, s);
		sb.append(s);
		return sb.toString();
	}

	@Override
	public String visit(VarNotInit n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(TypeLeaf n) throws RuntimeException {
		return String.format("%s%s", n.getValue().toLowerCase(), " ");
	}

	@Override
	public String visit(ParTypeLeaf n) throws RuntimeException {
		return "";
	}

	private String escapeC(Type t) {
		switch (t.toString()) {
		case "INT":
			return "%d";
		case "DOUBLE":
			return "%lf";
		case "CHAR":
			return "%c";
		case "STRING":
			return "%s";
		case "BOOL":
			return "%s";
		case "VOID":
			return "";
		}
		return null;
	}

	private String needQuotes(Expr e) {
		StringBuilder sb = new StringBuilder();
		if (e instanceof BoolConst) {
			sb.append(String.format("\"%s\"", e.accept(this)));
			return sb.toString();
		} else if ((e instanceof IdConst && ((IdConst) e).getType() == Type.BOOL) || isLogicOp(e))
			return (sb.append(String.format("PB(%s)", e.accept(this)))).toString();
		else
			return e.accept(this).toString();
	}

	private boolean isLogicOp(Expr e) {
		return (e instanceof AndOp) || (e instanceof NotOp) || (e instanceof OrOp) || (e instanceof EqOp)
				|| (e instanceof GeOp) || (e instanceof GtOp) || (e instanceof LeOp) || (e instanceof LtOp);
	}

	private String allocateString() {
		StringBuilder sb = new StringBuilder("");
		String valore;
		for (Entry<String, Tuple> e : currentST.entrySet()) {
			if (e.getValue() instanceof VarTuple && ((VarTuple) e.getValue()).getType() == Type.STRING) {
				sb.append(String.format("%s = malloc(256*sizeof(char));\n", e.getKey()));
				if (stringInitValue.containsKey(e.getKey())) {
					valore = stringInitValue.get(e.getKey());
					valore = valore.replaceFirst(" = ", "");
					sb.append(String.format("strcpy(%s, %s);\n", e.getKey(), valore));
					stringInitValue.remove(e.getKey());
				}
			}
		}
		return sb.toString();
	}

	private String freeString() {
		StringBuilder sb = new StringBuilder("");
		for (Entry<String, Tuple> e : currentST.entrySet()) {
			if (e.getValue() instanceof VarTuple && ((VarTuple) e.getValue()).getType() == Type.STRING)
				sb.append(String.format("free(%s);\n", e.getKey()));
		}
		return sb.toString();
	}

	private String createStringCompare(Expr e1, Expr e2, String op) {
		return String.format("((strcmp(%s,%s))%s0)?1:0", e1.accept(this), e2.accept(this), op);
	}

	private Tuple lookup(String id) {
		ArrayList<SymbolTable> temp = (ArrayList<SymbolTable>) stack.getStack();
		int index = temp.indexOf(currentST);
		boolean find = false;
		SymbolTable sb = temp.get(index);

		while (!find && index >= 0) {
			sb = temp.get(index);
			find = sb.containsKey(id);
			index--;
		}

		if (find)
			return sb.get(id);
		else
			return null;
	}

}
