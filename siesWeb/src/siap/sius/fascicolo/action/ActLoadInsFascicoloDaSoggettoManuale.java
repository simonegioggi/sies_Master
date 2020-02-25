package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInsFascicoloDaSoggettoManuale extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {

    // Dettaglio Soggetto tramite Dettaglio HTML.
    setRequestAttribute("soggetto",(SoggettoModel)getSessionAttribute("soggetto"));

    // Imposta Tipo Atto.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAtto());
    setRequestAttribute("tipoAtto", "" + lOption );

    // Imposta Mittente Atto.
    lOption = new Option( DecodificheManager.getInstance().getMittenteAtto(), 36);
    setRequestAttribute("mittenteAtto", "" + lOption );

    // Imposta Contenuto.
    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    if (strCodTipoUfficio.equals("TDS"))
      lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoTDS(), 75);
    else if (strCodTipoUfficio.equals("UDS"))
      lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
    else if (strCodTipoUfficio.equals("TDSM"))
        lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), 75);
    else if (strCodTipoUfficio.equals("UDSM"))
        lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
    else
      lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimento(), 75);
    setRequestAttribute("contenuto", "" + lOption );

    // Imposta Posizione Giuridica.
    // STUB 26/05/2005 Variazione: lPosizioneGiuridica contiene le posizioni dell'esecuzione.
    //lOption = new Option( DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione() );
    lOption = new Option( DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione() );
    setRequestAttribute("posizioneGiuridica", "" + lOption );

    // Imposta l'elenco magistrati.
    String lCodUfficio = getCodUfficioUtenteConnesso();
    IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
    // Modifica del 18/11/2016 MEV_50
    //Vengono recuperati solo i Magistrati ancora in servizio
    //lOption = new Option( lMagCtrl.ExElencoCbxMagistratiByCodUfficio( lCodUfficio ) );
    lOption = new Option( lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio( lCodUfficio ) );
    setRequestAttribute( "magistrato", "" + lOption );

    setRequestAttribute("modalita", "IS");

    //Redirect per tornare indietro
    String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"="+this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
    setRequestAttribute("TornaQui", lTornaQui);

    return PG_LOAD_INSERISCIFASCICOLOSIUSMANUALE;
  }
}
