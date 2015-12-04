package ntz;

import java.awt.Event;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.common.util.concurrent.Runnables;

import ntz.web.milanuncios.Resultado;
import ntz.web.milanuncios.WebNavigation;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Button;

public class MainUI extends Shell implements Runnable {
	private Table tableReport;
	private String currentUrl = "";
	/*
	 * onGoUrl -->
	 * onPaintTable --> 
	 * */
	private String nomEvento = "";//permite condicionar el flujo del metodo run()
	
	private static final String[] URLS = {
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=2",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=3",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=4",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=5",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=6",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=7",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=8",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=9",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=10",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=11",
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n&pagina=12"
			};
	
	private static final String[] FILTROS = {
			"http://www.milanuncios.com/informaticos/?demanda=n",//Busca las ofertas de trabajo de informatica para madrid
			"http://www.milanuncios.com/informaticos-en-madrid/?demanda=n"//Busca las ofertas de trabajo de informatica para madrid
	};
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
		composite.setBounds(0, 33, 684, 208);
		
		tableReport = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		tableReport.setBounds(0, 0, 681, 208);
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
				for(String url :URLS){
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
		setSize(700, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void pintarTabla(){
		String cUrl = this.currentUrl;
		WebNavigation nav = new WebNavigation(cUrl);
		nav.ofertasMilAnuncios();
		
		ArrayList<Resultado> results = nav.getResultados();
		
		for(Resultado result : results){
			TableItem item = new TableItem(tableReport, SWT.NONE);
			item.setText(new String[]{result.getNombre(), result.getUrl(), result.getTelefono()});			
		}
		progressBar.setSelection(progressBar.getSelection() + 1);			
			
	}
	private void irA() {
		// TODO Auto-generated method stub
		String cUrl = this.currentUrl;
		WebNavigation nav = new WebNavigation();
		nav.webDriverFirefox = new FirefoxDriver();
		nav.webDriverFirefox.get(cUrl);
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
