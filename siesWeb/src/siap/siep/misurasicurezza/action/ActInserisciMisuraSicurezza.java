package siap.siep.misurasicurezza.action;


/**
* <p>Title: ActInserisciMisuraSicurezza</p>
* <p>Description: Classe Action per l'inserimento di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza
{
	
	
/**
* Azione di Inserimento del MisuraSicurezza
* @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws Exception
    {
		// Nuova Misura di Sicurezza da inserire
		MisuraSicurezzaModel lMisMod = null;
	  
      if(!this.isRequestParameterNullObj("lTipoFun")) // paramentro passato solo nel caso di iscrizione guidata
       {
         this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFun"));
       }

       FascicoloSiepModel lFasc = (FascicoloSiepModel)this.getSessionAttribute("fascicolo");
       //Controllo Fascicolo non archiviato
//       lFasc.getCodMotivoArchiviazione().equals("01") /* STATO_FASCICOLO = ARCHIVIATO/DEFINITO */
       if(lFasc == null || "01".equals(lFasc.getCodStatoFascicolo()) )
       {
         throw new F3BException(F3BException.USER_MESSAGE, "Il Procedimento non esiste o risulta archiviato");
       }
       
       lMisMod = letturaDatiMisura(lFasc.getIdFascicoloSiep());

       //---Aggiungere in SIEPLookupRemote il metodo getMisuraSicurezzaRemote()

       IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
       MisuraSicurezzaModel llMisModRet = lCtrl.ExInserisciMisuraSicurezza(lMisMod);

        //Prepara la pagina di destinazione
       String lPage = "";
       lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza&"+CAMPO_ID_MISURA_SICUREZZA+"="+llMisModRet.getIdMisuraSicurezza().toString();
       return lPage;
    }
  
  protected MisuraSicurezzaModel letturaDatiMisura(BigDecimal aIdFascicoloSiep) throws Exception
  {
	  MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

      lMisMod.setCodNatura( getRequestStringParameter( CAMPO_COD_NATURA) );
      lMisMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) );
      lMisMod.setNumAnni( getRequestBigDecimalParameter( CAMPO_NUM_ANNI) );
      lMisMod.setNumMesi( getRequestBigDecimalParameter( CAMPO_NUM_MESI) );
      lMisMod.setNumGiorni( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI) );
      lMisMod.setAnnoReg38(new BigDecimal( DateUtils.getSysDate("yyyy")));
      //lMisMod.setNumReg38( getRequestBigDecimalParameter( CAMPO_NUM_REG_38) );

      lMisMod.setDataFineValidita(getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

      lMisMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lMisMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lMisMod.setDataInserimento(DateUtils.getSysDate());
      lMisMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
      
      //lMisMod.setEveIdEvento( getRequestBigDecimalParameter( CAMPO_EVE_ID_EVENTO) );
      return lMisMod;
  }



}