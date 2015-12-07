package ntz.web.milanuncios;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import ntz.web.Resultado;

public class WebNavigation extends Thread{
	private String url;
	public HtmlUnitDriver webDriverHidden;
	public FirefoxDriver webDriverFirefox;

	private ArrayList<Resultado> resultados;

	public WebNavigation(){}
	public WebNavigation(String url) {
		// Si no existe url, utiliza por defecto
		if (!(url.length() > 0))
			this.url = "http://www.milanuncios.com/informaticos/?demanda=n";
		else
			this.url = url;
		this.webDriverHidden = new HtmlUnitDriver(false);

	}

	/**********************************************************************************/
	public ArrayList<Resultado> getResultados() {
		return resultados;
	}
	
	/**********************************************************************************/
	public String ofertasMilAnuncios() {
		// navega a la pagina
		this.webDriverHidden.get(this.url);
		List<WebElement> elements = this.webDriverHidden.findElementsByClassName("cti");
		List<WebElement> telefonos = this.webDriverHidden.findElementsByClassName("telefonos");
		ArrayList<Resultado> resultados = new ArrayList<Resultado>();
		Resultado result = null;

		try {
			for (int i = 0; i < elements.size(); i++) {
				result = new Resultado();
				result.setNombre(elements.get(i).getText());
				result.setUrl(elements.get(i).getAttribute("href"));
				// result.setTelefono(telefonos.get(i).getText());

				resultados.add(result);
			}
		} catch (Exception err) {
			err.toString();
		}

		this.resultados = resultados;

		this.close();

		return "Proceso webScrapping finalizado";
	}

	public void close() {
		this.webDriverHidden.close();
	}
	
	// Pendiente terminar el metodo.
	public void telefonos() {
		WebElement cuadroListAnunc = this.webDriverHidden.findElementById("cuerpo");
		
		List<WebElement> lista = cuadroListAnunc.findElements(By.className("x1"));
		
		for(WebElement element:lista){
			element.click();
		}	
	}
	
}
