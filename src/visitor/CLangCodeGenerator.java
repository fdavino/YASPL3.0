package visitor;

import syntaxTree.Args;
import syntaxTree.Body;
import syntaxTree.CompStat;
import syntaxTree.Decls;
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
import syntaxTree.utils.ParDeclSon;
import syntaxTree.varDeclInitOp.VarInit;
import syntaxTree.varDeclInitOp.VarNotInit;
import syntaxTree.wrapper.DeclsWrapper;
import syntaxTree.wrapper.VarDeclsInitWrapper;

public class CLangCodeGenerator implements Visitor<String>{

	@Override
	public String visit(Args n) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
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
		toReturn += "int main(){\n";
		return toReturn;
	}

	@Override
	public String visit(DefDeclNoPar n) throws RuntimeException {
		String toReturn = "void ";
		toReturn += n.getId().accept(this)+"(){\n";
		toReturn += n.getB().accept(this);
		return toReturn;
	}

	@Override
	public String visit(DefDeclPar n) throws RuntimeException {
		String toReturn = "void ";
		toReturn += n.getId().accept(this)+"(";
		toReturn += n.getPd().accept(this)+"){\n";
		toReturn += n.getB().accept(this);
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
		String toPrint = "#include<stdio.h>\n";
		toPrint += "#include<string.h>\n";
		toPrint += "#include<stdlib.h>\n";
		toPrint += "#typedef int bool;\n";
		toPrint += "#define false 0\n";
		toPrint += "#define true 1\n";
		
		toPrint += n.getD().accept(this);
		toPrint += n.getS().accept(this);
		
		toPrint += "return 0;\n}";
		System.out.println(toPrint);
		return toPrint;
	}

	@Override
	public String visit(Statements n) throws RuntimeException {
		String toReturn = "";
		for(Stat s : n.getChildList()) {
			toReturn += s.accept(this);
		}
		//toReturn += "return 0;\n}";
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(AddOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " + ";
		toReturn += n.getE2().accept(this);
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
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " && ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(NotOp n) throws RuntimeException {
		String toReturn = "!";
		toReturn += n.getE().accept(this);
		return toReturn;
	}

	@Override
	public String visit(OrOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " || ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(EqOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " == ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(GeOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " >= ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(GtOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " > ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(LeOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " <= ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(LtOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getE1().accept(this);
		toReturn += " < ";
		toReturn += n.getE2().accept(this);
		return toReturn;
	}

	@Override
	public String visit(BoolConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(IdConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(IntConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(DoubleConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(CharConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(StringConst n) throws RuntimeException {
		return n.getId().getValue();
	}

	@Override
	public String visit(AssignOp n) throws RuntimeException {
		String toReturn = "";
		toReturn += n.getId().accept(this);
		toReturn += " = ";
		toReturn += n.getE().accept(this)+ ";\n";
		return toReturn;
	}

	@Override
	public String visit(CallOp n) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(Leaf n) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
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
		toReturn += n.getId().accept(this);
		toReturn += "=";
		toReturn += n.getViv().accept(this);
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
		return (n.getValue() == "IN")?"":"*";
	}

}
