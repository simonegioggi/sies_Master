package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioProvvAltraAutorita</p>
 * <p>Description: Classe Action per la load dettaglio di Provvedimento Altra Autorità</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioProvvAltraAutorita extends ActSIESDettaglioProvvedimento
                                                 //implements ICostantiArchiviazione,
                                                 //           ICostantiEvento
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//EVENTO
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    //IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    //EventoModel lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
    EventoModel lEveMod = lEveNotMod.getEvento();
      
    setRequestAttribute("evento", lEveMod);

//ARCHIVIAZIONE
    IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
    ArchiviazioneModel lArcMod = new ArchiviazioneModel();
    lArcMod = lCtrlArc.ExRicercaArchiviazioneByIdEvento(lIdEvento);
    setRequestAttribute("archiviazione", lArcMod);

//POSIZIONE GIURIDICA
    /* REWORK DETTAGLIO
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
    */

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lIdFascicolo);

    setRequestAttribute("posizioneluogoaltra", lPos);

// PENA RESIDUA
	/* REWORK DETTAGLIO
    IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel llPenMod = lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());
    */

    PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lIdFascicolo);

    setRequestAttribute("penaresidua", llPenMod);

//  MAGISTRATO
    MagistratoModel lMag = lEveNotMod.getMagistrato();

    setRequestAttribute("magistrato", lMag);

    return ICostantiArchiviazione.PG_LOAD_DETTAGLIO_PROVV_ALTRA_AUT;
  }
}