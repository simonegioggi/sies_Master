package it.mig.sies.business;

import java.math.BigInteger;

import it.mig.sies.exception.ControlException;
import it.mig.sies.type.esecuzione_NEW.RequestData;
import it.mig.sies.util.ApplicationProperties;
import it.mig.sies.util.PropertyUtil;
import it.mig.sies.util.SiesDAO;

import org.apache.log4j.Logger;

/**
 * MEV 23010 - Servizio che effettua una serie di controlli
 * per eventualmente bloccare il trasferimento
 *  
 * @author Federico Paparoni
 * */

public class ControlService {
	
	private static final Logger logger=Logger.getLogger(ControlService.class);
	private RequestData requestData;
	
	public ControlService(RequestData requestData) {
		this.requestData=requestData;
	}

	public void execute() throws ControlException  {
		controlloTerzoCollegatoPresente();
	}
	
	/**
	 * Nei casi di prosecuzione/cessazione per sopravvenienza 
	 * nuovo titolo bloccare il trasferimento da SIUS 
	 * se manca il riferimento al nuovo titolo.
	 * @throws ControlException 
	 * */
	private void controlloTerzoCollegatoPresente() throws ControlException {
		logger.info("controlloTerzoCollegatoPresente ");
		if (requestData.getProvvedimento().getDatiTribunaleSorveglianza()!=null) {
			BigInteger idTerzoCollegato=requestData.getProvvedimento().getDatiTribunaleSorveglianza().getIdProvvedimentoRevocato();
			String codiciUnivoci=ApplicationProperties.getIstance().getProperty("blocco.terzo.collegato.nullo");
			if (codiciUnivoci.indexOf(requestData.getProvvedimento().getDatiTribunaleSorveglianza().getCodiceUnivocoProvvedimento())!=-1) {
				if(!PropertyUtil.isPresent(idTerzoCollegato)) {
					logger.error("Errore in controlloTerzoCollegatoPresente per codiceUnivoco["+requestData.getProvvedimento().getDatiTribunaleSorveglianza().getCodiceUnivocoProvvedimento()+"]");
					String responseCode="messaggio.errore.controllo.blocco.terzo.collegato";
					String responseMessage=ApplicationProperties.getIstance().getProperty(responseCode);
					throw new ControlException(responseMessage);
				}
			}
		}
		
	}
	
	/**
	 * Verifica la presenza della trasmissione verso NSC
	 * e quindi la possibilita' di cancellare il foglio complementare
	 * MEV 06
	 * */
	public boolean verificaPresenza(String idEvento) {
		logger.info("verificaPresenza");
		SiesDAO dao=SiesDAO.getIstance();
		boolean presente=dao.verificaPresenza(idEvento);
		return presente;
	}
}
