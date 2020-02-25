package siap.siep.penaaccessoria.action;

import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadInserisciPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		return preparazioneForm();

	}

	/**
	 * La funzione prepara i dati necessari alla form di input. Separata dalla processRequest() per poter
	 * essere ereditata.
	 * 
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String preparazioneForm() throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getTipoPeneAccessorie());
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);
		lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoriaNoAltre", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
				DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });
		/* boolean lBool = ( */lColl.addAll(lColl2)/* ) */;
		lOption = new Option(lColl, "-");
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// Costruzione combo "Tenore Ordinanza"
		Collection lColTenoreOrdinanza = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TENORE_ORDINANZA_PA");
		lColTenoreOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTenoreOrdinanza = new Option(lColTenoreOrdinanza, "0", 50);
		setRequestAttribute("tenoreOrdinanza", "" + lOptionTenoreOrdinanza);

		// Costruzione combo "Fonte Reato" e "Sottonumerazione"
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		setRequestAttribute("modalita", "I");
		// setRequestAttribute("ChiaveFascicolo",getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

		return PG_LOAD_INSERISCIPENAACCESSORIA; // restituisce la jsp di VIEW
	}

}