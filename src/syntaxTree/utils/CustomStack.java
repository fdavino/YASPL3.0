package syntaxTree.utils;

import java.util.ArrayList;
import java.util.List;

import semantic.SymbolTable;

public class CustomStack {

	private ArrayList<SymbolTable> stack;
	
	public CustomStack() {
		stack = new ArrayList<>();
	}
	
	public void push(SymbolTable s) {
		stack.add(stack.size(), s);
	}
	
	public SymbolTable pop() {
		return (stack.size() == 0)?null:stack.remove(stack.size() - 1);
	}
	
	public SymbolTable top() {
		return (stack.size() == 0)?null:stack.get(stack.size() - 1);
	}
	
	public List<SymbolTable> getStack(){
		return stack;
	}
	
	
	
}
