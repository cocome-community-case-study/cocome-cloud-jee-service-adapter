package de.kit.ipd.java.utils.framework.statemachine;

import java.io.InputStream;

public interface Parser<M> {
	
	public void parse(InputStream in);
	
	public void parse(String content);
	
	public M getModel();
	
	public void setModel(M model);
	
	public void addVisitor(ParserVisitor...visitors);

}
