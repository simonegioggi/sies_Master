package siap.siep.istruttoria.action;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.web.IWebConstants;

/**
 *
 * <p>Title: ActLoadInserisciCertificatoPenale</p>
 * <p>Description: Classe che permette di richiedere un certificato penale su NSC</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciCertificatoPenale extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {
     if( isSessionAttributeNullObj("fascicolo") )
     {
       String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
                      "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                      ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
                      "siap.siep.istruttoria.action.ActLoadInserisciCertificatoPenale";
       return lPage;
     }

     this.isFascicoloSiepDiCompetenza();
     this.isEventoNonValidato();
     
     // Se vengo da IstruttoriaCUMULO
     if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
     {
     	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
     }
     
     UtenteModel lUtenteModel = this.getUtenteConnesso();

     setRequestAttribute("utente", lUtenteModel );

     return PG_LOAD_RICHIESTA_CERTIFICATO_PENALE;
   }
   
}