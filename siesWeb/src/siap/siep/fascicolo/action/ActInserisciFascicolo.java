package siap.siep.fascicolo.action;

/**
 * <p>Title: ActInserisciFascicolo</p>
 * <p>Description: Classe Azione di inserimento del Fascicolo
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 */

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciFascicolo extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }

    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    SentenzaModel lSent = (SentenzaModel)getSessionAttribute("sentenza");
    SoggettoModel lSogg = (SoggettoModel)getSessionAttribute("soggetto");

    FascicoloSiepModel lFasMod = new FascicoloSiepModel();
    
    if (getRequestStringParameter("assegnazione_manuale").equals("S")) 
    {
    	lFasMod.setChiaveAnno  (getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
    	lFasMod.setChiaveProgr (getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
    }
    else {
    	lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); //Anno corrente
    	//Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente
    }
    
    lFasMod.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); //Ufficio dell'operatore che inserisce
    
    lFasMod.setCodStatoFascicolo("02"); //Stato fascicolo settato ad aperto
    lFasMod.setCodMotivoArchiviazione("-"); //Motivo di archiviazione '-' per le join
    lFasMod.setCodTipoPosLibero("-"); //Motivo di archiviazione '-' per le join

    lFasMod.setDataIscrizione( getRequestDateParameter( CAMPO_ANNO_ISCRIZIONE_ATTI,CAMPO_MESE_ISCRIZIONE_ATTI,CAMPO_GIORNO_ISCRIZIONE_ATTI) );
    lFasMod.setDataIrrevocabilita( getRequestDateParameter( CAMPO_ANNO_DATA_IRREVOCABILITA,CAMPO_MESE_DATA_IRREVOCABILITA,CAMPO_GIORNO_DATA_IRREVOCABILITA) );

    lFasMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
    lFasMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
    lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione giuridica

    lFasMod.setCodOperatoreInserimento( lUtenteMod.getUserId() );
    lFasMod.setDataInserimento( DateUtils.getSysDate() );
    lFasMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

    lFasMod.setSogIdSoggetto(lSogg.getIdSoggetto());
    lFasMod.setSenIdSentenza(lSent.getIdSentenza());

    if (   getRequestStringParameter("assegnazione_manuale").equals("S") 
        && this.isRequestChecked("checkNumSpec")  
       )
    {
      lFasMod.setTipoProgressivo(0); // Nel caso di num Speciale il tipo deve essere 0
    } 
    else 
    {
      lFasMod.setTipoProgressivo(getRequestIntParameter("tipo"));
    }
    /* inizio modifica marzo 2010 */
    lFasMod.setDataArrivoAtto(getRequestDateParameter( CAMPO_ANNO_ARRIVO_ATTO,CAMPO_MESE_ARRIVO_ATTO,CAMPO_GIORNO_ARRIVO_ATTO) );
    /* fine modifica marzo 2010 */

    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFasMod = lCtrl.ExInserisciFascicoloSiep(lFasMod);
    String lStringNomeAzione = "&NomeAzione=siap.siep.fascicolo.action.ActInserisciFascicolo";

    //restituisce la jsp di VIEW
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + 
    		"=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+
    		CAMPO_ID_FASCICOLO_SIEP+"="+lFasMod.getIdFascicoloSiep().toString()+
    		lStringNomeAzione;
  }
}
