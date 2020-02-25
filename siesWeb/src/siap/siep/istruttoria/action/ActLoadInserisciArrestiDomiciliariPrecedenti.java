package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
 *
 * <p>Title: ActLoadInserisciArrestiDomiciliariPrecedenti</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciArrestiDomiciliariPrecedenti extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {

     if( isSessionAttributeNullObj("fascicolo") )
        {
          String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
               "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
               ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
               "siap.siep.istruttoria.action.ActLoadInserisciArrestiDomiciliariPrecedenti";

               return lPage;
        }

     this.isFascicoloSiepDiCompetenza();
     this.isEventoNonValidato();
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");

   /*  if (lFascicoloModel.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
       throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento  N." +lFascicoloModel.getChiaveAnno()+"/"+ lFascicoloModel.getChiaveProgr() + " è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Capi di Imputazione" );

     if (lFascicoloModel.getFlagValidato().equalsIgnoreCase("N"))
        throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento N." +lFascicoloModel.getChiaveAnno()+"/"+ lFascicoloModel.getChiaveProgr() + " non è stato Validato. Impossibile inserire un ordine d'esecuzione!" );
  */
     
     // Se vengo da IstruttoriaCUMULO
     if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
     {
     	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
     }

         // Riempimento  ComboBoX
         Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
         setRequestAttribute("autorita", "" + lOption );
         setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(),"dd/MM/yyyy"));


     return PG_LOAD_INSERISCI_ARRESTI_DOMICILIARI_PRECEDENTI;
   }


}