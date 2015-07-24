package edu.fing.test.cases;

import java.util.ArrayList;
import java.util.List;

import org.switchyard.component.bean.Service;

@Service(ColoniaAttractionsService.class)
public class ColoniaAttractionsServiceBean implements ColoniaAttractionsService {
	
	@Override
	public List<AttractionDTO> getAttractions() {
		List<AttractionDTO> attractions = new ArrayList<AttractionDTO>();
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
		attractionBasilica.setOutside(false);
		attractions.add(attractionBasilica);
		return attractions;
		
	}

}
