package siap.siep.alias.action;

/**
* <p>Title: ActInserisciAlias</p>
* <p>Description: Classe Action per l'inserimento di Alias</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.alias.controller.AliasController;
import siap.siep.alias.model.AliasModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAlias extends ActionSiap implements ICostantiAlias
{
/**
* Azione di Inserimento del Alias
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
 		 AliasModel lAliMod = new AliasModel();

		 lAliMod.setIdAlias( getRequestBigDecimalParameter( CAMPO_ID_ALIAS) );
		 lAliMod.setCognome( getRequestStringParameter( CAMPO_COGNOME) );
		 lAliMod.setNome( getRequestStringParameter( CAMPO_NOME) );
		 lAliMod.setPaternita( getRequestStringParameter( CAMPO_PATERNITA) );
		 lAliMod.setCodFiscale( getRequestStringParameter( CAMPO_COD_FISCALE) );
		 lAliMod.setCodCs( getRequestStringParameter( CAMPO_COD_CS) );
		 lAliMod.setCodAfis( getRequestStringParameter( CAMPO_COD_AFIS) );
		 lAliMod.setAttoNascita( getRequestStringParameter( CAMPO_ATTO_NASCITA) );
		 lAliMod.setSesso( getRequestStringParameter( CAMPO_SESSO) );
		 lAliMod.setCodComuneNascita( getRequestStringParameter( CAMPO_COD_COMUNE_NASCITA) );
		 lAliMod.setCodProvinciaNascita( getRequestStringParameter( CAMPO_COD_PROVINCIA_NASCITA) );
		 lAliMod.setCodStatoNascita( getRequestStringParameter( CAMPO_COD_STATO_NASCITA) );
		 lAliMod.setDataNascita( getRequestDateParameter( CAMPO_ANNO_DATA_NASCITA,CAMPO_MESE_DATA_NASCITA,CAMPO_GIORNO_DATA_NASCITA) );
		 lAliMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lAliMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		 lAliMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lAliMod.setDataInserimento(DateUtils.getSysDate());
		 lAliMod.setDataInserimento(DateUtils.getSysDate());
		 lAliMod.setSogIdSoggetto( getRequestBigDecimalParameter( CAMPO_SOG_ID_SOGGETTO) );

		 //---Aggiungere in SICOLookupRemote il metodo getAliasRemote()
  	 //--CCCCC---- IAlias lCtrl = SIEPLookupRemote.getAliasRemote();

     AliasController lCtrl = new AliasController();
		 AliasModel llAliModRet = lCtrl.ExInserisciAlias(lAliMod);		 // setta la risposta nella request
		 setRequestAttribute("alias", llAliModRet);

     //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.alias.action.ActLoadDettaglioAlias&"+CAMPO_ID_ALIAS+"="+llAliModRet.getIdAlias().toString();
		 return lPage;
  }
}