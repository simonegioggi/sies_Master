package siap.siep.modulocumulo.action;


/**
* <p>Title: ActLoadInserisciTitoloCumulato</p>
* <p>Description: Classe Action per la load inserisci di TitoloCumulato</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Action per la load Inserisci o Modifica dei dati del Titolo Cumulato
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciTitoloCumulato extends ActionModuloCumulo implements ICostantiTitoloCumulato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException {

    //==========================================================================
    // Recupero l'istruttoria da passare alla form
    //==========================================================================
    super.getDatiIstruttoria();
    
    String lModalita = "";
    TitoloCumulatoModel lTitMod = null;
    if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_CUMULATO) && !"".equals(getRequestStringParameter(CAMPO_ID_TITOLO_CUMULATO))) 
    {
      // Sono in modifica
      BigDecimal lIdTitolo = getRequestBigDecimalParameter(CAMPO_ID_TITOLO_CUMULATO);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Sono in modifica, idTitolo = "+lIdTitolo);
      
      ITitoloCumulato lTitoloCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
      lTitMod = lTitoloCtrl.ExRicercaTitoloCumulatoById( lIdTitolo);
      
      // In caso di decreto (02) devo capire se trattasi di decreto penale o 
      // decreto della Sorveglianza. Testo il cod ufficio inserimento
      if ("02".equals (lTitMod.getCodTipoProvvedimento())) {
        String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
        if (!Arrays.asList(lUfficiSorv).contains(lTitMod.getCodTipoAutoritaEmittente())){
          // Rimappo il codice per poterlo gestire nella jsp
          lTitMod.setCodTipoProvvedimento("02bis"); // Decreto Penale
        }
      }
      
      
      
      // Ripulisco il trattino nel campo Sede
      if ("-".equals(lTitMod.getDescrLuogoProvvRif()))
        lTitMod.setDescrLuogoProvvRif(null);
      
      lModalita = "M";
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lTitMod = "+lTitMod);
      
      setRequestAttribute("titolocumulato", lTitMod);
    }
    else {
      lModalita = "I";
    }

    // Combo Tipo Provvedimento: n.b. 01 = Sentenza, 02 = Decreto
    // Per decreto si intende Decreto penale ma non esiste una voce relativa sulla CG
    // se si vuole visualizzare 
//    Option lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
//    lOptionTipoProvv.setFilter(new String[]{"-", "01", "02"}); 
//    setRequestAttribute("TipoProvv", "" + lOptionTipoProvv);
    

    // combo Tipo Registro Generale
    Option tipoRegGen = new Option( DecodificheManager.getInstance().getTipoRegistroGenerale(), "-");
    if (lTitMod!=null && lTitMod.getTipoRegGen()!=null)
      tipoRegGen.setSelected(lTitMod.getTipoRegGen());
    setRequestAttribute("tipoRegGen", "" + tipoRegGen );
    
  
    // Combo Autorita Emittente
    Option lOptionAutEmi = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    setRequestAttribute("autoritaEmi", "" + lOptionAutEmi );
    
    // Combo Autorita Emittente Sorveglianza
    Option lAutoSorvOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),"-");
    lAutoSorvOption.setFilter( new String[] {"-", "TDS", "UDS", "UDSM"} );
    setRequestAttribute("autoritaEmiSorv", "" + lAutoSorvOption );

    // Combo tipo rito
    Option lOptionTipoRito = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
    setRequestAttribute("tipoRito", "" + lOptionTipoRito );
    
    // Combo tipo provvedimento
    Option lOptionProvv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), "-");
    lOptionProvv.setFilter(new String[]{"-", "01", "53"});    
    setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv );
    
    // Tipo Sentenza (conf. / rif.)
    Option lOptionTipoSentenza = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");
    setRequestAttribute("tipoProvvedimentiRif", "" + lOptionTipoSentenza );
    
    // Combo Dispositivo
    Option lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );


    // Imposta la Modalità a Inserimento/Modifica.
    setRequestAttribute("modalita", lModalita);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_INSERISCITITOLOCUMULATO; 
  }
}