package siap.sige.tenore.action;

import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadDettaglio
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di TenoreSige
 * </p>
 * La classe apre la finestra per la Gestione degli Oggetti Sige. Non viene effettuata la ricerca degli
 * oggetti perchè quelli visualizzati dalla finestra saranno quelli presenti in session.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class ActLoadDettaglioOggetti extends ActionSige implements ICostantiTenoreSige {

	public String processRequest() throws Exception {

		setLinkRitorno();

		// Ricerca Sentenze assegnate al Fascicolo SIGE
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());

		setRequestAttribute("sentenze", lSentenze);
		setRequestAttribute("titolo", "Gestione Oggetti");

		// Carica la lista dei contenuti
		Option lOption = new Option(DecodificheManager.getInstance().getContenutiSigeTrattino());
		setRequestAttribute("contenuto", lOption.toString());

		// [EC] Lettura del codice contenuto
		// String codContenutoSelected = getRequestStringParameter(COD_CONTENUTO_SEL);

		// 20190519 [SG]: aggiunta gestione idUdienzaSige
		if (!isRequestParameterNullEmptyObj("idUdiSig"))
			setRequestAttribute("idUdiSig", getRequestStringParameter("idUdiSig"));

		// Passa alla request il parametro che esprime la possibilità di inserire/modificare/cancellare
		// Oggetti
		setModificabileOggettiAtto();

		return PG_LOAD_DETTAGLIOTENORISIGE;
	}

}