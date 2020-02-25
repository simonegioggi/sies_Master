package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sezione.util.SezioneUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciFascicoloSige</p>
* <p>Description: Classe Action per la load inserisci di FascicoloSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadInserisciFascicolo extends ActRicercaFSigePuntuale implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {
		FascicoloSiepModel fascicolo = null;

		// STUB: Fascicolo SIEP potrebbe non essere in sessione mentre il soggetto deve esserci !!!
		if (!isSessionAttributeNullObj("fascicolo")) {
			// ID Fascicolo SIEP.
			fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			setRequestAttribute("IDfascicoloSIEP", fascicolo.getIdFascicoloSiep().toString());
			// ID Soggetto.
			setRequestAttribute("IDSoggetto", fascicolo.getSogIdSoggetto().toString());
		} else
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo SIEP non presente in sessione ");

		// Si rimuovono eventuali dati preesistenti
		rimuoviFascicoloSigeEstesoDallaSessione();

		preparaRequest(fascicolo.getIdFascicoloSiep());

		// Modalità Inserimento da Fascicolo SIEP
		setRequestAttribute("modalita", "IF");

		return PG_LOAD_INSERISCIFASCICOLOSIGE;
	}

	/**
	 * La funzione prepara i dati da passare nella request necessari alla form di "Inserimento Fascicolo".
	 * I dati relativi al Fascicolo SIEP vengono trovati in base all' ID Fascicolo passato come parametro;
	 * se tale ID fosse null, tali dati saranno quelli di default.
	 * @param aIdFasSiep
	 * @throws Exception
	 */
	protected void preparaRequest(BigDecimal aIdFasSiep) throws Exception {
		Date dataFinePena = null;
		//Date dataAtto = null;
		String posGiuridica = "-";
		// Dettaglio Fascicolo SIEP
		DettaglioFascicoloModel lDettaglio = null;

		// Codice Tipo Ufficio dell'utente connesso.
		String lCodTipoUfficioConnesso = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Luogo di detenzione
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";

		// Imposta Tipo Atto SIGE.
		Option lTipoAttoOpt = new Option(DecodificheManager.getInstance().getTipoAttoSige());
		setRequestAttribute("tipoAtto", "" + lTipoAttoOpt);

		// Imposta Mittente Atto.
		Option lMittenteOpt = new Option(DecodificheManager.getInstance().getTipoRichiedenteSige(), 56);
		
		
		setRequestAttribute("mittenteAtto", "" + lMittenteOpt);

		// Imposta Tipo Giudizio.
		Option lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige());
		if (filterColl.contains(lCodTipoUfficioConnesso)) {
			// solo Rito Collegiale
			lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "C");
			lOptionGiudizio.setFilter("C");
		} else if (filterMono.contains(lCodTipoUfficioConnesso)) {
			// solo Rito Monocratico
			lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "M");
			lOptionGiudizio.setFilter("M");
		}
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

		// Imposta in dettagliofascicolo la Posizione Giuridica e la pena residua per il Fascicolo SIEP.
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		if (aIdFasSiep != null) {
			// Si leggono i dati relativi al Fascicolo SIEP
			lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aIdFasSiep);
			if (lDettaglio == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo SIEP non presente");

			lMittenteOpt = new Option(DecodificheManager.getInstance().getTipoRichiedenteSige(), 56);
			String codeEmittente=getRichiedenteByFascicoloSiep (lDettaglio.getFascicoloSiep().getCodTipoUfficio()); 
			
			if (codeEmittente != null)
			    lMittenteOpt.setSelected(codeEmittente);
			
			setRequestAttribute("mittenteAtto", "" + lMittenteOpt);

			String comuneUfficio=lDettaglio.getFascicoloSiep().getDescrComuneUfficio();
			setRequestAttribute("comuneUfficio", "" + comuneUfficio);
			
		}
		//Modifica del 11/11/2016
		// La data atto non viene più prevalorizzata (richiesta di Nunzia)
		//if(lDettaglio != null && lDettaglio.getFascicoloSiep().getDataArrivoAtto() != null ){
		//	dataAtto = lDettaglio.getFascicoloSiep().getDataIscrizione();
		//}

		if (lDettaglio != null && lDettaglio.getPenaResidua() != null && lDettaglio.getPenaResidua().getDataFine() != null && lDettaglio.getPenaResidua().getFlagValidato() != null && lDettaglio.getPenaResidua().getFlagValidato().equals("S"))
			dataFinePena = lDettaglio.getPenaResidua().getDataFine();

		// Elenco Posizioni Giuridiche da inserire nella combo
		ArrayList <DecodificheModel>lPosizioneGiuridica = getListaPosGiuridiche();
		// Preparazione della Combo List di Posizioni Giuridiche
		Option lPosGiuridicaOpt = null;

		if ((lDettaglio != null && lDettaglio.getPosizioneGiuridica() != null)) {
			posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
			lPosGiuridicaOpt = new Option(lPosizioneGiuridica, posGiuridica, 66);
		} else
			lPosGiuridicaOpt = new Option(lPosizioneGiuridica, 66);

		if (lDettaglio != null) {
			// Imposta il luogo detenzione.
			// Attenzione: se il luogo detenzione è per la causa attuale, viene valorizzato idLuogoDetenzione;
			// se il luogo detenzione è per altra causa, viene valorizzato idAltraCausa.
			if ((lDettaglio.getLuogoDetenzione() != null) && (lDettaglio.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)) {
				if (lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione() != null)
					luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione();
				else
					luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto() + " - " + lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune();

				if (lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione() != null)
					idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
			} else if ((lDettaglio.getAltraCausa() != null) && lDettaglio.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
				if (lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione() != null)
					luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione();
				else
					luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto() + " - " + lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrComune();

				if (lDettaglio.getAltraCausa().getIdAltraCausa() != null)
					idAltraCausa = lDettaglio.getAltraCausa().getIdAltraCausa().toString();

			}
		} // endif dettaglio

		// Elenco delle Sezioni previste per l'Ufficio
		Option lSezioniOpt = null;
		lSezioniOpt = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()));

		// Imposta l'elenco magistrati.
		Option lMagistratiOpt = null;
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		lMagistratiOpt = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(getCodUfficioUtenteConnesso()));

		setRequestAttribute("elencoSezioni", lSezioniOpt.toString());
		setRequestAttribute("magistrato", "" + lMagistratiOpt);
		setRequestAttribute("dataFinePena", dataFinePena);
		//setRequestAttribute("dataAtto", dataAtto);
		setRequestAttribute("posGiuridica", posGiuridica);
		setRequestAttribute("posizioneGiuridica", "" + lPosGiuridicaOpt);
		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);
	}

	/**
	 * Funzione per la preparazione dell'elenco delle Posizioni Giuridiche che popolerà la ComboList.
	 */
	@SuppressWarnings("unchecked")
	protected ArrayList <DecodificheModel> getListaPosGiuridiche() {
		// la Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection <DecodificheModel>lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection <DecodificheModel>lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		Collection <DecodificheModel>lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();

		ArrayList <DecodificheModel>lPosizioneGiuridica = new ArrayList<DecodificheModel>(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);

		return lPosizioneGiuridica;
	}
	
	private String getRichiedenteByFascicoloSiep (String codeSiep) {
		HashMap <String, String>mappa=new HashMap<String, String> ();
		mappa.put("PGCAP", "17");
		mappa.put("PM", "03");
		mappa.put("PMM", "16");
		return mappa.get(codeSiep);
	}

}
