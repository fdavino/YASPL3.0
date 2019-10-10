package semantic;

import semantic.SymbolTable.Kind;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;

public class Tuple{
	
	private Kind kind;
	private boolean used;
		
	public Tuple(Kind kind) {
		super();
		this.kind = kind;
	}
	
	public Kind getKind() {
		return kind;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}	
	
	
		
}
