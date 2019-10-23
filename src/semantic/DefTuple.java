package semantic;

import java.util.ArrayList;

import semantic.SymbolTable.Kind;

public class DefTuple extends Tuple{
	
	private ArrayList<ParTuple> param;
	
	public DefTuple(Kind kind) {
		super(kind);
		param = new ArrayList<>();
	}

	public ArrayList<ParTuple> getParam() {
		return param;
	}

	public void setParam(ArrayList<ParTuple> param) {
		this.param = param;
	}
	
	public int getNumParam() {
		return (param!=null)?param.size():0;
	}

	@Override
	public String toString() {
		return "DefTuple [" + (param != null ? "param=" + param : "") + "]";
	}
	
	
}
