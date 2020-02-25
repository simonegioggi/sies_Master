package siap.sige.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.sezione.util.SezioneUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaFascicolo
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica di FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaFascicolo extends ActLoadDettaglioFascicolo {

	public String processRequest() throws Exception {

		super.processRequest();

		preparaRequest();

		// Modalità Modifica
		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCIFASCICOLOSIGE;
	}

	/**
	 * La funzione prepara i dati da passare nella request necessari alla form di "Inserimento Fascicolo". I
	 * dati relativi al Fascicolo SIEP vengono trovati in base all' ID Fascicolo passato come parametro; se
	 * tale ID fosse null, tali dati saranno quelli di default.
	 * 
	 * @param aIdFasSiep
	 * @throws Exception
	 */
	protected void preparaRequest() throws Exception {
		FascicoloSigeModel lFascicolo = mFascicoloEsteso.getFascicoloSige();
		RichiestaSigeModel lRichiesta = mFascicoloEsteso.getRichiestaSige();

		// Codice Tipo Ufficio dell'utente connesso.
		String lCodTipoUfficioConnesso = getUfficioUtenteConnesso().getCodTipoUfficio();

		Date dataFinePena = null;
		String posGiuridica = "-";

		// Luogo di detenzione
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";

		// Imposta Tipo Atto SIGE.
		Option lTipoAttoOpt = new Option(DecodificheManager.getInstance().getTipoAttoSige());

		lTipoAttoOpt = setSelezionato(lTipoAttoOpt, lRichiesta.getCodTipoAtto());
		setRequestAttribute("tipoAtto", "" + lTipoAttoOpt);

		// Imposta Mittente Atto.
		Option lMittenteOpt = new Option(DecodificheManager.getInstance().getTipoRichiedenteSige(), 56);
		lMittenteOpt = setSelezionato(lMittenteOpt, lRichiesta.getCodTipoRichiedente());
		setRequestAttribute("mittenteAtto", "" + lMittenteOpt);

		// Imposta Tipo Giudizio.
		// String lTipoGiudizio = (lFascicolo.getCodTipoGiudizio()==null ? "-" :
		// lFascicolo.getCodTipoGiudizio().trim() );
		// Option lOptionGiudizio = new Option( DecodificheManager.getInstance().getTipoGiudizioSige(),
		// lTipoGiudizio);
		// if (lCodTipoUfficioConnesso.compareTo("GIP")==0)
		// lOptionGiudizio.setFilter("M");
		// else if ("CASAP_CAS_CAP".indexOf(lCodTipoUfficioConnesso)>=0)
		// lOptionGiudizio.setFilter("C");
		// setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString() );
		// Carica Combo x TipoGiudizio.
		String lTipoGiudizio = (lFascicolo.getCodTipoGiudizio() == null ? "-"
				: lFascicolo.getCodTipoGiudizio().trim());
		Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficioConnesso);
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

		// Elenco Posizioni Giuridiche da inserire nella combo
		ArrayList lPosizioneGiuridica = getListaPosGiuridiche();

		// Preparazione della Combo List di Posizioni Giuridiche
		Option lPosGiuridicaOpt = null;
		lPosGiuridicaOpt = new Option(lPosizioneGiuridica, 66);
		lPosGiuridicaOpt = setSelezionato(lPosGiuridicaOpt, lFascicolo.getCodPosizioneGiuridica());
		posGiuridica = lFascicolo.getCodPosizioneGiuridica();

		// Elenco delle Sezioni previste per l'Ufficio
		Option lSezioniOpt = null;
		lSezioniOpt = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()));
		if (lFascicolo.getIdSezione() != null)
			lSezioniOpt = setSelezionato(lSezioniOpt, lFascicolo.getIdSezione().toString());

		setRequestAttribute("elencoSezioni", lSezioniOpt.toString());
		// setRequestAttribute( "magistrato", "" + lMagistratiOpt );
		setRequestAttribute("dataFinePena", dataFinePena);
		setRequestAttribute("posGiuridica", posGiuridica);
		setRequestAttribute("posizioneGiuridica", "" + lPosGiuridicaOpt);
		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);
	}

	/**
	 * Funzione per la preparazione dell'elenco delle Posizioni Giuridiche che popolerà la ComboList.
	 */
	protected ArrayList getListaPosGiuridiche() {
		// la Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();

		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);

		return lPosizioneGiuridica;
	}

	private Option setSelezionato(Option aOptin, String aSelected) {
		if (aSelected != null && aSelected.length() > 0)
			aOptin.setSelected(aSelected);
		else
			aOptin.setSelected("-");

		return aOptin;

	}

}