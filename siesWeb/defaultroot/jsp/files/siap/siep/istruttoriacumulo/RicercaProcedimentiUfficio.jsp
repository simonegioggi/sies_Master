<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="TornaQui" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaProcedimenti" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="IstruttoriaCumulo" 		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="lsoggetto" 				scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="StatoNascita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaTitoliInIstruttoria"  scope="request" class="java.util.Vector"/>

<%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
<jsp:useBean id="checkRicercaProvvVal"	scope="request" class="java.lang.String"/>
<jsp:useBean id="chiaveAnnoRich" 	    scope="request" class="java.lang.String"/>
<jsp:useBean id="chiaveProgRich" 	    scope="request" class="java.lang.String"/>
<jsp:useBean id="offsetUffAccorpato"    scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" /> 
<jsp:useBean id="primoCaricamento"      scope="request" class="java.lang.String"/>

<jsp:useBean id="isCheckCognome"        scope="request" class="java.lang.String"/>
<jsp:useBean id="isCheckNome"           scope="request" class="java.lang.String"/>
<jsp:useBean id="isCheckCUI"            scope="request" class="java.lang.String"/>
<jsp:useBean id="isCheckDataNascita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="isCheckComune"         scope="request" class="java.lang.String"/>
<jsp:useBean id="isCheckStato"          scope="request" class="java.lang.String"/>
<%-- MEV_2025-48 --%>

<%
//==============================================================================
// Jsp per la visualizzazione delle richieste atti ricevute
//==============================================================================
int TotaleIscrivibili=0;
Iterator itx1 = ListaProcedimenti.iterator();
while (itx1.hasNext()) {
	FascicoloSiepModel lProc = (FascicoloSiepModel)itx1.next();
	if(!"SI".equals(lProc.getgiaInIstruttoria()) )
		TotaleIscrivibili ++;
}	

// 26/04/2019 MEV70 Controllo eventuale presenza del titolo esecutivo legato al Procedimento da Importare tra i Procedimenti già in istrutttoria.  
String stessoTitolo = ""; 
Vector<TitoloCumulatoModel> VecTitCum = new Vector(ListaTitoliInIstruttoria);
String[] aTitoli = new String[ListaProcedimenti.size()]; 
Integer aNumCheck = ListaProcedimenti.size();

for (int k=0; k<ListaProcedimenti.size();k++){
	FascicoloSiepModel lfascicolo = (FascicoloSiepModel)ListaProcedimenti.get(k);
	SentenzaModel lsentenza = lfascicolo.getSentenza();
	if(lfascicolo.getgiaInIstruttoria().compareTo("SI")!=0)  {
		if (VecTitCum != null) {
			for (int ii=0; ii<VecTitCum.size();ii++) {
				TitoloCumulatoModel lTitoloCumModel = (TitoloCumulatoModel) VecTitCum.elementAt(ii);
	        	if (lsentenza != null && lTitoloCumModel.isStessoTitolo(lsentenza)) {
	        		// Ticket#202602100127 - si gestisce il caso di lTitoloCumModel.getProcedimentoCumulato()==null
	        		//                       che andava in null pointer
	        		if (lTitoloCumModel.getProcedimentoCumulato()!=null) { 
	             	stessoTitolo += " "+lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" ";
	             	aTitoli[k]=lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato();
	        		} else {
	        			stessoTitolo += " n.d./n.d. ";
	        			aTitoli[k] = "n.d./n.d.";	             		
	        		}
					break;
	        		// Ticket#202602100127
	        	} else {
	             	aTitoli[k]="";
	        	}
			}
		}
	} else {
	 	aTitoli[k]="p";
	 	aNumCheck--;	// 14/06/2019 MEV70 I fascicoli selezionabili (con presenza del checkBox) non sono già presenti 
	}
}

%>

<html>
  <head>
    <title>[S.I.E.S.] - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    <script language="JavaScript">
      window.focus();
      
      var desktop;
      
      <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
      <%
      SoggettoModel soggettoSessione = (SoggettoModel) session.getAttribute("soggetto");
      %>
      <%-- Ticket#202602260149 - sostituzione apice singolo con doppio apice per nomi e cognomi con accentate --%>
      var cognome     = "<%=soggettoSessione.getCognome()%>";
      var nome        = "<%=soggettoSessione.getNome()%>";
      var codCUI      = "<%=soggettoSessione.getCodAfis()%>";
      var descComune  = "<%=soggettoSessione.getDescrComuneNascita()%>";
      var codStato    = "<%=soggettoSessione.getCodStatoNascita()%>";
      var ggNascita   = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoSessione.getDataNascita(),"dd" ),"")%>";
      var mmNascita   = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoSessione.getDataNascita(),"MM" ),"")%>";
      var aaNascita   = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoSessione.getDataNascita(),"yyyy" ),"")%>";

      
      $(document).ready(function(){
          <%if (checkRicercaProvvVal.equals("checked") && 1==1) { %>
            checkAbilitaRicercaProcedimento($('[name="checkRicercaProvv"]'));
          <% } %>

          //checkAbilitaRicercaProcedimento($('[name="checkRicercaProvv"]'));
          
          <%if ("true".equals(primoCaricamento)){ %>
          if (codCUI.length > 0 ) {
              $('[name="checkCognome"]').prop('checked',true);
              $('[name="checkNome"]').prop('checked',true);
              $('[name="checkDataNascita"]').prop('checked',true);
              $('[name="checkComune"]').prop('checked',true);
              $('[name="checkStato"]').prop('checked',true);
              checkAbilitaDisabilitaCampi($('[name="checkCognome"]'),'<%=ICostantiSoggetto.CAMPO_COGNOME%>' );
              checkAbilitaDisabilitaCampi($('[name="checkNome"]'),'<%=ICostantiSoggetto.CAMPO_NOME%>' );
              checkAbilitaDisabilitaDataNascita($('[name="checkDataNascita"]'));
              checkAbilitaDisabilitaCampi($('[name="checkComune"]'),'<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>' );
              checkAbilitaDisabilitaCampi($('[name="checkStato"]'),'<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>' );        	  
          }
          <% } else { %>
          	<%-- if (request.getParameter("checkCognome")!=null) { %>$('[name="<%=ICostantiSoggetto.CAMPO_COGNOME%>"]').val('<%=request.getParameter("checkCognome")%>')<% } --%>
          	<% if ("checked".equals(isCheckCognome)) { %>
          	    $('[name="<%=ICostantiSoggetto.CAMPO_COGNOME%>"]').val('<%=request.getParameter("checkCognome")%>');
          		$('[name="checkCognome"]').prop('checked',true);
          		checkAbilitaDisabilitaCampi($('[name="checkCognome"]'),'<%=ICostantiSoggetto.CAMPO_COGNOME%>' );
          	<% } %>
          	<% if ("checked".equals(isCheckNome)) { %>
	      	    $('[name="<%=ICostantiSoggetto.CAMPO_NOME%>"]').val('<%=request.getParameter("checkNome")%>');
	      		$('[name="checkNome"]').prop('checked',true);
	      		checkAbilitaDisabilitaCampi($('[name="checkNome"]'),'<%=ICostantiSoggetto.CAMPO_NOME%>' );
      		<% } %>
          	<% if ("checked".equals(isCheckCUI)) { %>
	      	    $('[name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>"]').val('<%=request.getParameter("checkCUI")%>');
	      		$('[name="checkCUI"]').prop('checked',true);
	      		checkAbilitaDisabilitaCampi($('[name="checkCUI"]'),'<%=ICostantiSoggetto.CAMPO_COD_AFIS%>' );
  			<% } %> 
          	<% if ("checked".equals(isCheckDataNascita)) { %>
          		var dataNascita = '<%=request.getParameter("checkDataNascita")%>';
          		var parti = dataNascita.split('/');
        		$('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').val(parti[0]);
        		$('[name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"]').val(parti[1]);
        		$('[name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"]').val(parti[2]);
        		
	      		$('[name="checkDataNascita"]').prop('checked',true);
	      		checkAbilitaDisabilitaDataNascita($('[name="checkDataNascita"]'));
      		<% } %>
          	<% if ("checked".equals(isCheckComune)) { %>
	      	    $('[name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>"]').val('<%=request.getParameter("checkComune")%>');
	      		$('[name="checkComune"]').prop('checked',true);
	      		checkAbilitaDisabilitaCampi($('[name="checkComune"]'),'<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>' );
			<% } %>
          	<% if ("checked".equals(isCheckStato)) { %>
	      	    $('[name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>"]').val('<%=request.getParameter("checkStato")%>');
	      		$('[name="checkStato"]').prop('checked',true);
	      		checkAbilitaDisabilitaCampi($('[name="checkStato"]'),'<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>' );
			<% } %>
          	
          <% } %>
      });
      
      <%-- MEV_2025-48 - FINE --%>
      
      <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
      <%--function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }--%>
      function ListaComuniNascita(a_formname,a_fieldname) {
    		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
      }
      <%-- MEV_2025-48 - FINE --%>
      
      // Torna indietro su ElencoFascicoli coinvolti
function eseguiFunzione(action) {
        document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.f.submit();
      }
      
      // azzeramento dei campi Ricerca
function pulisciCogno() {
      	document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value = "";
       }
      
function pulisciNome() {
      	document.f.<%=ICostantiSoggetto.CAMPO_NOME%>.value = "";
      }
      
function pulisciCui() {
      	document.f.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.value = "";
      }
      
function pulisciData() {
          document.f.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value= "";
          document.f.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value= "";
          document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value= "";
      }
      
function pulisciComune() {
      	document.f.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value = "";
      }
      
function pulisciStato() {
      	document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA %>.value = "";
      }
      
      //Funzione utile per impostare la data corrente. ?????
      function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
        day=dataOdierna.substring(0,2);
        month=dataOdierna.substring(3,5);
        year=dataOdierna.substring(6,10);
        document.getElementsByName(campo_giorno).item(0).value = day;
        document.getElementsByName(campo_mese).item(0).value = month;
        document.getElementsByName(campo_anno).item(0).value = year;      
      }
      
      function espandi(idTabella){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        var hrefNew = "Javascript:collassa('"+idTabella+"');";
        
        $('#idHrefRicerca').attr('href',hrefNew);
        $('#idHrefRicerca').children().attr('src',collapseGif);
      
        var tabella = $('#'+idTabella).fadeIn();
      }
      
      function collassa(idTabella){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
        var hrefNew = "Javascript:espandi('"+idTabella+"');";
        
        $('#idHrefRicerca').attr('href',hrefNew);
        $('#idHrefRicerca').children().attr('src',collapseGif);
      
        var tabella = $('#'+idTabella).fadeOut();
      }
      
      function Verify()
      {
    	<%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
        if ($('[name="checkRicercaProvv"]').prop('checked')) {
          var anno = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value;
          if(anno.length<4)
          { 
            alert("Per la ricerca per procedimento e' necessario specificare sia anno che numero procedimento");      
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
            return false;
          }
          if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value == "")
          {
            alert("Per la ricerca per procedimento e' necessario specificare sia anno che numero procedimento");      
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
            return false;
          }            
        } 
        else 
        {   
        	var cognome = $('[name="<%=ICostantiSoggetto.CAMPO_COGNOME%>"]').val();
        	var codCUI = $('[name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>"]').val();
        	var ggNascita = $('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').val();

            if(   (!$('[name="<%=ICostantiSoggetto.CAMPO_COGNOME%>"]').prop('disabled')
                    && $.trim(cognome)!='')
                || (!$('[name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>"]').prop('disabled')
                    && $.trim(codCUI)!='')
                || (!$('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').prop('disabled')
                    && $.trim(ggNascita)!='')
                ){
           	  // alert("valorizzato");
            }
             else  {
                 alert('Per impostare la Ricerca inserire almeno uno tra: \nCognome, Codice CUI, Data Nascita');      
                 document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
                 return false;
             }

          
          <%--if(document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value == "" && 
             document.f.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.value == "" &&  
             document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value == "" )
          {
            alert('Per impostare la Ricerca inserire almeno uno tra: \nCognome, Codice CUI, Data Nascita');      
            document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
            return false;
          } --%>
          <%-- MEV_2025-48 - FINE --%>
        
          var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        
          // Data Nascita
          <%-- 202603 Il check va fatto solo se i campi sono abilitati --%>
          if ( !$('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').prop('disabled') )
          {
            var data_nasc =     document.f.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value
                            +'/'+document.f.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value
                            +'/'+document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
            if (!ControllaDataPassaVuota(data_nasc)) {
              alert('Data Nascita non corretta');      
              document.f.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.focus();
              return false;
            }
          }

          <%-- 202603 Il check va fatto solo se entrambi i campi sono abilitati --%>
          if (   !$('[name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>"]').prop('disabled') 
              && !$('[name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>"]').prop('disabled')
              && document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.value != "" 
              && document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.value != "-"
             ) 
          {
                if (   document.f.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.value != ""
                    && document.f.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.value != "-"
                    && document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.f.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value != '039'
                   ) 
                {
                        alert('Il campo Stato Nascita e comune nascita incongruenti');
                        return false;
                }
          }
        }
        
        
        // Ripulisco la lista
        $('#divRisultatoRicerca').hide();
        
        return true;
      
      } // Chiude Verify()
      
      <%-- Ticket#20220127012 - Funzione riscritta --%>
function IscrizioneinIstru() {
        // 29/04/2019  MEV70 Elaborazione dell'Array contrenente i riferimenti ad eventuali titoli giàpresenti in Istruttoria.
        // - Se un procedimento e già presente in Istruttoria il relativo elemento nell'Array è contrassegnato con "p"; 
        // - Se un procedimento non e presente ma afferisce a un Titolo già presente in Istruttoria, il relativo elemento nell'Array è contrassegnato con "Anno/Numero procedimento"; 
        // - Se un procedimento e il relativo Titolo Esecutivo non sono presenti in Istruttoria, il relativo elemento nell'Array è contrassegnato con ""; 
        <%
        StringBuffer titoliSB1 = new StringBuffer();
        for (int i = 0; i < aTitoli.length; ++i) {
          if (titoliSB1.length() > 0) {
            titoliSB1.append(',');
          }
            titoliSB1.append('"').append(aTitoli[i]).append('"');
          }
        %>

        var titoliJS = [ <%= titoliSB1.toString() %> ];
        var aNumCheckBox = <%=aNumCheck%>;   	 
        
           
        //MAC 20200110018 - 20200122 - MG: errore NON segnalato da utente ma rilevato durante l'esecuzione dei test 
        //in fase di valorizzazione della variabile aStessoTitolo va tenuto conto della dimensione della lista
        //dei procedimenti iscrivibili ( sizelista== 1 oppure sizelista > 1)
        var sizelista = <%=TotaleIscrivibili%>;	
        
        // Scrorro la lista dei titoli in tabella:
        // verificao che almeno un titolo sia stato selezionato
        // verifico tra i selezionati quali titoli sono già in istruttoria
        // verifica tra i selezionati se esistono titoli non caricato da NSC
        var NSC = "SI";
        var aStessoTitolo = '';
        var contaSelezionati = 0;
        
        for (var j = 0; j < <%=aTitoli.length%>; j++) {
<%
// Ticket#20251104017: casistica di un solo elemento in lista
if (aTitoli.length == 1) {
%>
		if (document.f.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.checked)
<%
} else {
%>
		if (document.f.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>[j].checked)
<%
}
%>
          {
            contaSelezionati++;
            // Testo se in istruttoria
            if (   titoliJS[j] !="p" // selezionabile
					&& titoliJS[j] != "") { // titolo già in iestruttoria
                aStessoTitolo+= titoliJS[j]+' ';
            }
            
// Testo se iscritto a NSC
<%
if (aTitoli.length == 1) {
%>
			if (document.f.<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC%>.value != "SI")
<%
} else {
%>
            if (document.f.<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC%>[j].value != "SI")
<%
}
%>
            {
              NSC = "NO";
            }
          }
        }        
    if (sizelista == 0) {
          alert("Nessun Fascicolo / Titolo da Inserire in Istruttoria");
          document.f.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
          return false;	 
	} else if (contaSelezionati == 0) {
          alert("Spuntare la checkBox del relativo Fascicolo / Titolo da Inserire in Istruttoria");
          return false;          
        }      
 
      	 
        var msgConfirm="";
        var esegui = true;
        if(aStessoTitolo.length > 0 )
        {
          msgConfirm = "Attenzione! Gia' e' presente in Istruttoria Cumulo\n il Procedimento "+aStessoTitolo+" con estremi del Titolo Esecutivo\n";
          msgConfirm += "uguali a quelli di un procedimento che si sta per iscrivere in istruttoria.";
          msgConfirm += "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato/i?"; 
          
          esegui = window.confirm(msgConfirm);
        } 
      	 
        if (esegui) 
        {
          if(NSC == "NO")
          {
            msgConfirm = "Attenzione! Almeno uno dei Procedimenti selezionati non risulta ancora trasmesso a NSC.\n";
            msgConfirm += "Per procedere all'iscrizione in Instruttoria, e' consigliabile prima affettuare lo scarico su NSC\n";
            msgConfirm += "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato?"; 

            esegui = window.confirm(msgConfirm);
          }	 
        } else {
          msgConfirm="";
        }
      	 
        if (esegui && msgConfirm=="") {
          msgConfirm = "\nSi vuole procedere all'iscrizione del/dei Titolo/i selezionato?"; 
          esegui = window.confirm(msgConfirm);
        }
      	 
        if( esegui ) 
        {
          lAzione = "siap.siep.istruttoriacumulo.action.ActInserisciFascicoloProprioUfficioInIstruttoria";
          document.f.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.f.submit();
          document.f.AGGIUNGI.disabled=true;
          document.body.style.cursor='wait';
        }
      }
      <%-- Ticket#20220127012 - FINE --%>
      <%-- Ticket#20220127012 - Funzione rivista --%>
function CtrStato(checkObject) {
        alert("Attenzione: Il Procedimento selezionato NON è mai stato Validato.\nPer procedere all'iscrizione in Instruttoria è necessario prima Validarlo");
        checkObject.checked = false;
	 }
 	<%-- Ticket#20220127012 - FINE --%>
	  <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
      function checkAbilitaDisabilitaCampi(checkObject, nomeCampo){

		var jQueryObj = $(checkObject);
		if (jQueryObj.prop('checked')){
			jQueryObj.val($('[name="'+nomeCampo+'"]').val());
			$('[name="'+nomeCampo+'"]').prop('disabled',true);
		}
		else {
			$('[name="'+nomeCampo+'"]').prop('disabled',false);
		}
      }      

      function checkAbilitaDisabilitaDataNascita(checkObject){
    	var jQueryObj = $(checkObject);
    	
    	if (jQueryObj.prop('checked')){
    		var valore = "";
    		var gg = $('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').val();
    		gg = gg.length==1 ? '0'+gg : gg;
    		valore = gg+'/';
    		var mm = $('[name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>"]').val();
    		mm = mm.length==1 ? '0'+mm : mm;
    		valore = valore+mm+'/';
    		var yyyy = $('[name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"]').val();
    		yyyy = yyyy.length==4 ? yyyy : (yyyy.length==2 ? '20'+yyyy : yyyy);
    		valore = valore+yyyy;
    		
			jQueryObj.val(valore);

    		$('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').prop('disabled',true);
    		$('[name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"]').prop('disabled',true);
    		$('[name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"]').prop('disabled',true);
  		}
  		else {
    		$('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').prop('disabled',false);
    		$('[name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"]').prop('disabled',false);
    		$('[name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"]').prop('disabled',false);
    	}
      }
	        
      function ripristinaValori(valore, nomeCampo){
  		$('[name="'+nomeCampo+'"]').val(valore);
      }
      
      function ripristinaValoriDataNascita(){
  		  $('[name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"]').val(ggNascita);
		  $('[name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"]').val(mmNascita);
		  $('[name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"]').val(aaNascita);
      }
      
      function checkAbilitaRicercaProcedimento(checkObject){
          var jQueryObj = $(checkObject);
          if (jQueryObj.prop('checked')){
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>"]').prop('disabled',false);
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"]').prop('disabled',false);
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>"]').prop('disabled',false);
              
              $('[name="checkCognome"]').prop('checked',true);
              $('[name="checkNome"]').prop('checked',true);
              $('[name="checkCUI"]').prop('checked',true);
              $('[name="checkDataNascita"]').prop('checked',true);
              $('[name="checkComune"]').prop('checked',true);
              $('[name="checkStato"]').prop('checked',true);
              
              $('[name="checkCognome"]').prop('disabled',true);
              $('[name="checkNome"]').prop('disabled',true);
              $('[name="checkCUI"]').prop('disabled',true);
              $('[name="checkDataNascita"]').prop('disabled',true);
              $('[name="checkComune"]').prop('disabled',true);
              $('[name="checkStato"]').prop('disabled',true);
          }
          else {
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>"]').prop('disabled',true);
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"]').prop('disabled',true);              
              $('[name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>"]').prop('disabled',true);
              
              $('[name="checkCognome"]').prop('checked',false);
              $('[name="checkNome"]').prop('checked',false);
              $('[name="checkCUI"]').prop('checked',false);
              $('[name="checkDataNascita"]').prop('checked',false);
              $('[name="checkComune"]').prop('checked',false);
              $('[name="checkStato"]').prop('checked',false);
              
              
              $('[name="checkCognome"]').prop('disabled',false);
              $('[name="checkNome"]').prop('disabled',false);
              $('[name="checkCUI"]').prop('disabled',false);
              $('[name="checkDataNascita"]').prop('disabled',false);
              $('[name="checkComune"]').prop('disabled',false);
              $('[name="checkStato"]').prop('disabled',false);
          }
          checkAbilitaDisabilitaCampi($('[name="checkCognome"]'),'<%=ICostantiSoggetto.CAMPO_COGNOME%>' );
          checkAbilitaDisabilitaCampi($('[name="checkNome"]'),'<%=ICostantiSoggetto.CAMPO_NOME%>' );
          checkAbilitaDisabilitaCampi($('[name="checkCUI"]'),'<%=ICostantiSoggetto.CAMPO_COD_AFIS%>' );
          checkAbilitaDisabilitaDataNascita($('[name="checkDataNascita"]'));
          checkAbilitaDisabilitaCampi($('[name="checkComune"]'),'<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>' );
          checkAbilitaDisabilitaCampi($('[name="checkStato"]'),'<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>' );
      }
      <%-- MEV_2025-48: FINE --%>
    </script>
  </head>
  
<body class="corpo" style="margin-top: 0px; margin-left: 0px;">
<table>
  <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
    <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Ricerca Procedimenti per Titolo e Soggetto</font>&nbsp;&nbsp;
    </td>
    <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      	</td>
  </tr>
</table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
<br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaPropriProcedimenti">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="Titolo" colspan="6"> Criteri di ricerca 
        <a id="idHrefRicerca" href="Javascript:collassa('tabCriteriRicerca');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0"/></a>
      </td>
    </tr>
  </table>
  <table id="tabCriteriRicerca" cellspacing="2" cellpadding="2" width="100%" >
    <tr>
      <td class="l">Cognome</td>
      <td class="l" colspan="1">
        	<input type="text" title="Cognome" maxlength="35" size="35" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="<%=StringUtils.toStringJSP(lsoggetto.getCognome(),"")%>">
        <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
        <%-- a href="Javascript:pulisciCogno();"><img src="/images/delete.gif" border=0></a>  --%>
        <a href="Javascript:ripristinaValori(cognome,'<%=ICostantiSoggetto.CAMPO_COGNOME%>');" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a>				
		<input type="checkbox" <%=isCheckCognome %> name="checkCognome" value="checkCognome" 
			   title="Escludi campo dalla ricerca"
		       onClick="Javascript:checkAbilitaDisabilitaCampi(this,'<%=ICostantiSoggetto.CAMPO_COGNOME%>');">  
		<%-- MEV_2025-48 - FINE --%>		          
      </td>
      <td class="l">Nome &nbsp;
        	<input type="text" title="Nome"  maxlength="35" size="35" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="<%=StringUtils.toStringJSP(lsoggetto.getNome(),"")%>">
               <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
        <%-- <a href="Javascript:pulisciNome();"><img src="/images/delete.gif" border=0></a> --%>
        <a href="Javascript:ripristinaValori(nome,'<%=ICostantiSoggetto.CAMPO_NOME%>');" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a>
		<input type="checkbox" name="checkNome" 
		       title="Escludi campo dalla ricerca"
		       onClick="Javascript:checkAbilitaDisabilitaCampi(this,'<%=ICostantiSoggetto.CAMPO_NOME%>');">     
		<%-- MEV_2025-48 - FINE --%>		       
      </td>
      <td class="l" colspan="2">Codice CUI &nbsp;
          	<input title="Codice CUI" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_AFIS %>" value="<%=StringUtils.toStringJSP(lsoggetto.getCodAfis(),"")%>" maxlength="7" size="7">
          	<%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
        <%--  <a href="Javascript:pulisciCui();"><img src="/images/delete.gif" border=0></a>	 --%>
          <a href="Javascript:ripristinaValori(codCUI,'<%=ICostantiSoggetto.CAMPO_COD_AFIS%>');" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a>
		<input type="checkbox" name="checkCUI" 
		       title="Escludi campo dalla ricerca"
		       onClick="Javascript:checkAbilitaDisabilitaCampi(this,'<%=ICostantiSoggetto.CAMPO_COD_AFIS%>');">
		<%-- MEV_2025-48 - FINE --%>
      </td>
    </tr>
    
    <tr>
      <td class="l">Data Nascita</td>
      <td class="l"> 
  			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(), "dd"), "")%>"
               name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(),"MM" ),"")%>"
               name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lsoggetto.getDataNascita(),"yyyy" ),"")%>"
               name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
         >&nbsp;
         <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
         <%-- <a href="Javascript:pulisciData();"><img src="/images/delete.gif" border=0></a> --%>
         <a href="Javascript:ripristinaValoriDataNascita();" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a> 
         <input type="checkbox" name="checkDataNascita" 
                title="Escludi campo dalla ricerca"
                onClick="Javascript:checkAbilitaDisabilitaDataNascita(this);"> 
         <%-- MEV_2025-48 - FINE --%>
      </td>
      
      <td class="l">Comune di nascita  &nbsp;
        <input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lsoggetto.getDescrComuneNascita(),"")%>" type="text" 
        	   name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>" maxlength="30" size="30">
         <a href="Javascript:ListaComuniNascita('f','<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>');">
        	<img src="/images/filefolder.gif" border=0></a>
         <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
         <%-- <a href="Javascript:pulisciComune();"><img src="/images/delete.gif" border=0></a> --%>
         <a href="Javascript:ripristinaValori(descComune,'<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>');" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a>
         <input type="checkbox" name="checkComune" 
                title="Escludi campo dalla ricerca"
                onClick="Javascript:checkAbilitaDisabilitaCampi(this,'<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>');"> 
         <%-- MEV_2025-48 - FINE --%>       
      </td>

      <td class="l">Stato di Nascita</td>
      <td class="L">
        <select  title="Stato di Nascita" name=<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA %> >
    	<%=StatoNascita%>
        </select>
        <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
        <%--  <a href="Javascript:pulisciStato();"><img src="/images/delete.gif" border=0></a> --%>
         <a href="Javascript:ripristinaValori(codStato,'<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>');" title="Ripristina Valore anagrafica Soggetto cumulante">
		   <img src="/images/ActiveSession.gif" border=0 width="12px" height="12px"></a>        
         <input type="checkbox" name="checkStato" 
                title="Escludi campo dalla ricerca"
                onClick="Javascript:checkAbilitaDisabilitaCampi(this,'<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>');"> 
        <%-- MEV_2025-48 - FINE --%>
      </td>
    </tr>
 
 <%-- MEV_2025-48 - Ricerca soggetto da Iscrizione proprio titolo --%>
      <tr>
       <td class="l">Ricerca per Procedimento &nbsp;<input type="checkbox" name="checkRicercaProvv" onClick="Javascript:checkAbilitaRicercaProcedimento(this);" <%=checkRicercaProvvVal%>> </td>
      </tr>
      
     <tr>
      <td class="l">Anno/Numero Procedimento</td>
      <td class="L">
        <input type="text"  maxlength="4" size="4"  
               title="Anno Procedimento"
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)"
               name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>"
               value="<%=chiaveAnnoRich%>" 
               disabled
        	 >
        <input type="text"  maxlength="14" size="14" 
               title="Numero Procedimento" 
               onkeypress="return TicTabNumField(this,event)"
               name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>"
               value="<%=chiaveProgRich%>" 
               disabled
        	 >
      </td>
    </tr>
 	<tr>
	    <td class="L">Ufficio Accorpato</td>
	    <td class="l">
	        <select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>" disabled>
	            <option value="0" >-</option>
	
	            <%
	            Iterator it = elencoUfficiAccorpati.iterator();
	            while (it.hasNext())  {
	                UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
	                String selected= "";  //offsetUffAccorpato
	                if (ua.getIncrProgressivo().equals(offsetUffAccorpato) )
	                   selected= "selected";
	                %>
	                <option value="<%=ua.getIncrProgressivo()%>" <%=selected %> ><%=ua.getDescrizione()%></option>
	                <%
	            }
	        %>
	        </select>
	    </td>
	</tr>
 <%-- MEV_2025-48 - FINE --%>
 
    <tr>
	    <td><input class="bottone" type="submit" name="Ricerca" value="Ricerca"></td>
    </tr>
  </table>
<div id="divRisultatoRicerca">
 
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing="2" cellpadding="2" width="100%">
	<tr><td class="Titolo" colspan="8">Elenco Procedimenti trovati</td></tr>
    <tr>
      <td class="int">Data Titolo <br> Esecutivo</td>
      <td class="int">Anno e Numero</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato </td>
      <td class="int">Iscrivi</td>
    </tr>
<% 
if (ListaProcedimenti == null || ListaProcedimenti.size() == 0) {
%>
	<tr><td class="c" colspan="7"><center>Nessun procedimento trovato con i criteri di ricerca selezionati</center></td></tr>
<%
} else {	 
	 String lNsc = "";
	Iterator itx = ListaProcedimenti.iterator();
	while(itx.hasNext()) {
		FascicoloSiepModel lfascicolo = (FascicoloSiepModel)itx.next();
		SentenzaModel lsentenza = lfascicolo.getSentenza();
		lNsc="";
		
		// 
        String lAnnoNumeroSIEP = "";
        if (lfascicolo.getChiaveProgrOrig()==null)             
        {
            lAnnoNumeroSIEP =  "<font class='campo'>"+StringUtils.toStringJSP(lfascicolo.getChiaveAnno()) 
                                               +" / "+StringUtils.toStringJSP(lfascicolo.getChiaveProgr())+"</font>";
        }            
        else if (lfascicolo.getChiaveProgrOrig()!=null) 
        {
            lAnnoNumeroSIEP =  "<font class='campo'>"+StringUtils.toStringJSP(lfascicolo.getChiaveAnno()) 
                                               +" / "+StringUtils.toStringJSP(lfascicolo.getChiaveProgrOrig())+"</font>";

            BigDecimal offset = lfascicolo.getChiaveProgr().subtract(lfascicolo.getChiaveProgrOrig());
            
            UfficioAccorpatoModel uffAccFasc = null;
            
            Iterator itUA = elencoUfficiAccorpati.iterator();
            while (itUA.hasNext())  {
                UfficioAccorpatoModel ua = (UfficioAccorpatoModel) itUA.next();
                if (offset.toString().equals(ua.getIncrProgressivo()) )
                    uffAccFasc = ua;
            }
            lAnnoNumeroSIEP += "<br> <font class=\"cRosso\">(Ex " + uffAccFasc.getCodTipoUfficio() + " di " + uffAccFasc.getDescrizione() + ")</font>";
        }
%>  
	    <tr>
		<td class="c">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lsentenza.getDataProvvedimento(), "dd-MM-yyyy"))%>
	      	 <br> <%=StringUtils.toStringJSP(lsentenza.getDescrTipoProvvedimento()) %></font>
<%
		if (lfascicolo.getKeyProvvNsc() == null) {
%> 
	   			&nbsp;<font class=crosso> (*)</font>&nbsp;
<%
		} else {
			lNsc = "SI";
		}
%>				  	 
	      </td>
	      <td class="c" nowrap ><font class="campo"><%=StringUtils.toStringJSP(lsentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(lsentenza.getNumeroSentenza())%></font></td>
		<td class="c">
			<font class="campo"><%=StringUtils.toStringJSP(lsentenza.getDescrTipoAutoritaEmittente())%></font>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lsentenza.getDescrLuogoEmittente())%></font>
	      </td>		 
	      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lfascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
	      <td class="c" nowrap><%=lAnnoNumeroSIEP%></td>
	      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lfascicolo.getDataIscrizione(),"dd-MM-yyyy"))%></font></td>
<%
		if ("02".equals(lfascicolo.getCodStatoFascicolo())) {
%>
	      <td class="c"><font class="campo" style="color:red" ><%=StringUtils.toStringJSP(lfascicolo.getDescrStatoFascicolo())%></font></td>
<%
		} else {
%>	      
	      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lfascicolo.getDescrStatoFascicolo())%></font></td>
<%
		}
		if ("SI".equals(lfascicolo.getgiaInIstruttoria())) {
%>
		<td class="c">
			<font style="color:green"><img src="/images/V.gif"></font>
      	    			<font style="font-size: 12">già in <br> istruttoria </font>
          <%-- Ticket#20220127012 SI aggiunge sempre il campo con IdFascicolo ed NSC per evere la tabella complata per i controlli JS  --%>
	        <input type="hidden" disabled name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=lfascicolo.getIdFascicoloSiep()%>" title="Iscrivi in Istruttoria">     	  
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC %>" value="<%=lNsc%>" > 
          <%-- Ticket#20220127012 FINE --%>
      </td>
 <%
 		} else {
 %>     	  
	      <td class="c">
	        <input type="checkbox" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP %>" value="<%=lfascicolo.getIdFascicoloSiep()%>" 
	        						title="Iscrivi in Istruttoria" 
				<%-- Ticket#20220127012 Modificata la chiamata alla funzione CtrStato onclick1="javascript:CtrStato('<%=lfascicolo.getCodStatoFascicolo()%>');" --%>
<%
			if ("02".equals(lfascicolo.getCodStatoFascicolo())) {
%>
	        						onclick="javascript:CtrStato(this);" 
<%
			}
%>
	        						<%-- Ticket#20220127012 - FINE --%>
	        						>
	        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_KEY_PROVV_NSC %>" value="<%=lNsc%>" >
	      </td>
<%
		}
%>

		</tr>
<%
	} // end while
}
%>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Iscrivi in istruttoria" onClick="javascript:IscrizioneinIstru();">
      </td>
      <td class="l" colspan="7">&nbsp;&nbsp;&nbsp;
      	<font class="label"> N.B.: &nbsp; I Fascicoli segnati con</font>
      	&nbsp;<font class=crosso> (*)</font>&nbsp;
      	<font class="label"> risultano non ancora trasmessi a N.S.C.  &nbsp;</font>
      </td>	    
    </tr>
   </table> 
</div> <%-- divRisultatoRicerca --%>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>