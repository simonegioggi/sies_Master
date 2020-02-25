package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

 /**
 * <p>
 * Title: ActLoadEmissioneDecreto
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di DepositoDecreto
 * </p>
 * L'azione presenta contenuto ed oggetti relativi al procedimento sceltoe e le combo-box atte a cambiare
 * questi ed a selezionare il tipo di decreto da emettere.
 *
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadEmissioneDecreto extends ActRicercaFSPuntuale implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   // Codice oggetto Procedimento (contenuto del Fascicolo)
   // Membro di classe perchè ereditato.
   String mCodOggettoProc = null;

	public String processRequest() throws Exception {

    String lRetPage = PG_LOAD_EMISSIONE_DECRETO; // pagina di view

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadEmissioneDecreto: inizio");

		setLinkRitorno();

    //  STUB : occorre verificare se può essere eliminato !
		if (isRequestParameterNullObj("ritorno")) {
    // Solo se provengo da menu
      super.processRequest();
    }

    String lCodOggetti  = new String();
    String lDescOggetti = new String();
    String lCodDettagli = new String();  //STUB 20/04/2004
    String lFascSospeso = "NO";
    
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
      throw new SIUSException(SIUSException.USER_MESSAGE,"fascicoloSiusGP non in sessione");

    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    // Preleva l'id di generale procedimento.
    BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
    if (lIdGenProc == null)
      throw new SIUSException(SIUSException.USER_MESSAGE,"Id Generale Procedimento assente");

   // Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
   // già emesso per il Fascicolo SIUS.
   // Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
   RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lIdGenProc);

   boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();

   if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

   // Viene effettuato il controllo sulla preesistenza di un'ordinanza di rimessione atti
   // già emessa per il Fascicolo SIUS.
   // Se esiste almeno un provvedimento di questo tipo l'utente viene avvertito prima di proseguire.
   boolean lEsistenzaSospensione = lRicerca.verificaEsistenzaSospensione();
   if (lEsistenzaSospensione) {
	   // Richiede se si vuoi proseguire nonostante una precedente sospensione.
	   // Lo stato del fascicolo sarebbe comunque impostato ad "Iscritto"
	   // return PG_WARNING;
	   lFascSospeso = "SI";
	}
   
   // Esegue il controlle dello stato del fascicolo SIEP, nel caso di fascicolo archiviato
   // il metodo ereditato inserisce in request il messaggio di conferma personalizzato
   // per il caso di emissione provvedimento.
   if ( super.checkFascicoloSIEPArchiviatoPerEmissioneProvvedimento() )
     return PG_WARNING; 
   
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("RicercaTenori");
    // Effettua la ricerca dei tenori eventualmente già presenti
    // questo per mostrare l'elenco degli oggetti selezionati.
    ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
    Vector <?>lTenori = lCtrl.ExRicercaTenoreByGenProc( lIdGenProc );

    // Preleva eventuali tenori, per ricavare codice oggetto e descrizione.
    // Questa istruzione è utile per riportare nella form gli oggetti archiviati.
		if (lTenori != null) {
      Iterator <?>itx = lTenori.iterator();
			while (itx.hasNext()) {
        TenoreModel lTenore = (TenoreModel)itx.next();
        lCodOggetti += lTenore.getCodOggettoTenore() + "|";
        lDescOggetti += lTenore.getDescrOggettoTenore() + "\n";
        // STUB 20/04/2004 Aggiunti i Codici dettaglio.
				if (lTenore.getCodDettaglioOggetto() != null
						&& lTenore.getCodDettaglioOggetto().length() > 1) {
          lCodDettagli += lTenore.getCodOggettoTenore() + lTenore.getCodDettaglioOggetto() + "|";
        }
      }
    }

    setRequestAttribute("codOggetti", lCodOggetti);
    setRequestAttribute("descOggetti", lDescOggetti);
    setRequestAttribute("codDettagli", lCodDettagli); // STUB 20/04/2004
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("codOggetti: -"+ lCodOggetti + "-");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("descOggetti: -"+ lDescOggetti + "-");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("codDettagli: -"+ lCodDettagli + "-");
    setRequestAttribute("fascSospeso", lFascSospeso);
    
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadEmissioneDecreto: oggetto procedimento");

    // Preleva il cod Oggetto procedimento per poi passarlo come contenuto
    mCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
    if (mCodOggettoProc == null)
      throw new SIUSException(SIUSException.USER_MESSAGE,"Oggetto Procedimento assente !");
    // Imposta Contenuto.
    setRequestAttribute("codContenuto", mCodOggettoProc );
    // 02/05/2006 la descrizione del contenuto va impostata senza suffisso.
    String lDescContenuto="";
    
    //MEV63 aggiunto controllo per distinzione ufficio minori
 	String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

 	// Imposta Contenuto.		
    if (mCodOggettoProc.indexOf("C") == 0) {
    	if ("TDS".equals(strCodTipoUfficio)) 
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoTDS(), mCodOggettoProc);
    	else
    		lDescContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(),mCodOggettoProc);
		// MEV_66: distinguo per ufficio minorile
		if ("TDSM".equals(getUfficioUtenteConnesso().getCodTipoUfficio()))
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), mCodOggettoProc);
		else
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoTDS(), mCodOggettoProc);
		// FINE MEV_66
    } else if (mCodOggettoProc.indexOf("U") == 0) {
    	if ("UDS".equals(strCodTipoUfficio)) 
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), mCodOggettoProc);
    	else
    		lDescContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(),mCodOggettoProc);
		// MEV_66: distinguo per ufficio minorile
		if ("UDSM".equals(getUfficioUtenteConnesso().getCodTipoUfficio()))
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), mCodOggettoProc);
		else
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), mCodOggettoProc);
		// FINE MEV_66
		} else
			lDescContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimento(), mCodOggettoProc);

    setRequestAttribute("contenuto", lDescContenuto );

    //  Ricerca del Magistrato Relatore
    IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    setRequestAttribute("magistratorelatore", lMagRel);

    // Ricerca avvocati assegnati al fascicolo
    IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl
				.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    setRequestAttribute("avvocato", lAvvocato);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadEmissioneDecreto: -> page: " + lRetPage);
    
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("ActLoadEmissioneDecreto: fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }

  // Generazione della lista di tipi decreti
	public List generaListaTipi() {

		List<DecodificheModel> lTipoDecreto = new ArrayList<DecodificheModel>(
				DecodificheManager.getInstance().getTipoDecreto());
		lTipoDecreto
				.add(new DecodificheModel("00", "Generazione Automatica", "-", "-", "-", "-", "-", "-", "-"));
    return lTipoDecreto;
  }

}