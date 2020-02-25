package siap.siep.statistiche.action;

import f3b.util.F3BProperties;
import siap.sico.web.ActionSiap;

/**
 * MEV_39: creata nuova classe per gestione Statistiche - Estrazione Dati
 * 
 * @author Gioggi
 */
public class ActLoadStatisticheEstrazioneDati extends ActionSiap implements ICostantiStatistiche {
	
	private String mFlagStatisticaIV;

	public String processRequest() throws Exception {

		this.mFlagStatisticaIV = "ON";
		try {
			this.mFlagStatisticaIV = F3BProperties.getProperty("mFlagStatistica.IV");
			if(this.mFlagStatisticaIV == null){
				this.mFlagStatisticaIV = "ON";
			}
		} catch (Exception e) {}
			
		F3BProperties.getProperty("mFlagStatistica.IV");
		// valore OFF oppure ON
		setRequestAttribute("flagStatisticaIV", mFlagStatisticaIV);
		
		// valore di ritorno
		return PG_LOAD_STATISTICHE_ESTRAZIONE_DATI;
	}

}