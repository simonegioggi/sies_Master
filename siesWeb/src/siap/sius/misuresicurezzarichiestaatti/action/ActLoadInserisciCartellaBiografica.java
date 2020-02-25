package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
/**
 * <p>Title: ActLoadInserisciSospAttiRemDebIstPenitenziario </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.
 * La classe estende la classe ActLoadRichiestaSanzSostDocumentiIstruttori
 * per il riuso di parti comuni.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciCartellaBiografica extends ActionSiap
implements ICostantiRichiestaAtti,ICostantiMisureSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Metodo processRequest che prepara i dati necessari per la
   * composizione della form, e ritorna come parametro la relativa
   * JSP compresiva di path.
   * <p>
   * @return pagina JSP da caricare
   * @throws Exception propaga qualunque errore di eccezione.
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    gestioneRitorno();

    // Data Fascicolo SIUS
    Date lDataInserimento = ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getDataInserimento();
    String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
    setRequestAttribute("dataInsFS",lDataInserimentoString);// Imposta il valore in request.

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return PG_LOAD_CARTELLABIOGRAFICA; //restituisce la jsp di VIEW
  }
}