package ntz;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ntz.utils.selenium.JavaWebDriver;
import ntz.web.Resultado;
import ntz.web.UrlsJobScrapper;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public class MainUI extends Shell implements Runnable {
	private Table tableReport;
	private String currentUrl = "";
	/*
	 * onGoUrl -->
	 * onPaintTable --> 
	 * */
	private String nomEvento = "";//permite condicionar el flujo del metodo run()
	
	private ProgressBar progressBar;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MainUI shell = new MainUI(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public MainUI(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmBuscar = new MenuItem(menu, SWT.CASCADE);
		mntmBuscar.setText("Buscar");
		
		Menu menu_1 = new Menu(mntmBuscar);
		mntmBuscar.setMenu(menu_1);
		
		MenuItem mntmMilanuncioscom = new MenuItem(menu_1, SWT.NONE);
		
		mntmMilanuncioscom.setText("MilAnuncios.com");
		
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setBounds(10, 2, 545, 25);
		progressBar.setMaximum(11);//Este valor deberia ser dinamico
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(0, 33, 684, 408);
		
		tableReport = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		tableReport.setBounds(0, 0, 681, 408);
		tableReport.setHeaderVisible(true);
		tableReport.setLinesVisible(true);
		
		TableColumn colNombre = new TableColumn(tableReport, SWT.NONE);
		colNombre.setWidth(378);
		colNombre.setText("Nombre");
		
		TableColumn colUrl = new TableColumn(tableReport, SWT.NONE);
		colUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectionEvent evento = e;
				
				String txt = evento.text;
				System.out.println(txt);
			}
		});
		colUrl.setWidth(68);
		colUrl.setText("URL");
		
		TableColumn tblclmnTelefono = new TableColumn(tableReport, SWT.NONE);
		tblclmnTelefono.setWidth(131);
		tblclmnTelefono.setText("telefono");
		
		mntmMilanuncioscom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {							
				for(String url :UrlsJobScrapper.MILANUNCIOSURLS){
					nomEvento = "onPaintTable";
					currentUrl = url;
					run();
				}
				
				//
			}
		});
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int totalFilas = tableReport.getItemCount();
				int filaSelected = -1; 
				
				tableReport.isSelected(0);
						
				for(int x = 0 ; x < totalFilas; x++){
					TableItem fila = tableReport.getItem(x);
					boolean selected = fila.getChecked();
					
					if(selected){
						nomEvento = "onGoUrl";
						currentUrl = fila.getText(1); // <-- no se si saca toda la fila , o solo una columna de la fila
						run();
					}
					
				}
				
			}
		});
		btnNewButton.setBounds(561, 2, 113, 25);
		btnNewButton.setText("Ir a ...");
		createContents();
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Buscar Trabajo");
		setSize(700, 500);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void pintarTabla(){
		//Inicia un webdriver Oculto para buscar la url pasada desde el campo del objeto
		String cUrl = this.currentUrl;
		JavaWebDriver jwd = new JavaWebDriver(cUrl);
		jwd.init(0);
		//Devuelve resultados de la query
		ArrayList<Resultado> results = jwd.getResultados();
		
		
		//Pinta la tabla con los resultados de la query
		for(Resultado result : results){
			TableItem item = new TableItem(tableReport, SWT.NONE);
			item.setText(new String[]{result.getNombre(), result.getUrl(), result.getTelefono()});			
		}
		progressBar.setSelection(progressBar.getSelection() + 1);			
			
	}
	private void irA() {
		// TODO Auto-generated method stub
		String cUrl = this.currentUrl;
		JavaWebDriver jwd = new JavaWebDriver();
		jwd.init(1);
		jwd.webDriver.get(cUrl);
	}
	@Override
	public void run() {
		
		//Condiciona
		switch(nomEvento){
			case "onGoUrl":
				irA();
				break;
			case "onPaintTable":
				pintarTabla();
				break;
		}
		
		
		
	}

	
}
