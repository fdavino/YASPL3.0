package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import semantic.DefTuple;
import semantic.ParTuple;
import semantic.SymbolTable;
import semantic.Tuple;
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

	private boolean isWrite;
	private CustomStack stack;
	private SymbolTable currentST;
	
	private DefTuple def = null;

	public CLangCodeGenerator() {
		isWrite = false;
		stack = new CustomStack();
	}

	@Override
	public String visit(Args n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		
		List<Expr> list = n.getChildList();
		int size = list.size();
		if(isWrite) {
			StringBuilder format = new StringBuilder();
			StringBuilder value = new StringBuilder();

			for(int i = 0; i < size; i++) {
				Expr e = list.get(i);
				value.append(needQuotes(e));
				if(i != size-1)
					value.append(",");
				format.append(escapeC(e.getType()));
			}

			sb.append(format.toString());
			sb.append("#");
			sb.append(value.toString());
		}
		else {
			String id;
			ParTuple pt;
			for(int i = 0; i < size; i++) {
				Expr e = list.get(i);
				
				if(e instanceof IdConst) {
					id = ((IdConst)e).getId().getValue();
					pt = (ParTuple) lookup(id);
					if(pt.getParType() != ParType.IN)
						sb.append("&");
				}
				
				sb.append(e.accept(this));
				if(i != size-1)
					sb.append(",");
			}
		}


		return sb.toString();
	}

	@Override
	public String visit(Body n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getVd().accept(this));
		sb.append(n.getS().accept(this));
		
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
		for(DeclsWrapper dw : n.getChildList()) {
			if(dw instanceof VarDecl)
				sb.append(dw.accept(this));
		}
		sb.append("\n");
		for(DeclsWrapper dw : n.getChildList()) {
			if(!(dw instanceof VarDecl))
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
		SymbolTable tab = n.getSymTableRef();
		stack.push(tab);
		currentST = stack.top();
		ArrayList<String> ids = new ArrayList<>();
		ArrayList<ParTuple> pars = new ArrayList<>();
		for(Entry<String, Tuple> entry : tab.entrySet()) {
			if(entry.getValue() instanceof ParTuple && ((ParTuple)entry.getValue()).getParType() != ParType.IN) {
				pars.add((ParTuple)entry.getValue());
				ids.add(entry.getKey());
			}
		}
		
		int size = pars.size();
				
		
		StringBuilder sb = new StringBuilder();
		sb.append("void ");
		String id = n.getId().accept(this).toString(); 
		sb.append(id);
		sb.append("(");
		sb.append(n.getPd().accept(this).toString());
		sb.append("){\n");
		String tmp = n.getB().accept(this).toString();
		String uno = null;
		String due = tmp;
		
		
		for(int i = 0; i < size; i++) {
			uno = due.replaceAll(ids.get(i), String.format("(*%s)", ids.get(i)));
			due = uno;
		}
		
		sb.append(uno);
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public String visit(ParDecls n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		List<ParDeclSon> list = n.getChildList();
		int size = list.size();
		for(int i = 0; i < size ; i++) {
			sb.append(list.get(i).accept(this));
			if(i != size - 1)
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
		sb.append("#define 1 true\n");
		sb.append("#define 0 false\n");
		sb.append("#define PB(x)((x)?\"true\":\"false\")\n");
		sb.append("typedef int bool;\n");
		sb.append("typedef char* string;\n\n");
		sb.append(n.getD().accept(this));
		sb.append("int main(void){\n");
		sb.append(n.getS().accept(this));
		sb.append("return 0;\n}\n");
		
		stack.pop();
		return sb.toString();
	}

	@Override
	public String visit(Statements n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		for(Stat s : n.getChildList()) {
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
		for(VarDecl vd : n.getChildList()) {
			sb.append(vd.accept(this));
		}
		return sb.toString();
	}

	@Override
	public String visit(VarDeclsInit n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		List<VarDeclsInitWrapper> vdiw = n.getChildList();
		int size = vdiw.size();
		for(int i = 0; i < size; i++) {
			sb.append(vdiw.get(i).accept(this));
			if(i != size - 1)
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
		for(IdConst id : n.getChildList()) {
			if(id.getType() != null) {
				format.append(escapeC(id.getType()));
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
		sb.append("!");
		sb.append(n.getE().accept(this));
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
		sb.append(n.getE1().accept(this));
		sb.append(" == ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(GeOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" >= ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(GtOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" > ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(LeOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" <= ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(LtOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getE1().accept(this));
		sb.append(" < ");
		sb.append(n.getE2().accept(this));
		return sb.toString();
	}

	@Override
	public String visit(BoolConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
	}

	@Override
	public String visit(IdConst n) throws RuntimeException {
		return n.getId().accept(this).toString();
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
		sb.append(n.getId().accept(this));
		sb.append("=");
		sb.append(n.getE().accept(this));
		sb.append(";\n");
		return sb.toString();
	}

	@Override
	public String visit(CallOp n) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		String id = n.getId().accept(this).toString();
		sb.append(id);
		Tuple t = lookup(id);
		if(t instanceof DefTuple) {
			def = (DefTuple) t;
			sb.append("(");
			sb.append(n.getA().accept(this));
			sb.append(");\n");
		}
		
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
		sb.append(n.getId().accept(this).toString());
		sb.append(n.getViv().accept(this));
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
		return (!n.getValue().equalsIgnoreCase("IN"))?"*":"";	
	}

	private String escapeC(Type t) {
		switch(t.toString()) {
		case "INT": return "%d";
		case "DOUBLE": return "%lf";
		case "CHAR": return "%c";
		case "STRING": return "%s";
		case "BOOL": return "%s";
		case "VOID": return "";
		}
		return null;
	}

	private String needQuotes(Expr e) {

		StringBuilder sb = new StringBuilder();
		if(e instanceof StringConst || e instanceof BoolConst)
			return (sb.append(String.format("\"%s\"", e.accept(this)))).toString();
		else 
			if(e instanceof CharConst)
				return (sb.append(String.format("\'%s\'", e.accept(this)))).toString();
			else
				if(e instanceof IdConst && ((IdConst)e).getType() == Type.BOOL)
					return (sb.append(String.format("PB(%s)", e.accept(this)))).toString();
				else
					return e.accept(this).toString();
	}
	
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
		
		if(find)
			return sb.get(id);
		else
			return null;
	}

}
