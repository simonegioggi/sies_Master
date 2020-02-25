package siap.sige.fascicolo.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaFascicoloSige
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della maschera di Ricerca Fascicolo SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 5.0
 */
@SuppressWarnings("unchecked")
public class ActLoadRicercaFascicoloSige extends ActionSiap implements ICostantiFascicoloSige
{
	public String processRequest() throws Exception
	{

		String lAction = new String();
		if (!isRequestParameterNullObj(CAMPO_AZIONE_CHIAMANTE)){
			lAction = getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE);
			setRequestAttribute("azionechiamante", lAction);
		}
		
		// Imposta Tipo Ufficio SIGE.
		String strCodUfficio = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strDescrComune = getUfficioUtenteConnesso().getDescrComune();

		Option lOption = new Option(DecodificheManager.getInstance().getTipiUfficioSigeAccorpato());
		lOption.setSelected(strCodTipoUfficio);
		
		Option lSezioniOpt = new Option(SezioneUtils.getElencoSezioniPerRicerca(getCodUfficioUtenteConnesso()));
		setRequestAttribute("sezioni", lSezioniOpt.toString());
		setRequestAttribute("tipoUfficioSige", lOption.toString());
		setRequestAttribute("UfficioConnesso", strCodUfficio);
		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);
		setRequestAttribute("ComuneUfficioConnesso", strDescrComune);

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		
		Vector <UfficioModel>lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_RICERCAFASCICOLOSIGE; // restituisce la jsp di VIEW
	}
}
