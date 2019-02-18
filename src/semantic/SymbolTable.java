package semantic;

import java.util.Map;
import java.util.TreeMap;
import syntaxTree.comp.Node;

public class SymbolTable extends TreeMap<String, Tuple>{

	private static final long serialVersionUID = 1L;
	
	public enum Kind{
		VARDECL,
		DEFDECL
	}
	public enum Type{
		INT,
		STRING,
		DOUBLE,
		CHAR,
		BOOL,
		VOID
	}
	public enum ParType{
		IN,
		OUT,
		INOUT,
	}
	
	public SymbolTable() {
		super();
	}

	@Override
	public String toString() {
		String toReturn = "SymbolTable__"+ hashCode() +"__\n";
		for(Map.Entry<String, Tuple> e : this.entrySet()) {
			toReturn += "<Name:\t"+e.getKey()+"\t\t"+e.getValue()+">\n";
		}
		return toReturn;
	}
	
	
	
}
