package siap.siep.modulocumulo.action;


/**
* <p>Title: ActCancellaMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la cancellazione di MisuraSicurezzaCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActCancellaMisuraSicurezzaCumulo extends ActionSiap implements ICostantiMisuraSicurezzaCumulo
{
 /*****************************************************************************
  * Azione per la cancellazione dei dati. 
  * Effettua la cancellazione logica o fisica a seconda della provenienza del dato
  * da cancellare.
  * Se il dato è stato estratto (FLAG_STATO=E o M) effettua una cancellazione logica
  * impostando il FLAG_STATO a C e registrando il motivo della cancellazione nel 
  * campo note. 
  * Se il dato è stato inserito manualmente (FLAG_STATO=I) viene effettuata la 
  * cancellazione fisica
  * 
  * @return PG_MESSAGE di avvenuta cancellazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
    
    BigDecimal lIdTitolo = getRequestBigDecimalParameter ( ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
  

    //==========================================================================
    // Istanzia il model e recupero i dati per la cancellazione   
    //==========================================================================
    MisuraSicurezzaCumuloModel lMisMod = new MisuraSicurezzaCumuloModel();
    
    lMisMod.setIdMisuraSicurezzaCumulo  ( getRequestBigDecimalParameter ( CAMPO_ID_MISURA_SICUREZZA_CUMULO) );
    lMisMod.setFlagStato                ( getRequestStringParameter     ( CAMPO_FLAG_STATO));
    lMisMod.setMotivoModifica           ( getRequestStringParameter     ( CAMPO_MOTIVO_MODIFICA) );

    lMisMod.setCodOperatoreAggiornamento ( getCodUtenteConnesso());
    lMisMod.setDataAggiornamento         ( DateUtils.getSysDate());
    lMisMod.setCodUfficioAggiornamento   ( getCodUfficioUtenteConnesso());

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    lCtrl.ExCancellaMisuraSicurezzaCumulo(lMisMod);

    //=================================================================
    // Restituisce la pagina di con la lista delle Misure di Sicurezza 
    //=================================================================
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaMisuraSicurezzaCumulo";
    lPage += "&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;

    return lPage;

  }
}