package dao;

public enum Type {
	
	TYPE01,TYPE02;
	
	public String toString(){
		switch(this){
			case TYPE01:return "type01";
			case TYPE02:return "type02";
			default:return "unspecified";
		}
		
	}
}
