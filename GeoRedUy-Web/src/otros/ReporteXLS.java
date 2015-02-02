package otros;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;



import pojos.Oferta;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * 
 * Reporte XLS
 * 
 */
public class ReporteXLS {

	// Titulos
	private Colour COLOR_FONDO_TITULOS = Colour.GRAY_80;
	private Colour COLOR_TEXTO_TITULOS = Colour.WHITE;

	private FontName FUENTE_TITULOS = WritableFont.ARIAL;
	private int TAMANO_TITULOS = 11;

	// Datos
	private Colour[] COLOR_FONDO_DATOS = new Colour[] { Colour.WHITE, Colour.GRAY_25 };

	private Colour COLOR_TEXTO_DATOS = Colour.BLACK;

	private FontName FUENTE_DATOS = WritableFont.ARIAL;
	private int TAMANO_DATOS = 9;

	public ReporteXLS() {

	}


	
	public void procesarArchivoReporteT(File xls,
			JSONArray ofertas) throws Exception {

		WritableWorkbook libro = Workbook.createWorkbook(xls);
		WritableSheet hoja = libro.createSheet("Reporte", 0);

		List<String> columnas = Arrays.asList("Empresa", "Local", "Oferta",
				"Precio($)", "Cantidad", "Monto Total($)", "Calificacion");

		String mensaje = "Reporte";
		generarEstructuraBasico(hoja, columnas, mensaje,
				"Ofertas");

		int fila = 4;
		int columna;
		int colorFondo = 0;
		System.out.println(ofertas.length());
		for (int i =0; i<ofertas.length();i++) {

			JSONObject oferta=ofertas.getJSONObject(i);
			String empresa= oferta.getString("empresa");
			String local=oferta.getString("local");
			String nombreoferta=oferta.getString("nombre");
			String precio=oferta.getString("costo");
			String cantidad=oferta.getString("cantidad");
			String monto=oferta.getString("monto");
			String calificacion=oferta.getString("calificacion");
			
			columna = 0;

			Colour color = COLOR_FONDO_DATOS[colorFondo++ % 2];
			

			Label etiqueta = new Label(columna++, fila,
					 empresa
							, generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			etiqueta = new Label(columna++, fila,
					 local
							 ,
					generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			etiqueta = new Label(columna++, fila,
					nombreoferta, generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			etiqueta = new Label(columna++, fila,
					precio	, generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			etiqueta = new Label(columna++, fila,
					cantidad, generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			etiqueta = new Label(columna++, fila,
					monto, generarFormatoDatos(color));

			hoja.addCell(etiqueta);
			
			etiqueta = new Label(columna++, fila,
					calificacion, generarFormatoDatos(color));

			hoja.addCell(etiqueta);

			
		fila++;
		
		ajustarColumnas(hoja);
		}

		

		libro.write();
		libro.close();
	}

	

	private void generarEstructuraBasico(WritableSheet hoja,
			List<String> columnas, String personaYfecha, String tipoReporte)
			throws RowsExceededException, WriteException {

		int i = 0;
		for (; i < columnas.size(); i++) {

			hoja.addCell(new Label(i, 2, columnas.get(i),
					generarFormatoTitulos()));

			hoja.addCell(new Label(i, 3, "", generarFormatoTitulos()));
		}

		hoja.addCell(new Label(0, 0, "Reporte", generarFormatoTitulos()));
		hoja.addCell(new Label(1, 0, "de", generarFormatoTitulos()));
		hoja.addCell(new Label(2, 0, tipoReporte, generarFormatoTitulos()));
	}

	private WritableCellFormat generarFormatoTitulos() throws WriteException {
		WritableFont wf = new WritableFont(FUENTE_TITULOS, TAMANO_TITULOS,
				WritableFont.BOLD);
		wf.setColour(COLOR_TEXTO_TITULOS);
		WritableCellFormat cellFormat = new WritableCellFormat(wf);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setBackground(COLOR_FONDO_TITULOS, Pattern.SOLID);

		return cellFormat;
	}

	private WritableCellFormat generarFormatoDatos(Colour colorFondo)
			throws WriteException {
		WritableFont wf = new WritableFont(FUENTE_DATOS, TAMANO_DATOS,
				WritableFont.NO_BOLD);
		wf.setColour(COLOR_TEXTO_DATOS);
		WritableCellFormat cellFormat = new WritableCellFormat(wf);
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setBackground(colorFondo, Pattern.SOLID);

		return cellFormat;
	}

	private void ajustarColumnas(WritableSheet hoja) {
		
		
		CellView ajuste = new CellView();
		
		ajuste.setAutosize(true);
		for (int i = 0; i < hoja.getColumns(); i++) {
			
		
				hoja.setColumnView(i, ajuste);
			
			
		}

	}

}
