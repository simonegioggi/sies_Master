package siap.sige.impugnazione.action;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;


/**
* <p>Title: ActAnnullaImpugnazioneSige</p>
* <p>Description: Classe Action per annullare l'Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActAnnullaImpugnazioneSige extends ActionSige
 implements ICostantiImpugnazioneSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lRectPage = null;
    if (isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE) || isRequestParameterNullObj(CAMPO_MOTIVO_ANNULLAMENTO) )
      throw new SIGEException(SIGEException.USER_MESSAGE, "Errore nei dati");

    IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    FascicoloSigeModel lFasMod = null; 
    if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)){
        lFasMod  = lFasCtrl.ExRicercaFascicoloSigeByKey(getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE));
    }
    
    gestioneRitorno();

    annulla(lFasMod);
    lRectPage = ritornoDopoCancellazione("Impugnazione annullata!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRectPage;
  }

  private void annulla(FascicoloSigeModel lFasMod) throws Exception
  {
    //Istanzio il Model e lo carico con quello posto nella request.
    ImpugnazioneSigeModel LimpMod = new ImpugnazioneSigeModel();

   //Dati da aggiornare in  Impugnazione
    LimpMod.setIdImpugnazioneSige(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
    LimpMod.setCodOperatoreAggiornamento(getCodUtenteConnesso()); //Codice dell'operatore che annulla
    LimpMod.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso()); //Codice dell'operatore che annulla
    LimpMod.setDataAggiornamento(DateUtils.getSysDate());
    LimpMod.setDataAnnullamento(LimpMod.getDataAggiornamento());
    LimpMod.setFlagAnnullamento("S");
    LimpMod.setMotivoAnnullamento(getRequestStringParameter(CAMPO_MOTIVO_ANNULLAMENTO));

   // Viene richiamato il Controller per eseguire l'Update
   IImpugnazioneSige lImpCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
   lImpCtrl.ExAnnullaImpugnazione(LimpMod, lFasMod);
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( "ID Impugnazione Aggiornato: " + LimpMod.getIdImpugnazioneSige() );
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( "ID Evento Aggiornato: " + getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO) );

  }

}