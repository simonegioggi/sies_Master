package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import f3b.util.F3BException;

public class ActDettaglioRichiestaMS extends ActionSiap
                                              implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
     FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

     //ricerca esistenza almeno una  misura alternativa
     MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
     IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
     lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByFascicoloOrdinanza(lFascMod.getIdFascicoloSiep());

//Controllo se è presente una misura alternativa con ultima data inserimento
     if(lMisAlMod==null || lMisAlMod.getIdMisuraAlternativa()==null)
     {
       throw new SIEPException(SIEPException.USER_MESSAGE, "Non è presente nessuna misura alternativa per questo fascicolo!");
     }
     else
     {
       //Sede Ufficio di Sorveglianza
      UfficioModel lUffEmiMod = new UfficioModel();
      IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
      lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlMod.getChiaveUfficioFascicoloSius());
      setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

      //String lCodMag = lMisAlMod.getCodMagistrato();
      String lCodMag = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO);

      MagistratoModel lMagSorvMod = new MagistratoModel();
      IMagistrato lCtrlMagSorv = SICOLookupRemote.getMagistratoRemote();
      lMagSorvMod = lCtrlMagSorv.ExRicercaMagistratoByCod(lCodMag);
      setRequestAttribute("magistratosorveglianza", lMagSorvMod);
     }
     setRequestAttribute("misuraalternativa",lMisAlMod);

     //Ricerca EventoNotifica

     BigDecimal lIdEvento = this.getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO );
     IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
     EventoNotificaModel lEveMod = new EventoNotificaModel();
     lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

     this.setRequestAttribute("eventonotifica", lEveMod);

     String lFlagEst = "N";

     if(lEveMod.getEvento().getCodMotivo().equals("0124")|| lEveMod.getEvento().getCodMotivo().equals("0125"))
     {
       lFlagEst = "S";
     }
     this.setRequestAttribute("varieopzioni", lFlagEst);


    return PG_DETTAGLIO_RICHIESTA_MS;
  }
}