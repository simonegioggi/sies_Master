package siap.regesies.regesoggetto.action;



import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaRegeSoggetto</p>
* <p>Description: Classe Action per la modifica di RegeSoggetto</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/
public class ActModificaRegeSoggetto extends ActionRegeSiap implements ICostantiRegeSoggetto
{
/**
* Azione di Modifica del RegeSoggetto
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_FILE);
 		 // riempie il model
 		 RegeSoggettoModel lRegMod = new RegeSoggettoModel ();

 		 lRegMod.setIdFile(lId);
		// lRegMod.setFlagTipoSogg( getRequestStringParameter( CAMPO_FLAG_TIPO_SOGG) );
		 lRegMod.setCodFiscale( getRequestStringParameter( CAMPO_COD_FISCALE) );
		// lRegMod.setCodCs( getRequestStringParameter( CAMPO_COD_CS) );
		 lRegMod.setCodAfis( getRequestStringParameter( CAMPO_COD_AFIS) );
		 lRegMod.setCognome( getRequestStringParameter( CAMPO_COGNOME) );
		 lRegMod.setNome( getRequestStringParameter( CAMPO_NOME) );
		 lRegMod.setAnnoNascita( getRequestIntParameter( CAMPO_ANNO_DATA_NASCITA) );
		 lRegMod.setDataNascita( getRequestDateParameter( CAMPO_ANNO_DATA_NASCITA,CAMPO_MESE_DATA_NASCITA,CAMPO_GIORNO_DATA_NASCITA) );

     ComuneModel lComune = this.getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_COMUNE_NASCITA));

     lRegMod.setCodComuneNascita( lComune.getCodComune() );
		 lRegMod.setCodProvinciaNascita( lComune.getCodProvincia() );
		 lRegMod.setCodStatoNascita( getRequestStringParameter( CAMPO_COD_STATO_NASCITA) );
		 lRegMod.setDescComuneNascitaEstero( getRequestStringParameter( CAMPO_DESC_COMUNE_NASCITA_ESTERO) );
		 lRegMod.setNazionalita( getRequestStringParameter( CAMPO_NAZIONALITA) );
		 lRegMod.setPaternita( getRequestStringParameter( CAMPO_PATERNITA) );
		 lRegMod.setCognomeMadre( getRequestStringParameter( CAMPO_COGNOME_MADRE) );
		 lRegMod.setNomeMadre( getRequestStringParameter( CAMPO_NOME_MADRE) );
		 lRegMod.setSesso( getRequestStringParameter( CAMPO_SESSO) );
		 lRegMod.setAttoNascita( getRequestStringParameter( CAMPO_ATTO_NASCITA) );
		 lRegMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
		 //lRegMod.setDenoSogg( getRequestStringParameter( CAMPO_DENO_SOGG) );
		 //lRegMod.setRagiSogg( getRequestStringParameter( CAMPO_RAGI_SOGG) );
		 //lRegMod.setNomeRappLega( getRequestStringParameter( CAMPO_NOME_RAPP_LEGA) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lRegMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lRegMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lRegMod.setDataAggiornamento(DateUtils.getSysDate());

		 // chiama il controller
		 IRegeSoggetto lCtrl = RegeSiesLookupRemote.getRegeSoggettoRemote();
		 RegeSoggettoModel llRegModRet = lCtrl.ExModificaRegeSoggetto(lRegMod);

  	 setRequestAttribute("regesoggetto", llRegModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.regesies.regesoggetto.action.ActDettaglioRegeSoggetto&"+CAMPO_ID_FILE+"="+llRegModRet.getIdFile();
		 return lPage;
	 }



}