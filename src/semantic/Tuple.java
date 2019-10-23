package semantic;

import semantic.SymbolTable.Kind;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;

public class Tuple{
	
	private Kind kind;
		
	public Tuple(Kind kind) {
		super();
		this.kind = kind;
	}
	
	public Kind getKind() {
		return kind;
	}	
	
	
		
}
