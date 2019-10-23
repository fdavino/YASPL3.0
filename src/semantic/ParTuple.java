package semantic;

import semantic.SymbolTable.Kind;
import semantic.SymbolTable.ParType;
import semantic.SymbolTable.Type;

public class ParTuple extends Tuple {
	
	private ParType parType;
	private Type type;
	
	public ParTuple(Kind kind, ParType parType, Type type) {
		super(kind);
		this.parType = parType;
		this.type = type;
	}

	public ParType getParType() {
		return parType;
	}

	public void setParType(ParType parType) {
		this.parType = parType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ParTuple [" + (parType != null ? "parType=" + parType + ", " : "")
				+ (type != null ? "type=" + type : "") + "]";
	}
	
	
}
