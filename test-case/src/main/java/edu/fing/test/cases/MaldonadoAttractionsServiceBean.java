package edu.fing.test.cases;

import java.util.ArrayList;
import java.util.List;

import org.switchyard.component.bean.Service;

@Service(MaldonadoAttractionsService.class)
public class MaldonadoAttractionsServiceBean implements
		MaldonadoAttractionsService {
	
	@Override
	public List<AttractionDTO> getAttractions() {
		List<AttractionDTO> attractions = new ArrayList<AttractionDTO>();
		AttractionDTO attractionPuertoPE = new AttractionDTO();
		attractionPuertoPE.setAttraction("Puerto de Punta del Este");
		attractionPuertoPE.setCity("Maldonado");
		attractionPuertoPE.setDescription("El Puerto de Punta del Este, bautizado por Puerto Nuestra Señora de la Candelaria por su descubridor español Juan Díaz de Solís, es un puerto turístico equipado con casi 500 amarras. En la explanada portuaria se ubican empresas que realizan paseos marítimos, para visitar las islas (Lobo y Gorriti) o para emprender un día de pesca embarcada. El entorno al puerto está rodeado de restaurantes, pubs, y expendios de frutos del mar.");
		attractionPuertoPE.setOutside(true);
		attractions.add(attractionPuertoPE);
		
		AttractionDTO attractionPuntaShopping = new AttractionDTO();
		attractionPuntaShopping.setAttraction("Punta Shopping");
		attractionPuntaShopping.setCity("Maldonado");
		attractionPuntaShopping.setDescription("En un lugar privilegiado y a sólo cinco minutos del centro de la península, Punta Shopping es el primer y único Shopping en Punta del Este y en la región de este de país.");
		attractionPuntaShopping.setOutside(false);
		attractions.add(attractionPuntaShopping);
		
		AttractionDTO attractionCasPueblo = new AttractionDTO();
		attractionCasPueblo.setAttraction("Museo Casapueblo");
		attractionCasPueblo.setCity("Maldonado");
		attractionCasPueblo.setDescription("Casapueblo es la antigua casa de veraneo del artista uruguayo Carlos Páez Vilaró y actualmente es una ciudadela-escultura, que incluye un Museo, una galería de arte, y un hotel llamado Hotel Casapueblo que está dentro de la estructura. Se encuentra en Punta Ballena, a 30 km de Punta del Este.");
		attractionCasPueblo.setOutside(false);
		attractions.add(attractionCasPueblo);
		
		AttractionDTO attractionDedos = new AttractionDTO();
		attractionDedos.setAttraction("Los Dedos");
		attractionDedos.setCity("Maldonado");
		attractionDedos.setDescription("Conocida popularmente como Los Dedos esta obra fue creada por el artista chileno Mario Irrarazábal en el año 1982, quien la llamó Monumento al Ahogado u Hombre emergiendo a la vida. Es una escultura de cinco dedos parcialmente sumergidos en arena, que ya se ha convertido en un símbolo y uno de los puntos de referencia más reconocibles de Punta del Este.");
		attractionDedos.setOutside(true);
		attractions.add(attractionDedos);
		return attractions;
	}

}
