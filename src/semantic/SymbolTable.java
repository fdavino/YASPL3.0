package semantic;

import java.util.Map;
import java.util.TreeMap;
import syntaxTree.comp.Node;

public class SymbolTable extends TreeMap<String, Tuple>{

	private static final long serialVersionUID = 1L;
	private String name;
	
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
	
	public SymbolTable(String name) {
		super();
		this.name = name;
	}
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		String toReturn = "SymbolTable__"+name+"__"+hashCode() +"__\n";
		for(Map.Entry<String, Tuple> e : this.entrySet()) {
			toReturn += "<Name:\t"+e.getKey()+"\t\t"+e.getValue()+">\n";
		}
		return toReturn;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymbolTable other = (SymbolTable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
	
}
