package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioNonLuogoAProvvedere</p>
 * <p>Description: Classe Action per la load dettaglio di Non Luogo a Provvedere</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioNonLuogoAProvvedere extends ActSIESDettaglioProvvedimento

{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//EVENTO NOTIFICA
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
    setRequestAttribute("eventonotifica", lEveNotMod);

//ARCHIVIAZIONE
    IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
    ArchiviazioneModel lArcMod = new ArchiviazioneModel();
    lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lIdEvento);
    setRequestAttribute("archiviazione", lArcMod);

// NOTIFICHE
    if(lEveNotMod != null && lEveNotMod.getNotifiche() != null)
    {
      NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
      for (int i=0; i < lNotifiche.length; i++)
      {
        // Cassellario
        if( lNotifiche[i].getAutEstIdAutoritaEsterna() != null &&
        	"E".equals(lNotifiche[i].getCodTipoNotifica()))
        {
          setRequestAttribute("autorita", lNotifiche[i]);
        }

        //istituto 
        if( lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
          && ! lNotifiche[i].getIstDetIdIstitutoDetenzione().equals("") )
        {
          setRequestAttribute("istituto", lNotifiche[i]);
        }
        
        // ufficio recupero crediti
        if( lNotifiche[i].getUffCodUfficio() != null )
        {
          setRequestAttribute("ufficio", lNotifiche[i]);
        }
        
        // altra autorita
        if( lNotifiche[i].getAutEstIdAutoritaEsterna() != null &&
        	"C".equals(lNotifiche[i].getCodTipoNotifica()))
        {
          setRequestAttribute("altraautorita", lNotifiche[i]);
        }       
      }
    }

//POSIZIONE GIURIDICA

    /* REWORK DETTAGLIO
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
    */

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lIdFascicolo);

    setRequestAttribute("posizioneluogoaltra", lPos);

// MAGISTRATO
    MagistratoModel lMag = lEveNotMod.getMagistrato();

    setRequestAttribute("magistrato", lMag);

// PENA RESIDUA

    /* REWORK DETTAGLIO
    IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel llPenMod = lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
    */

    PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lIdFascicolo);

    setRequestAttribute("penaresidua", llPenMod);

    return ICostantiArchiviazione.PG_LOAD_DETTAGLIO_NON_LUOGO_A_PROVVEDERE;
  }
}