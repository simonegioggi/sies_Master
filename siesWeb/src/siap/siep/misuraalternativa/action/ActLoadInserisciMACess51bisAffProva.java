package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

/**
 * <p>
 * Title: ActLoadInserisciMACess51bisAffProva
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Cessazione Affidamento in Prova 51bis disposta dal MDS
 * DL 146/2013
 * </p>
 *
 * @since 03/2014
 * @version 1.0
 */

public class ActLoadInserisciMACess51bisAffProva extends ActCessazione51bis {

	/**
	 * n.b. L'inserimento viene effettuato in due fasi a causa della presenza del calcolo della pena
	 * (eventuale) Fase 1: si proviene dalla griglia della funzioni, si carica la form di registrazione
	 * dell'ordinanza di cessazione e eventuale calcolo della pena Fase 2: si proviene dalla Action di
	 * inserimento dell'ordinanza e calcolo pena e si carica la form per l'inserimento dei dati el
	 * provvedimento di esecuzione
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lEsito = null;
		if (isRequestParameterNullObj("isInsProvvSorv")) {
			lEsito = super.loadInserisciProvvedimetoSorveglianza();
		} else {
			lEsito = super.loadInserisciProvvedimetoEsecuzione();
		}
		;

		if (lEsito != null)
			return lEsito;

		// ==========================================================================
		//
		// ==========================================================================
		Collection<DecodificheModel> lCollOggettoMDS51bis = new Vector(
				DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAffPMDS51bis());
		// Iterator<DecodificheModel> itOggetti = lCollOggettoMDS51bis.iterator();
		// while (itOggetti.hasNext()){
		// DecodificheModel lDecode = itOggetti.next();
		// if ( lDecode.getCode().equals("2281")) lDecode.setDescription("Cessazione Misura Affidamento al
		// Servizio Sociale - per sopravvenienza nuovo titolo esecutivo");
		// else if (lDecode.getCode().equals("2282")) lDecode.setDescription("Cessazione Misura Affidamento
		// art. 47 quater o.p. - per sopravvenienza nuovo titolo esecutivo");
		// else if (lDecode.getCode().equals("2205")) lDecode.setDescription("Cessazione Misura Affidamento
		// Servizio Sociale da Tossicodipendente - alcooldipendente - per sopravvenienza nuovo titolo
		// esecutivo");
		// }
		// setRequestAttribute("oggettiMDS", lOggettoMDS51bis);

		// Option lOggettoMDS51bis = new Option(
		// DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAffPMDS51bis());
		Option lOggettoMDS51bis = new Option(lCollOggettoMDS51bis);
		setRequestAttribute("comboOggettiMDS", lOggettoMDS51bis.toString());

		setRequestAttribute("tipoCessazione", "AFFIDAMENTO");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CESSAZIONE_51BIS;
	}

}