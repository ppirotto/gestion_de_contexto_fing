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
				
				break;
				
			case COLONIA:
				AttractionDTO attractionSuspiros = new AttractionDTO();
				attractionSuspiros.setAttraction("Calle de los Suspiros");
				attractionSuspiros.setCity("Colonia");
				attractionSuspiros.setDescription("La Calle de los Suspiros es una calle empedrada y peatonal del Barrio Histórico de Colonia del Sacramento, que conserva su atmósfera colonial. La calle está desnivelada y no tiene veredas.  La Calle de los Suspiros se ha convertido en una de las postales de Colonia, con sus antiguas señalizaciones, y sus casas del 1800 o incluso anteriores.");
				attractionSuspiros.setOutside(true);
				attractions.add(attractionSuspiros);
				
				AttractionDTO attractionBasilica = new AttractionDTO();
				attractionBasilica.setAttraction("Basílica del Santísimo Sacramento");
				attractionBasilica.setCity("Colonia");
				attractionBasilica.setDescription("La Basílica del Santísimo Sacramento es la iglesia más importante de Colonia del Sacramento. Fue construida en 1808, en el actual Casco Histórico, el mismo lugar donde en 1680 se había levantado un rancho de pidras que sirvió como primer iglesia de la ciudad.");
				attractionBasilica.setOutside(true);
				attractions.add(attractionBasilica);
				break;
			case MALDONADO:
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
				
			}
			
		}
		return attractions;
	}


}
