package siap.sico.decodifiche.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoLicenzePeriodiModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * Action per il caricamento della popup con l'elenco dei provvedimenti della 
 * sorveglianza (decreti/ordinanze) di concessione dei "Reclamo Rimedi Risarcitori"
 * previsti dal DL 92/146:
 *
 * @author c.s
 */
public class ActLoadListaReclamoRimediRisarcitori extends ActionSiap
                                               implements ICostantiDecodifiche
{
  public String processRequest() throws Exception
  {
    BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

    ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
    
    Vector <EventoLicenzePeriodiModel> lListaEventiLicenze = null;
    lListaEventiLicenze =  lCtrlLib.ExRicercaReclamoRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(lIdFascicoloSiep);
    setRequestAttribute("listaEventiLicenze", lListaEventiLicenze);
    
    return PG_LISTA_RECLAMO_RIMEDI_RISARCITORI; 
  }
}
