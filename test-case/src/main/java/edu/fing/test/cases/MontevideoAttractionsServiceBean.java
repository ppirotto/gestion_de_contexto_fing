package edu.fing.test.cases;

import java.util.ArrayList;
import java.util.List;

import org.switchyard.component.bean.Service;

@Service(MontevideoAttractionsService.class)
public class MontevideoAttractionsServiceBean implements
		MontevideoAttractionsService {

	@Override
	public List<AttractionDTO> getAttractions() {
		List<AttractionDTO> attractions = new ArrayList<AttractionDTO>();
		
		AttractionDTO attractionPuerto = new AttractionDTO();
		attractionPuerto.setAttraction("Mercado del Puerto");
		attractionPuerto.setCity("Montevideo");
		attractionPuerto.setDescription("El Mercado del Puerto de Montevideo es un antiguo mercado con más de 130 años de historia, donde se aglomeran decenas de parrillas y restaurantes especializados en carnes asadas. El Mercado del Puerto está ubicado en la Ciudad Vieja de Montevideo y es el sitio perfecto para probar la deliciosa carne uruguaya, acompañada de vino Tannat, que es la cepa típica del país.");
		attractionPuerto.setOutside(true);
		attractions.add(attractionPuerto);
		
		AttractionDTO attractionSolis = new AttractionDTO();
		attractionSolis.setAttraction("Teatro Solís");
		attractionSolis.setCity("Montevideo");
		attractionSolis.setDescription("El Teatro Solís es el teatro más antiguo del Uruguay. Está ubicado en el centro de Montevideo, en diagonal a la Plaza Independencia, la principal de la ciudad. El teatro fue inaugurado a mediados de 1856 y es uno de los edificios más impactantes de Montevideo. El teatro presenta obras y espectáculos musicales.");
		attractionSolis.setOutside(false);
		attractions.add(attractionSolis);
		
		AttractionDTO attractionRodo = new AttractionDTO();
		attractionRodo.setAttraction("Parque Rodó");
		attractionRodo.setCity("Montevideo");
		attractionRodo.setDescription("El Parque Rodó es un gran parque y espacio público entre los barrios de Parque Rodó y Punta Carretas. El parque toma su nombre de José Enrique Rodó, un importante escritor uruguayo. Dentro del parque hay un lago artificial, una zona de juegos infantiles, y un parque de atracciones mecánicas donde también se dispone de varios locales gastronómicos.");
		attractionRodo.setOutside(true);
		attractions.add(attractionRodo);
		
		AttractionDTO attractionTresCruces = new AttractionDTO();
		attractionTresCruces.setAttraction("Tres Cruces");
		attractionTresCruces.setCity("Montevideo");
		attractionTresCruces.setDescription("Tres Cruces es a la vez un centro comercial (shopping) y la terminal de buses de Montevideo. Se lo considera un importante punto de encuentro en la ciudad y un punto de partida desde donde puedes llegar a los principales sitios de interés de Montevideo.");
		attractionTresCruces.setOutside(false);
		attractions.add(attractionTresCruces);
		
		return attractions;
	}
	
	

}
