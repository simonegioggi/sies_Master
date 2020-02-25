package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
 *
 * <p>Title: ActLoadInserisciAnagraficaCittadiniStranieri</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciAnagraficaCittadiniStranieri extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {

     if( isSessionAttributeNullObj("fascicolo") )
        {
          String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
               "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
               ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
               "siap.siep.istruttoria.action.ActLoadInserisciAnagraficaCittadiniStranieri";

               return lPage;
        }

     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     this.isFascicoloSiepDiCompetenza();
     this.isEventoNonValidato();

    /* if (lFascicoloModel.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
       throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento  N." +lFascicoloModel.getChiaveAnno()+"/"+ lFascicoloModel.getChiaveProgr() + " è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Capi di Imputazione" );

     if (lFascicoloModel.getFlagValidato().equalsIgnoreCase("N"))
        throw new SIEPException( SIEPException.USER_MESSAGE, "Il Procedimento N." +lFascicoloModel.getChiaveAnno()+"/"+ lFascicoloModel.getChiaveProgr() + " non è stato Validato. Impossibile inserire un ordine d'esecuzione!" );
    */
        // Riempimento  ComboBoX
        // 14/06/2010 Sostituzione Elenco Autorità Emittenti
     		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
        Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
        setRequestAttribute("autorita", "" + lOption );
        setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(),"dd/MM/yyyy"));


     return PG_LOAD_INSERISCI_ANAGRAFICA_CITTADINI_STRANIERI;
   }


}