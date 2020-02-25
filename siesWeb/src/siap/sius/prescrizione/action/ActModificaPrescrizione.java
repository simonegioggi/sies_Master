package siap.sius.prescrizione.action;


/**
* <p>Title: ActModificaPrescrizione</p>
* <p>Description: Classe Action per la modifica di Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaPrescrizione extends ActionSiap implements ICostantiPrescrizione
{
/**
* Azione di Modifica del Prescrizione
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_PRESCRIZIONE);
 		 // riempie il model
 		 PrescrizioneModel lPreMod = new PrescrizioneModel ();

 		 lPreMod.setIdPrescrizione(new BigDecimal(lId));
		 lPreMod.setIdPrescrizione( getRequestBigDecimalParameter( CAMPO_ID_PRESCRIZIONE) );
		 lPreMod.setCodTipoPrescrizione( getRequestStringParameter( CAMPO_COD_TIPO_PRESCRIZIONE) );
		 lPreMod.setCodLuogoAffidamento( getRequestStringParameter( CAMPO_COD_LUOGO_AFFIDAMENTO) );
		 lPreMod.setCodUffMagistratoCompetente( getRequestStringParameter( CAMPO_COD_UFF_MAGISTRATO_COMPETENTE) );
		 lPreMod.setCodLuogoAutorizzato( getRequestStringParameter( CAMPO_COD_LUOGO_AUTORIZZATO) );
		 lPreMod.setIdCssaCompetente( getRequestBigDecimalParameter( CAMPO_ID_CSSA_COMPETENTE) );
		 lPreMod.setDescrMansioneLavorativa( getRequestStringParameter( CAMPO_DESCR_MANSIONE_LAVORATIVA) );
		 lPreMod.setDescrLuogoLavoro( getRequestStringParameter( CAMPO_DESCR_LUOGO_LAVORO) );
		 lPreMod.setCodProvinciaAutorizzata( getRequestStringParameter( CAMPO_COD_PROVINCIA_AUTORIZZATA) );
		 lPreMod.setOraUscitaAbitazione( getRequestStringParameter( CAMPO_ORA_USCITA_ABITAZIONE) );
		 lPreMod.setOraRientroAbitazione( getRequestStringParameter( CAMPO_ORA_RIENTRO_ABITAZIONE) );
		 lPreMod.setAutoritaCompetenteControllo( getRequestStringParameter( CAMPO_AUTORITA_COMPETENTE_CONTROLLO) );
		 lPreMod.setNumVolteControllo( getRequestBigDecimalParameter( CAMPO_NUM_VOLTE_CONTROLLO) );
		 lPreMod.setDescrAltraPrescrizione( getRequestStringParameter( CAMPO_DESCR_ALTRA_PRESCRIZIONE1) );
		 lPreMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lPreMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lPreMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		 lPreMod.setDataAggiornamento(DateUtils.getSysDate());
    // Le prescrizioni sono ora collegate all'evento Luigi 12-12-2003
    lPreMod.setEveIdEve(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
//		 lPreMod.setDepOpidDepositoOrdinanzaPc( getRequestBigDecimalParameter( CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC) );

		 // chiama il controller
		 IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		 PrescrizioneModel llPreModRet = lCtrl.ExModificaPrescrizione(lPreMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("prescrizione", llPreModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.prescrizione.action.ActLoadDettaglioPrescrizione&"+CAMPO_ID_PRESCRIZIONE+"="+llPreModRet.getIdPrescrizione().toString();
		 return lPage;
	 }



}