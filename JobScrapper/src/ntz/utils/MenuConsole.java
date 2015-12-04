package ntz.utils;

public class MenuConsole {
	String[] titles;
	
	public MenuConsole(String[] titles){
		this.titles = new String[titles.length + 2];
		this.titles[0] = "#########################################";
		for(int x = 1; x <= titles.length;x++){
			this.titles[x] = titles[x-1];
		}		
		this.titles[this.titles.length-1] = "#########################################";
	}
	
	public void mostrar(){
		for(String title :this.titles){
			System.out.println(title);
		}
	}
}
