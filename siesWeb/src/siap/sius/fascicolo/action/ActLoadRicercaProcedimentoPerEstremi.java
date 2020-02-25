package siap.sius.fascicolo.action;


import java.util.Collection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadRicercaProcedimentoPerEstremi extends ActionSius implements ICostantiFascicoloSius
{
  public String processRequest() throws F3BException
  {

    // Imposta Contenuto.
    Option lOption = new Option();
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
    //lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimento(), 75);
    setRequestAttribute("contenuto", "" + lOption );

    setRequestAttribute("tipoAtto",lOption.toString() );
    Collection<DecodificheModel> options =new Vector <DecodificheModel>();
    options.addAll(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
    String filtroMinorenni = super.getFiltroMinorenni();
    if (filtroMinorenni.equalsIgnoreCase("true")) {
    	options.add(new DecodificheModel("TDSM", "TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
		options.add(new DecodificheModel("UDSM", "UFFICIO DI SORVEGLIANZA PER I MINORENNI", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
    }
    
    // Imposta Tipo Ufficio con Trattino.
    lOption = new Option (options);
    lOption.setSelected(strCodTipoUfficio);

    setRequestAttribute("tipoUfficioSIUSTrattino", "-" + lOption );
    // STUB 16/06/2004 Integrazione ricerca avanzata.
    setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio );
    String strDescrComune = getUfficioUtenteConnesso().getDescrComune();
    setRequestAttribute("ComuneUfficioConnesso", strDescrComune );

    // STUB 18/02/2005 Imposta l'elenco magistrati.
    String lCodUfficio = getCodUfficioUtenteConnesso();
    IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
    lOption = new Option( lMagCtrl.ExElencoCbxMagistratiByCodUfficio( lCodUfficio ) );
    lOption.setAddBlankItem(true);
    lOption.setValueBlankItem("Tutti");
    setRequestAttribute( "magistrato", "" + lOption );

    ricercaCancellerieAssegnatarie();
    filtroCollaboratore();
    
    return PG_LOAD_RICERCAPROCEDIMENTOPERESTREMI; //restituisce la jsp di VIEW
  }

  /**
   * Ricerca delle Cancellerie Assegnatarie definite per l'Ufficio.
   * Se trovate le Cancellerie vengono passate nella request.
   * @throws F3BException
   */
   private void ricercaCancellerieAssegnatarie() throws F3BException
   {
      // Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'uttente
      CancelleriaAssegnatariaModel lCancAssModel = new CancelleriaAssegnatariaModel();
      lCancAssModel.setCodUfficio(getCodUfficioUtenteConnesso());

      // Ricerca
      ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
      Vector <?>lElencoCancellerie = lCancAssCtrl.ExRicercaCancelleriaAssegnataria(lCancAssModel);
      if ( lElencoCancellerie != null && lElencoCancellerie.size() > 0 )
      {
         setRequestAttribute("cancellerie", lElencoCancellerie);
      }

   }
   /**
    * La funzione abilita il filtro nella Ricerca sul Collaboratore di Giustizia.
    * @throws F3BException
    */
   private void filtroCollaboratore() throws F3BException
   {
       // Si controlla se esiste l'interfaccia per la Gestione Collaboratore di Giustizia
   		ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
   		if (lCtrl.ExIsPackage())
   		{
            setRequestAttribute("collaboratore", "SI");
   		}       

   }


}
