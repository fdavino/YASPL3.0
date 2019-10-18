package semantic;

import semantic.SymbolTable.Kind;
import semantic.SymbolTable.Type;

public class VarTuple extends Tuple{

	private Type type;
	
	public VarTuple(Kind kind, Type type) {
		super(kind);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "VarTuple [" + (type != null ? "type=" + type : "") + "]";
	}	
	
}
