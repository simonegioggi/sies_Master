<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />
<jsp:useBean id="TitoloInCumulo" scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel" />
<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel" />
<jsp:useBean id="aIdComputo" scope="request" class="java.lang.String" />

<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficioEmittente" scope="request" class="java.lang.String" />
<jsp:useBean id="luogoUfficioEmittente" scope="request" class="java.lang.String" />

<%// Oggetti motivo computo%>
<jsp:useBean id="oggetto" scope="request" class="java.lang.String" />

<jsp:useBean id="oggettoDufficio" scope="request" class="java.lang.String" />
<jsp:useBean id="oggettoAltroUfficio" scope="request" class="java.lang.String" />

<%// Caricamento combo altra autorita %>
<jsp:useBean id="emessoDa" scope="request" class="java.util.Vector" />
<jsp:useBean id="tipoprovvedimento" scope="request" class="java.lang.String" />
<jsp:useBean id="oggettoProvvedimento" scope="request" class="java.util.ArrayList" />

<jsp:useBean id="autorita" scope="request" class="java.util.ArrayList" />

<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />
<jsp:useBean id="indEmessoDa" scope="request" class="java.lang.String" />
<jsp:useBean id="indAltraAutorita" scope="request" class="java.lang.String" />
<jsp:useBean id="indOggettoProvv" scope="request" class="java.lang.String" />

<% 
//==============================================================================
//form per l'inserimento del Provvedimento di Rideterminazione Pena 'Altro'
//menu: 'Rideterminazione Pena - Provvedimenti del PM - Altro'
//==============================================================================
ComputiCumuloModel aComputo = new ComputiCumuloModel();
Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();

// Impostazione indici per le combo in fase di modifica
int indexEmessoDa = 0; int indexCodMotivo = 0; int indexTipoAut = 0;

 if ( modalita.equals("M") )
{
  Iterator itxComputi = lListaComputi.iterator();
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    BigDecimal lIdCompDaModificare = new BigDecimal (aIdComputo);
    if (lComputo.getIdComputiCumulo().compareTo(lIdCompDaModificare)==0){
      aComputo = lComputo;
      break;
    }
  }
		
	if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroAltAut(aProvvedimento.getCodMotivo())) indexEmessoDa=1;
	else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroGE(aProvvedimento.getCodMotivo())) indexEmessoDa=2;
	else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroSorv(aProvvedimento.getCodMotivo())) indexEmessoDa=3;
	
	if (!StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(aProvvedimento.getCodMotivo())) {
		indexCodMotivo = Integer.parseInt(indOggettoProvv); 
		indexTipoAut = Integer.parseInt(indAltraAutorita);
	}
} 

int maxNumComputi = 4;

%>

<html>
<head>
<title>Gestione Provvedimento Rideterminazione Pena altro</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript">
  	var lMaxNumComputi = <%=maxNumComputi%>;
  
    //============================================================================
    // funzione per la verifica dei dati imputati in maschera
    //============================================================================
    function Verify()
    {
      // - Campo Oggetto obbligatori
      // - Quantum obbligatori (almeno uno): segno e quantita o importi
      // - Se provvedimento altra autorita obbligatori:
      //   * Tipo Provvedimento
      //   * Oggetto provvedimento
      //   * Autorita emittente tipo e sede
      var obbligatori;

      // Controllo le combo Oggetto (n.b. sono due una visibile, l'altra no)
      if (   document.f.TipoOrd[0].checked
          && document.getElementById('selectProvvedimentoDufficio').selectedIndex==0
         )
      {
        alert("Selezionare Oggetto");
        document.getElementById('selectProvvedimentoDufficio').focus();
        return false;
      }
      
      //===========================================
      // Controllo sui campi altra autorita
      //===========================================
      if (document.f.TipoOrd[1].checked)
      {        
        
        if (   document.f.TIPO_AUTORITA_AA.selectedIndex==0  )
        {
          alert("Selezionare Tipo Autorita Emittente");
          document.f.TIPO_AUTORITA_AA.focus();
          return false;
        }        
        
        // Data Ricezione Provv AA
        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value;

        var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di ricezione Provvedimento Altra Autorita non valida');
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.focus();
          return false;
        }
        
        // Data Emissione
        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value;

        var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione Provvedimento Altra Autorita non valida');
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.focus();
          return false;
        }
        
        if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value=="")
        {
          alert('Anno Provvedimento Altra Autorita obbligatorio');
          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.focus();
          return false;
        }
        else if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value<1950)
        {
          alert('Anno Provvedimento Altra Autorita non valido');
          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.focus();
          return false;
        }
        
        if (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.value=="")
        {
          alert('Numero Provvedimento Altra Autorita obbligatorio');
          document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.focus();
          return false;
        }

        // Tipo provvedimento
        if (document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Tipo Provvedimento");
          document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.focus();
          return false;
        }
        
        // Oggetto provvedimento
        if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Oggetto");
          document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.focus();
          return false;
        }
        
        // Sede 
        if (document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=="")
        {        
          alert("Selezionare Sede Autorita Emittente");
          document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.focus();
          return false;
        }
      }

  
      // Quantum di computo
      var numComputi = 0;
      
      // Per ogni rigo visibile verifico la coerenza dei dati. Se presente il segno
      // vanno specificati anche i quantum. Se presenti i quantum e obbligatorio
      // il segno. E' accettato il rigo vuoto anche se visibile.
      for (i=0; i<lMaxNumComputi; i++){
        idComputo = "annotazione_"+i;
        display = document.getElementById(idComputo).style.display;
        if (display=="block"){
          // Rigo visibile controllo la presenza e coerenza dei dati
          if (document.getElementById('PM_'+i).selectedIndex ==0)
          {
            // Segno assente verifico che NON ci siano i quantum
            if (   trimStringa(document.getElementById('ARec_'+i).value) != "" 
                || trimStringa(document.getElementById('MRec_'+i).value) != "" 
                || trimStringa(document.getElementById('GRec_'+i).value) != "" 
                || trimStringa(document.getElementById('Multa_'+i).value) != "" 
                || trimStringa(document.getElementById('Mul_dec_'+i).value) != "" 
                || trimStringa(document.getElementById('AArr_'+i).value) != ""
                || trimStringa(document.getElementById('MArr_'+i).value) != "" 
                || trimStringa(document.getElementById('GArr_'+i).value) != "" 
                || trimStringa(document.getElementById('Ammenda_'+i).value) != "" 
                || trimStringa(document.getElementById('Amm_dec_'+i).value) != "" 
                || trimStringa(document.getElementById('motivazioni_'+i).value) != ""
               )
            {
              alert ("Selezionare tra + e -");
              document.getElementById('PM_'+i).focus();
              return false;
            }
          }
          else {
            // Presente il segno verifico se presenti i quantum
            if (trimStringa(document.getElementById('ARec_'+i).value) == "" && 
            	trimStringa(document.getElementById('MRec_'+i).value) == "" &&
                trimStringa(document.getElementById('GRec_'+i).value) == "" && 
                trimStringa(document.getElementById('Multa_'+i).value) == "" && 
                trimStringa(document.getElementById('Mul_dec_'+i).value) == ""  &&
                trimStringa(document.getElementById('AArr_'+i).value) == "" &&
                trimStringa(document.getElementById('MArr_'+i).value) == ""  &&
                trimStringa(document.getElementById('GArr_'+i).value) == ""  &&
                trimStringa(document.getElementById('Ammenda_'+i).value) == ""  &&
                trimStringa(document.getElementById('Amm_dec_'+i).value) == ""
               )
            {
              alert ("Indicare i quantum o deselezionare il segno + e -");
              document.getElementById('ARec_'+i).focus();
              return false;
            }
            else{
              numComputi=numComputi+1;
            }
          }
        }
      }
      
      if (numComputi==0){
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");
        //alert ("Indicare le quantita da computare. Quantum e/o importi. ");
        return false;
      }
      
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }

	  //alert("riabilito radio TipoOrd ");
	  document.f.TipoOrd[0].disabled=false;
	  document.f.TipoOrd[1].disabled=false;
      
      return true;

    } <%  // end function verify() %>

    //==========================================================================
    // Visualizza nasconde le combo Oggetto. 
    // n.b. trattasi di due combo distinte una per i provvedimenti di 
    //      ufficio, una per i provvedimento altro ufficio
    //==========================================================================
    function radioBase()
    {
      if(document.f.TipoOrd[0].checked)
      {
        //alert("radiobase document.f.TipoOrd[0].checked ");
        // Visualizzo la combo con i codici motivo previsti per i provvedimenti d'ufficio
        document.getElementById('OggettoProvvedimentoDufficio').style.display = "block";
        document.getElementById('SelectProvvedimentoDufficio').disabled = false;
        if ('M'!='<%=modalita%>' )
            document.getElementById('SelectProvvedimentoDufficio').selectedIndex=0;
        	
        // Nascondo la div con i dati del 'provvedimento altra autorita'
        // Combo oggetti compresa
        document.getElementById('DivAltraAutorita').style.display = "none";
      }
      else 
      {
		if ('M'!='<%=modalita%>' )
            document.getElementById('SelectProvvedimentoAltroUfficio').selectedIndex=0;
       
        // Nascondo la Combo con gli oggetti "d'ufficio" e visualizzo la div con 
        // gli oggetti di altro ufficio
        document.getElementById('OggettoProvvedimentoDufficio').style.display = "none";
        document.getElementById('SelectProvvedimentoDufficio').disabled = true;
        
        // Visualizzo la div con i dati del 'provvedimento altra autorita'
        document.getElementById('DivAltraAutorita').style.display = "block";
      }
      
    }

	function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
	{
		var desktop;
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

    //=============================================================================
    // Funzioni per la gestione del caricamento dinamico delle combo altra autorita
    //=============================================================================
    // ** GESTIONE COMBO BOX **
    // Matrice di tante righe quanti i componenti di "Provvedimento emesso da"
    // e tante colonne quante sono le combo da gestire. In questo caso 2 combo
    // La combo Autorita emittente e la combo Oggetto 
    // 
    //                            Combo Autorita emittente | Combo Oggetto
    // -                        :            []            |      []
    // Altra Autorita           :            []            |      []
    // Giudice Esecuzione       :            []            |      []
    // Giudice di Sorveglianza  :            []            |      []
    
    var lNumProvvedimento=<%=emessoDa.size()%>;
    var lNumCombo = 2; // Combo Autorita emittente e Combo Oggetto

    var lMatrice=new Array(lNumProvvedimento)
    for (i=0; i<lNumProvvedimento; i++)
      lMatrice[i]=new Array(lNumCombo);

<%
      // Carico le combo Tipo Autorita Emittente per i 
      for(int i=0; i<emessoDa.size(); i++)
      {
        List lAut = (List)autorita.get(i);

        out.println("\n\n\tvar lAutorita"+i+"=new Array()\n");

        Iterator lIterAutorita = lAut.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterAutorita.hasNext())
        {
          lDec = (DecodificheModel) lIterAutorita.next();
          out.println("\tlAutorita"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCodiceAlternativo()+"\");");
          idx++;
        }
      }
      
      // Carico le Combo Oggetto 
      for(int i=0; i<emessoDa.size(); i++)
      {
        List lCont = (List)oggettoProvvedimento.get(i);

        out.println("\n\n\tvar lOggetto"+i+"=new Array()\n");

        Iterator lIterOggetto = lCont.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterOggetto.hasNext())
        {
          lDec = (DecodificheModel) lIterOggetto.next();
          out.println("\tlOggetto"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\");");
          idx++;
        }
      }
      
%>
    
    // La matrice contiene l'insieme delle opzioni selezionabili
    // strutturare in questo modo:
    // ci sono tante righe quante sono le opzioni della combo 'Provvedimento'
    // e tante colonne quante sono le combo da relazionare
    //var lAutorita1=new Array()
    //var lOggetto1=new Array()
    
    //var lAutorita2=new Array()
    //var lOggetto2=new Array()

    lMatrice[0][0] = lAutorita0;  // Contiene Combo Tipo Autorita
    lMatrice[0][1] = lOggetto0;   // Contiene Combo Codici Motivo

    // Altra Autorita
    lMatrice[1][0] = lAutorita1;  // Contiene Combo Tipo Autorita
    lMatrice[1][1] = lOggetto1;   // Contiene Combo Codici Motivo
    
    // Giudice Esecuzione
    lMatrice[2][0] = lAutorita2;  // Contiene Combo Tipo Autorita
    lMatrice[2][1] = lOggetto2;   // Contiene Combo Codici Motivo
    
    // Giudice Sorveglianza
    lMatrice[3][0] = lAutorita3;  // Contiene Combo Tipo Autorita
    lMatrice[3][1] = lOggetto3;   // Contiene Combo Codici Motivo

    function initCombo()
    {
      //alert("initCombo");
      <% int lIndex = 0; 
      	if ("M".equals(modalita) && 
      	   (!StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(aProvvedimento.getCodMotivo() )) ) {
      		lIndex= 1;
      	}
      %>
      document.f.TipoOrd[<%=lIndex%>].checked=true;
	  
      caricamento();
    }
           
    function caricamento()
    {
      var idxSel = document.f.TIPO_AUTORITA_AA.options.selectedIndex;

      loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA, idxSel, 0); // tipo autorita emittente
      loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA, idxSel, 1);            // CAMPO_COD_OGGETTO_DEFINIZIONE
    }

    //==========================================================================
    // Effettua il caricamento della combo 'campo' con i dati filtrati per
    // campo = oggetto combo da caricare
    // riga  = 
    //==========================================================================
    function loadCombo(campo, riga, colonna)
    {
      // ripulisce la combo
      for (m=campo.options.length-1;m>=0;m--)
        campo.options[m]=null;
      
      // Carica la combo
      for (i=0;i<lMatrice[riga][colonna].length;i++)
      {
        campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value);
      }

      //campo.options[1].selected=true;
    }

    function initPage(){
      initCombo();
      radioBase();
	
	  // Impostazione delle combo in modifica.
      <% if ("M".equals(modalita) ) { %>
      
        	if(document.f.TipoOrd[1].checked)
        	{
           		document.getElementById('SelectProvvedimentoAltroUfficio').selectedIndex=<%=indexEmessoDa%>;
        	}

  			//alert("disabilito radio TipoOrd ");
			document.f.TipoOrd[0].disabled=true;
			document.f.TipoOrd[1].disabled=true;
        	
			// Valorizzazione delle combo in COD_OGGETTO e AUTORITA_EMITTENTE
			var idxSel = document.f.TIPO_AUTORITA_AA.options.selectedIndex;

		    loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA, idxSel, 0); // tipo autorita emittente
       		document.getElementById('selectAutoritaEmittente').selectedIndex=<%=indexTipoAut%>;

		    loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA, idxSel, 1);            // CAMPO_COD_OGGETTO_DEFINIZIONE
       		document.getElementById('selectOggettoProcedimento').selectedIndex=<%=indexCodMotivo%>;

	  <% } %>
    }
    
    //==========================================================
    // Visualizza una nuova riga per l'inserimento dei quantum
    //==========================================================
    function addQuantum(){
      for (i=0; i<lMaxNumComputi; i++){
        idComputo = "annotazione_"+i;
        display = document.getElementById(idComputo).style.display;
        if (display=="none"){
          Rigo = i;
          document.getElementById('PM_'+Rigo).disabled = false ;
          // Reclusione
          document.getElementById('ARec_'+Rigo).disabled = false ;
          document.getElementById('MRec_'+Rigo).disabled = false ;
          document.getElementById('GRec_'+Rigo).disabled = false ;
          document.getElementById('Multa_'+Rigo).disabled = false ;
          document.getElementById('Mul_dec_'+Rigo).disabled = false ;
    
          //Arresti
          document.getElementById('AArr_'+Rigo).disabled = false ;
          document.getElementById('MArr_'+Rigo).disabled = false ;
          document.getElementById('GArr_'+Rigo).disabled = false ;
          document.getElementById('Ammenda_'+Rigo).disabled = false ;
          document.getElementById('Amm_dec_'+Rigo).disabled = false ;
    
          // Motivazioni
          document.getElementById('motivazioni_'+Rigo).disabled = false ;
          
          strTdCancella = '<a href="Javascript:cancellaComputo(\''+Rigo+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';

          document.getElementById('tdCancella_'+Rigo).innerHTML = strTdCancella;
          document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = '<br>';
          
          // Visualizzo
          document.getElementById(idComputo).style.display = "block";
          break;
        }
      }
    }
    
    function cancellaComputo(Rigo){
      
      //===========================================================
      // Cancello il contenuto delle celle
      //===========================================================
      document.getElementById('PM_'+Rigo).selectedIndex = 0 ;
      // Reclusione
      document.getElementById('ARec_'+Rigo).value = "" ;
      document.getElementById('MRec_'+Rigo).value = "" ;
      document.getElementById('GRec_'+Rigo).value = "" ;
      document.getElementById('Multa_'+Rigo).value = "" ;
      document.getElementById('Mul_dec_'+Rigo).value = "" ;

      //Arresti
      document.getElementById('AArr_'+Rigo).value = "" ;
      document.getElementById('MArr_'+Rigo).value = "" ;
      document.getElementById('GArr_'+Rigo).value = "" ;
      document.getElementById('Ammenda_'+Rigo).value = "" ;
      document.getElementById('Amm_dec_'+Rigo).value = "" ;
      
      // Motivazioni
      document.getElementById('motivazioni_'+Rigo).value = "" ;
      
      
      //===========================================================
      // Disabilito le celle in modo che non venga fatta la submit
      //===========================================================
      document.getElementById('PM_'+Rigo).disabled = true ;
      // Reclusione
      document.getElementById('ARec_'+Rigo).disabled = true ;
      document.getElementById('MRec_'+Rigo).disabled = true ;
      document.getElementById('GRec_'+Rigo).disabled = true ;
      document.getElementById('Multa_'+Rigo).disabled = true ;
      document.getElementById('Mul_dec_'+Rigo).disabled = true ;

      //Arresti
      document.getElementById('AArr_'+Rigo).disabled = true ;
      document.getElementById('MArr_'+Rigo).disabled = true ;
      document.getElementById('GArr_'+Rigo).disabled = true ;
      document.getElementById('Ammenda_'+Rigo).disabled = true ;
      document.getElementById('Amm_dec_'+Rigo).disabled = true ;

      // Motivazioni
      document.getElementById('motivazioni_'+Rigo).disabled = true ;

      //===================
      // Nascondo la riga
      //===================
      document.getElementById('annotazione_'+Rigo).style.display = "none";
      
      if (Rigo>1){
        strTdCancella = '<a href="Javascript:cancellaComputo(\''+(Rigo-1)+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';

        document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = strTdCancella;
        document.getElementById('tdCancella_'+(Rigo)).innerHTML = '<br>';
      }
    }
    
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste
    //==========================================================================
    function tornaIndietro(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }
    
</script>

</head>

<body class="corpo" onLoad="initPage();">

	<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
		<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>"
			value="siap.siep.modulocumulo.action.ActInserisciRidetPenaPMAltroCumulo">
		<input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>">

		<input type="HIDDEN" name="modalita" value="<%=modalita%>"> 
		<input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
		<input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
		<input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
		<input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_FLAG_STATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">
		
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"> <img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0>
			</a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<% if( modalita.equals("I") ) { %> <font class="campo">Inserimento annotazione Rideterminazione Pena Altro &nbsp;</font> <% } 
				else if( modalita.equals("M") || modalita.equals("NP") ) { %> <font class="campo">Modifica annotazione Rideterminazione Pena Altro
					&nbsp;</font> <%}%></td>
			<td class="LBG">
		        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaRidetPenaPMAltroCumulo')">
		          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
		        </a>
			</td>
		</tr>
	</table>

	<br>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />

	<br>
	<table align="center" width="95%" border="0" cellspacing="1"
		cellpadding="1">
		<tr>
			<td><jsp:include
					page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp" />
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td><jsp:include
					page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp" />
			</td>
		</tr>
	</table>
	<br>


<%
//==============================================================================
// Sezione con i dati del provvedimento
//==============================================================================
%>

		<table width="95%">
			<tr>
				<td class="Titolo" colspan="100%">Rideterminazione della pena</td>
			</tr>
			<tr>
				<td class="l" colspan="100%">
					<input type="radio" name="TipoOrd" value="dufficio" checked onClick="javascript:radioBase();">D'ufficio &nbsp;&nbsp; 
					<input type="radio" name="TipoOrd" value="altroUfficio" onClick="javascript:radioBase();">In esecuzione di provvedimento altro ufficio &nbsp;&nbsp;
				</td>
			</tr>
			<tr id="OggettoProvvedimentoDufficio" style="display: block">
				<td class="l" width="15%">Oggetto <font class="ob">(*)</font>
					:&nbsp;
				</td>
				<td class="l">
					<select name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" id="selectProvvedimentoDufficio">
						<%=oggettoDufficio%>
				</select>
			</td>
		  	<tr>
		    	<td class="l">Data Emissione</td>
		    	<td class="L" >
		      	<input title = "Giorno Data Emissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>
					value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(DateUtils.getDateToString(aProvvedimento.getDataEmissione(), "dd" )):StringUtils.toStringJSP(DateUtils.getSysDate("dd"))%>" > 
		      	-
		      	<input title = "Mese Data Emissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>
					value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(DateUtils.getDateToString(aProvvedimento.getDataEmissione(), "MM" )):StringUtils.toStringJSP(DateUtils.getSysDate("MM"))%>" > 
		      	-
		      	<input title = "Anno Data Emissione" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>
					value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(DateUtils.getDateToString(aProvvedimento.getDataEmissione(), "yyyy" )):StringUtils.toStringJSP(DateUtils.getSysDate("yyyy"))%>" > 
		    	</td>
		  	</tr>
			<tr>
				<td class="l">Note :&nbsp;</td>
				<td class="l" colspan="4">
					<textarea cols="100" rows="2"
						name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>"> <%= ("M".equals(modalita)&&(aProvvedimento.getNote()!=null))?StringUtils.toStringJSP(aProvvedimento.getNote().trim(), "" ) :"" %>
					</textarea>
				</td>
			</tr>
		</table>

		<%
//==============================================================================
// Div per la visualizzazione della sezione con gli estremi del Provvedimento
// Altra Autorita
//==============================================================================
%>
		<div id="DivAltraAutorita" style="display: none">
			<table width="100%">
				<tr>
					<td colspan=4 class="titolo">Dati Provvedimento Altra Autorita</td>
				</tr>
				<tr>
					<td class="l" width="20%">Provvedimento emesso da<font class=ob>(*)</font></td>
					<td class="l" colspan="3">
						<select Title="Provvedimento" name="TIPO_AUTORITA_AA" id="selectProvvedimentoAltroUfficio" onChange="caricamento();">
							<% Iterator lIter = emessoDa.iterator();
							while(lIter.hasNext())
							{
								DecodificheModel lDecMod = (DecodificheModel)lIter.next();
							%>
								<option value="<%=lDecMod.getCode()%>" /><%=lDecMod.getDescription()%>
					 	 <% } %>
						</select>
					</td>
				</tr>
				<tr>
					<td class="l">Data ricezione provvedimento</td>
					<td class="l" colspan="3">
						<input type="text" Title="Giorno Ricezione" maxlength="2" size="2" 
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataRicezioneProvv(), "dd" ):DateUtils.getSysDate("dd")%>" 
							name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA" 
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> / 
						<input type="text" Title="Mese Ricezione" maxlength="2" size="2" 
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataRicezioneProvv(), "MM" ):DateUtils.getSysDate("MM")%>" 
							name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA"
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> / 
						<input type="text" Title="Anno Ricezione" maxlength="4" size="4" 
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataRicezioneProvv(), "yyyy" ):DateUtils.getSysDate("yyyy")%>" 
							name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA"
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					</td>
				</tr>
				<tr>
					<td class="l">Data emissione provvedimento</td>
					<td class="l">
						<input type="text" Title="Giorno Emissione provvedimento" maxlength="2" size="2"
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataEmissioneProvv(), "dd" ):DateUtils.getSysDate("dd")%>" 
							name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA"
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> / 
						<input type="text" Title="Mese Emissione provvedimento" maxlength="2" size="2" 
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataEmissioneProvv(), "MM" ):DateUtils.getSysDate("MM")%>" 
							name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA"
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> / 
						<input type="text" Title="Anno Emissione provvedimento" maxlength="4" size="4"
							value="<%= ("M".equals(modalita))?DateUtils.getDateToString(lListaComputi.get(0).getDataEmissioneProvv(), "yyyy" ):DateUtils.getSysDate("yyyy")%>" 
							name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA"
							onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					</td>
					<td class="l">Anno / Numero Provvedimento</td>
					<td class="l">
						<input type="text" Title="Anno Provvedimento" size="4" maxlength="4" 
							value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(lListaComputi.get(0).getAnnoProvv() ):"" %>" 
							name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA" 
							onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / 
						<input type="text" Title="Numero Provvedimento" size="6" maxlength="6" 
							value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(lListaComputi.get(0).getProgrProvv() ):"" %>" 
							name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA"
							onkeypress="return TicTabNumField(this,event)">
					</td>
				</tr>

				<tr>
					<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
					<td class="l" colspan=3><select Title="Tipo Provvedimento"
						name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA">
							<%=tipoprovvedimento%>
					</select></td>
				</tr>

				<tr>
					<td class="l">Oggetto Provvedimento <font class=ob>(*)</font></td>
					<td class="l" colspan=3><select Title="Oggetto Definzione" id="selectOggettoProcedimento"
						class="small" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA">
					</select></td>
				</tr>

				<tr>
					<td class="l">Autorita Emittente <font class="ob">(*)</font></td>
					<td class="l"><select Title="Autorita Emittente" id="selectAutoritaEmittente"
						name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA">
					</select></td>

					<td class="l" colspan="2">Sede &nbsp; <input
						title="Sede Autorita" type="text"
						name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA"
						value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(lListaComputi.get(0).getDescluogoUfficioEmittenteProvv() ):"" %>" 
						maxlength="35" size="35"> <a
						href="Javascript:ListaComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA',document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA[document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex].value);">
							<img src="/images/filefolder.gif" border="0">
					</a>
					</td>
				</tr>
			</table>
		</div>

		<%
//==============================================================================
// Sezione per specificare i quantum di rideterminazione
// n.b. è possibile inserire piu quantum
//==============================================================================
%>
		<table width="95%" border="0">
			<% String flagPiu =""; String flagMeno = "";
			
			   for (int i=0;i<maxNumComputi;i++ ) {
				   
				flagPiu=""; flagMeno="";
	
	      		if (lListaComputi!=null  &&  i<lListaComputi.size()) 
	      		{ 
	    	  		aComputo = lListaComputi.get(i);
					if ("M".equals(modalita) && ("+".equals(aComputo.getFlagPiuMeno() )) && aComputo.getIdComputiCumulo()!=null  )
						flagPiu = "selected";
					if ("M".equals(modalita) && ("-".equals(aComputo.getFlagPiuMeno() )) && aComputo.getIdComputiCumulo()!=null  )
						flagMeno = "selected";
			%>			    	  		
					<tr style="display: block" id="annotazione_<%=i%>">
			 <% } else { 
	    	  		aComputo = new ComputiCumuloModel();
	    	  		if (i==0) { %>
						<tr style="display: block" id="annotazione_<%=i%>">
				<% } else { %>
						<tr style="display: none" id="annotazione_<%=i%>">
				<% } 
	    	   } %>

				<td>
					<table width="100%">
						<tr>
							<td colspan="100%"><hr width="100%"></td>
							<td class="c"> 
								<input type="hidden" name="IdComputiCumulo_<%=i%>" id="IdComputiCumulo_<%=i%>" 
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) : null %>">
							</td>
						</tr>
						<tr>
							<td valign="middle" class="c" rowspan="3">+/- <font
								class="ob">(*)</font><br> 
								<select name="PM_<%=i%>"
									id="PM_<%=i%>">
									<option value=""></option>
									<option value="+" <%=flagPiu%> >+</option>
									<option value="-" <%=flagMeno%> >-</option>
								</select>
							</td>
							<td class="titolo" colspan="2">Reclusione</td>
							<td width="25">&nbsp;</td>
							<td class="titolo" colspan="2">Arresto</td>
							<% if (i==0) { %>
								<td valign="middle" class="c" rowspan="3" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
							<% } else { %>
								<td valign="middle" class="c" rowspan="3" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
							<% } %>
						</tr>
						<tr>
							<td class="c">
								<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp; 
								<font class="label">Giorni</font>
								<br> 
								<input type="text" name="ARec_<%=i%>" id="ARec_<%=i%>" maxlength="2" size="2"
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumAnniReclusione()):""%>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp; 
								<input type="text" name="MRec_<%=i%>" id="MRec_<%=i%>" maxlength="2" size="2" 
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumMesiReclusione()):"" %>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp; 
								<input type="text" name="GRec_<%=i%>" id="GRec_<%=i%>" maxlength="4" size="4" 
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumGiorniReclusione()):"" %>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
							</td>
							<td class="c">
								<font class="label">Multa</font>
								<br> 
								<input style="align: right" type="text" name="Multa_<%=i%>" id="Multa_<%=i%>" maxlength="8" size="6" 
									value="<%= ("M".equals(modalita))?StringUtils.getParteIntera(aComputo.getImportoMulta()):""%>"
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> , 
								<input style="align: right" type="text" name="Mul_dec_<%=i%>" id="Mul_dec_<%=i%>" maxlength="2" size="2" 
									value="<%= ("M".equals(modalita))?StringUtils.getParteDecimale(aComputo.getImportoMulta()):""%>"
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
							</td>
							<td width="25">&nbsp;</td>
							<td class="c">
								<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp; 
								<font class="label">Giorni</font>
								<br> 
								<input type="text" name="AArr_<%=i%>" id="AArr_<%=i%>" maxlength="2" size="2"
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumAnniArresto()):""%>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp; 
								<input type="text" name="MArr_<%=i%>" id="MArr_<%=i%>" maxlength="2" size="2" 
									value="<%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumMesiArresto()):""%>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
								<input type="text" name="GArr_<%=i%>" id="GArr_<%=i%>" maxlength="4" size="4"
									value="<%=  ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNumGiorniArresto()):""%>" 
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
							</td>
							<td class="c">
								<font class="label">Ammenda</font>
								<br> 
								<input style="align: right" type="text" name="Ammenda_<%=i%>" id="Ammenda_<%=i%>" maxlength="8" size="6" 
									value="<%= ("M".equals(modalita))?StringUtils.getParteIntera(aComputo.getImportoAmmenda()):""%>"
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> , 
								<input style="align: right" type="text" name="Amm_dec_<%=i%>" id="Amm_dec_<%=i%>" maxlength="2" size="2" 
									value="<%= ("M".equals(modalita))?StringUtils.getParteDecimale(aComputo.getImportoAmmenda()):""%>"
									onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
							</td>
						</tr>
						<tr>
							<td class="c" colspan="1"><font class="label">Motivazioni:</font></td>
							<td class="c" colspan="4">
								<textarea cols="60" rows="2"
									name="motivazioni_<%=i%>"> <%= ("M".equals(modalita))?StringUtils.toStringJSP(aComputo.getNote(), "" ):"" %>
								</textarea>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<% } %>

			<tr>
				<td class="l" colspan="100%"><a
					href="Javascript:addQuantum('f');"> Aggiungi ulteriore computo
				</a></td>
			</tr>
		</table>


		<table>
			<tr>
				<td class="lNoBord" colspan="2">
				<INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</form>

	<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>
