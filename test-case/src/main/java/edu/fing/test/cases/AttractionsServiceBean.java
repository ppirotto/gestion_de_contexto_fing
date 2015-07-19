package edu.fing.test.cases;

import java.util.ArrayList;
import java.util.List;

import org.switchyard.component.bean.Service;

@Service(AttractionsService.class)
public class AttractionsServiceBean implements AttractionsService {
	
	private static final String TREINTA_Y_TRES = "TREINTA Y TRES";
	private static final String ROCHA = "ROCHA";
	private static final String COLONIA = "COLONIA";
	private static final String SORIANO = "SORIANO";
	private static final String CERRO_LARGO = "CERRO LARGO";
	private static final String RIVERA = "RIVERA";
	private static final String TACUAREMBO = "TACUAREMBO";
	private static final String RIO_NEGRO = "RIO NEGRO";
	private static final String PAYSANDU = "PAYSANDU";
	private static final String SALTO = "SALTO";
	private static final String ARTIGAS = "ARTIGAS";
	private static final String LAVALLEJA = "LAVALLEJA";
	private static final String DURAZNO = "DURAZNO";
	private static final String FLORES = "FLORES";
	private static final String FLORIDA = "FLORIDA";
	private static final String SAN_JOSE = "SAN JOSE";
	private static final String MALDONADO = "MALDONADO";
	private static final String CANELONES = "CANELONES";
	private static final String MONTEVIDEO = "MONTEVIDEO";
	
	private static final List<String> cities;
	static {
		cities = new ArrayList<String>();
		cities.add(MONTEVIDEO);
		cities.add(CANELONES);
		cities.add(MALDONADO);
		cities.add(SAN_JOSE);
		cities.add(FLORIDA);
		cities.add(FLORES);
		cities.add(DURAZNO);
		cities.add(LAVALLEJA);
		cities.add(ARTIGAS);
		cities.add(SALTO);
		cities.add(PAYSANDU);
		cities.add(RIO_NEGRO);
		cities.add(TACUAREMBO);
		cities.add(RIVERA);
		cities.add(CERRO_LARGO);
		cities.add(SORIANO);
		cities.add(COLONIA);
		cities.add(ROCHA);
		cities.add(TREINTA_Y_TRES);
		
	}

	@Override
	public List<AttractionDTO> getAttractions(String city) {
		
		if(city == null || !cities.contains(city.toUpperCase())){
			return this.getAttractions(cities);
			
		}else{
			List<String> citiesList = new ArrayList<String>();
			citiesList.add(city.toUpperCase());
			return this.getAttractions(citiesList);
		}
	}


	
	private List<AttractionDTO> getAttractions(List<String> cities){
		List<AttractionDTO> attractions = new ArrayList<AttractionDTO>();
		for (String city : cities) {
			switch (city) {
			case MONTEVIDEO:
				AttractionDTO attractionPuerto = new AttractionDTO();
				attractionPuerto.setAttraction("Mercado del Puerto");
				attractionPuerto.setCity("Montevideo");
				attractionPuerto.setDescription("Paseo gastronómico y cultural de Montevideo");
				attractionPuerto.setOutside(true);
				attractions.add(attractionPuerto);
				
				AttractionDTO attractionSolis = new AttractionDTO();
				attractionSolis.setAttraction("Teatro Solís");
				attractionSolis.setCity("Montevideo");
				attractionSolis.setDescription("Constituye el principal escenario artístico de Montevideo");
				attractionSolis.setOutside(false);
				attractions.add(attractionSolis);
				
				break;
				
			case COLONIA:
				AttractionDTO attractionSuspiros = new AttractionDTO();
				attractionSuspiros.setAttraction("Calle de los Suspiros");
				attractionSuspiros.setCity("Colonia");
				attractionSuspiros.setDescription("Peatonal más linda de todo el Uruguay");
				attractionSuspiros.setOutside(true);
				attractions.add(attractionSuspiros);
				
				break;
			case MALDONADO:
				AttractionDTO attractionPuertoPE = new AttractionDTO();
				attractionPuertoPE.setAttraction("Puerto de Punta del Este");
				attractionPuertoPE.setCity("Maldonado");
				attractionPuertoPE.setDescription("Puerto deportivo más importante del Uruguay");
				attractionPuertoPE.setOutside(true);
				attractions.add(attractionPuertoPE);

				
			}
			
		}
		return attractions;
	}


}
