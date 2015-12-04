package ntz.web.milanuncios;

public class Resultado implements IResultado {

	private String nombre;
	private String url;
	private String telefono;

	
	public Resultado(){}
	
	public Resultado(String nombre, String url, String telefono){
		this.nombre = nombre;
		this.url = url;
		this.telefono = telefono;
	}
	
	/*************************************************************************/
	public String getNombre() {
		return nombre;
	}

	public String getUrl() {
		return url;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		if(!(telefono.length() > 0)) telefono = "No se ha encontrado";
		this.telefono = telefono;		
	}
	/*************************************************************************/
	
	@Override
	public void navegar() {
		// TODO Auto-generated method stub
		
	}

}
