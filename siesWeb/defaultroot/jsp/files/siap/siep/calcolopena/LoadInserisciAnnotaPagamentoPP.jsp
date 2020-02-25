<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%// Pena in decorrenza %>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<jsp:useBean id="UtenteConnesso" 	  scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
  
<%// Caricamento combo altra autorità %> 
<jsp:useBean id="autoritaEsternaAltra"  scope="request" class="java.lang.String" />
<%

//==============================================================================
// Funzione:  Converisione delle Pene Pecuniarie
// Microfunzione:form per l'Inserimento Annotazione avvenuto pagamento Pene Pecuniarie</p>
// menu: 'Rideterminazione Pena - Annoatzione Pagamento Pene Pecuniarie'
//
// - Posizione giuridica
// - Pena Residua In espiazione/Da espiare
// -
//==============================================================================
int maxNumComputi = 4;

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>


<html>

<head>
  <title> [S.I.E.S.] - Annotazioni Manuali - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    
    var lMaxNumComputi = <%=maxNumComputi%>;
    
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {

      // - Quantum obbligatori (almeno uno): segno e quantità o importi
      // - Se provvedimento altra autorità obbligatori:
      //   * Tipo Provvedimento
      //   * Oggetto provvedimento
      //   * Autorità emittente tipo e sede
      // - Magistrato Firmatario o Funzio0nario
      var obbligatori;

      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      if (document.f.TipoOrd[1].checked)
      {        
        // Data Ricezione Comunicazione  Altra Autorità
	        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value.length==1)
	        {
	          	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value='0'+
	          	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value;
	        }  	
	        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value.length==1)
	        {
	          	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value='0'+
	          	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value;
	        } 	

        	var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+
        						 document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+
        						 document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;

	        if (!ControllaData(data_to_verify) )
	        {
		          alert('Data di ricezione Provvedimento Altra Autorità non valida');
		          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
		          return false;
	        }
        
        // Data Emissione Comunicazione Altra Autorità
	        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value.length==1)
	        {
	          	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value='0'+
	          	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value;
	        }  	
	        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value.length==1)
	        {
	          	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value='0'+
	          	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value;
			}
        	var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value+'/'+
        					     document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value+'/'+
        					     document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value;

	        if (!ControllaData(data_to_verify) )
	        {
		          alert('Data di emissione Provvedimento Altra Autorità non valida');
		          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.focus();
		          return false;
	        }

	// Partita di Credito ed Ex Campione Penale : Per inserirli in Evento 
	//			Partita di Credito va in CAMPO_ANNO_PROTOCOLLO,CAMPO_PROGR_PROTOCOLLO
	//			Ex Campione Penale va in CAMPO_PROTOCOLLO_RES				
	
	        if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value=="")
	        {
		          alert('Anno Partita di Credito Obbligatorio');
		          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
		          return false;
	        }
	        else if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value<1950)
	        {
		          alert('Anno Partita di Credito non valido');
		          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
		          return false;
	        }
        
	        if (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value=="")
	        {
		          alert('Numero Partita di Credito obbligatorio');
		          document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.focus();
		          return false;
	        }
	        
	       	if(	document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value==""
			 && document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO %>.value==""
	         && document.f.<%=ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE %>.value==""  )
			{
				         alert("Inserire Almeno un campo tra Partita di Cradito e EX Campione Penale")
				         document.LoadInserisciRichiestaConversione.<%= ICostantiEvento.CAMPO_ANNO_PROTOCOLLO %>.focus(); 
				         return false;
			}	        
        // Tipo provvedimento : AMBROSINO - tolto il campo  ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO e
        //                                  relativo controllo
 		//	tolto il campo  ICostantiEvento.CAMPO_COD_MOTIVO e relativo controllo; 
        //        Adesso è fisso --> Nuovo codice Motivo = ANNOTAZIONE AVVENUTO PAGAMENTO PP 

        // Sede 

	        if (document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=="" ||
	        	document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=="-")
	        {        
			          alert("Selezionare Sede Autorità Emittente");
			          document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
			          return false;
	        }
      }
  
      // Quantum di computo
      var numComputi = 0;
      
      // Per ogni rigo visibile verifico la coerenza dei dati. 
      // Se presente il segno vanno specificati anche i quantum.
      // Se presenti i quantum è obbligatorio il segno.
      //
      // E' accettato il rigo vuoto anche se visibile.
      
      for (i=0; i<lMaxNumComputi; i++)
      {

	        idComputo = "annotazione_"+i;
	        display = document.getElementById(idComputo).style.display;

	        if (display=="block")
	        {
         // 	Rigo visibile, quindi controllo la presenza e coerenza dei dati
		          if (document.getElementById('PM_'+i).selectedIndex ==0)
		          {

         // 		Segno assente, quindi verifico che NON ci siano i quantum
			            if (   trimStringa(document.getElementById('Multa_'+i).value) != "" 
			                || trimStringa(document.getElementById('Mul_dec_'+i).value) != "" 
			                || trimStringa(document.getElementById('Ammenda_'+i).value) != "" 
			                || trimStringa(document.getElementById('Amm_dec_'+i).value) != "" 
			                || trimStringa(document.getElementById('motivazioni_'+i).value) != ""
			               )
			            {
				              alert ("Selezionare uno tra + e - , oppure togliere i quantum");
				              document.getElementById('PM_'+i).focus();
				              return false;
            			}
          		  }
          		  else 
          		  {
            // Presente il segno, quindi verifico che siano presenti i quantum
			            if (   trimStringa(document.getElementById('Multa_'+i).value) == "" 
			                && trimStringa(document.getElementById('Mul_dec_'+i).value) == "" 
			                && trimStringa(document.getElementById('Ammenda_'+i).value) == "" 
			                && trimStringa(document.getElementById('Amm_dec_'+i).value) == "" 
			               )
			            {
				              alert ("Indicare i quantum o deselezionare il segno + e -");
				              document.getElementById('Multa_'+i).focus();
				              return false;
            			}
            			else
            			{
              				numComputi=numComputi+1;
            			}
          			}  // chiude else
        	
        	}  // chiude if (display=="block")
        	
      }  // Chiude ciclo for
      
      if (numComputi==0)
      {
        	alert ("Inserire almeno un computo con multa e/o ammenda ");
        	return false;
      }

	// Data Emissione Comunicazione
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      {
        	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+
        	document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      }  	
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      {
        	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+
        	document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
	  }
	  	
      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+
      					   document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+
      					   document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
	        alert('Data di emissione non valida');
	        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	        return false;
      }     

     
      //=======================
      // Controllo Magistrato  
      //=======================
      if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
	        alert("Selezionare Magistrato firmatario");
	        return false;
      }       

      
      
      return true;
    }
    
    //==========================================================================
    // Visualizza nasconde le combo Oggetto. 
    // n.b. trattasi di due combo distinte una per i provvedimenti di 
    //      ufficio, una per i provvedimento altro ufficio
    //==========================================================================
    function radioBase()
    {
	      if(document.f.TipoOrd[0].checked)
	      {
	         	document.getElementById('OggettoProvvedimentoDufficio').style.display = "block";
	        	document.getElementById('DivAltraAutorita').style.display = "none";
	      }
	      else 
	      {
	        	document.getElementById('OggettoProvvedimentoDufficio').style.display = "none";
	        	document.getElementById('DivAltraAutorita').style.display = "block";
	      }
	      
	      visFirmatario();
      
    }

      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
	         var desktop;
	         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=170,left=90,width=300,height=500");
      }

      function ListaMagistrati(a_formname)
      {
	        var desktop;
	        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, top=170,left=90, width=500, height=500");
      }

   	  function ListaFunzionari(a_formname, a_field2,a_field3)
  	  {
	    	var desktop;
	    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadRicercaUtenteAttivo&formname="+a_formname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Funzionario", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, top=170,left=90, width=500, height=500");
  	  }    

	  function visFirmatario()
	  {
	  		if(document.f.TipoFir[0].checked)
	  		{ 
				document.getElementById('nomeMag').style.display="block";
				document.getElementById('nomeFunc').style.display="none";
			}
			else if(document.f.TipoFir[1].checked)
			{
				document.getElementById('nomeMag').style.display="none";
				document.getElementById('nomeFunc').style.display="block";
			}	
	  }
  
    //==========================================================
    // Visualizza una nuova riga per l'inserimento dei quantum
    //==========================================================
    function addQuantum()
    {
      	for (i=0; i<lMaxNumComputi; i++)
      	{
	        idComputo = "annotazione_"+i;
	        display = document.getElementById(idComputo).style.display;
        	if (display=="none")
        	{
		          Rigo = i;
		          document.getElementById('PM_'+Rigo).disabled = false ;
		          // Multa

		          document.getElementById('Multa_'+Rigo).disabled = false ;
		          document.getElementById('Mul_dec_'+Rigo).disabled = false ;
		    
		          //Ammenda

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
    
    function cancellaComputo(Rigo)
    {
      
      //===========================================================
      // Cancello il contenuto delle celle
      //===========================================================
          document.getElementById('PM_'+Rigo).selectedIndex = 0 ;
      // Multa
	      document.getElementById('Multa_'+Rigo).value = "" ;
	      document.getElementById('Mul_dec_'+Rigo).value = "" ;
      //Ammenda
	      document.getElementById('Ammenda_'+Rigo).value = "" ;
	      document.getElementById('Amm_dec_'+Rigo).value = "" ;
      // Motivazioni
	      document.getElementById('motivazioni_'+Rigo).value = "" ;

      //===========================================================
      // Disabilito le celle in modo che non venga fatta la submit
      //===========================================================
      
	      document.getElementById('PM_'+Rigo).disabled = true ;
	   // Multa
	      document.getElementById('Multa_'+Rigo).disabled = true ;
	      document.getElementById('Mul_dec_'+Rigo).disabled = true ;
	   //Ammenda
	      document.getElementById('Ammenda_'+Rigo).disabled = true ;
	      document.getElementById('Amm_dec_'+Rigo).disabled = true ;
	  // Motivazioni
	      document.getElementById('motivazioni_'+Rigo).disabled = true ;

      //===================
      // Nascondo la riga
      //===================
      	  document.getElementById('annotazione_'+Rigo).style.display = "none";
      
      	  if (Rigo>1)
      	  {
		        strTdCancella = '<a href="Javascript:cancellaComputo(\''+(Rigo-1)+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';
		
		        document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = strTdCancella;
		        document.getElementById('tdCancella_'+(Rigo)).innerHTML = '<br>';
      	  }
    }
  </script>
</head>

<body class="corpo" onLoad="radioBase();">

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciAnnotaPagamentoPP">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Annotazione Pagamento Pena Pecuniaria</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua (attuale)
//==============================================================================
%>
<table>
  <tr>
    <td class="l"> Posizione Giuridica :
      <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
           { %>
        			DETENUTO PER ALTRA CAUSA - <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
        <% } 
           else 
           {  %>
                <%=lPosizione.getDescrPosizioneGiuridica()%>;
   		<% 	} %>
      </font>
    </td>
  </tr>
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio, MAI
// - Se Libero vengono visualizzati i Quantum
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<%
  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
     && PenaComplessiva.getCodTipoPenaDetentiva() != ""
     && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
         || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
        )
    )
  {
%>
	    <table style="width: 95%;">
	      <tr>
	        <td colspan=3 class="Titolonocap">Pena complessiva</td>
	      </tr>
	      <tr>
	        <td class="l">
	          <font class="campo">
	            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
	          </font>
	        </td>
	        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
	        <td class="l">Data Fine : <font class="campo">MAI</font></td>
	      </tr>
	    </table>
<%
  }
  else if (!PenRes1.getErrorMsg().equals("-"))
  {
%>
		  <table style="width: 95%;">
		    	<tr>
			 <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) 
			    {%>
			      		<td class="Titolonocap" colspan=6 width=80%> Pena complessiva </td>
			 <% } 
			    else
			    {%>
			      		<td class="Titolonocap" colspan=6 width=80%> Pena residua da espiare </td>
			 <% } %>
		    	</tr>
<br>
		    <% if (PenRes1.getErrorMsg().startsWith("Libero")) 
		       { %>
						<tr>
					      <td class="l"> Reclusione :
					        Anni   <font class=campo><%=PenRes1.getNumAnni()%></font>
					        Mesi   <font class=campo><%=PenRes1.getNumMesi()%></font>
					        Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>&nbsp;&nbsp;
					        Multa  <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>&nbsp;
					      </td>
					      <td class="l"> Arresto :
					        Anni    <font class=campo><%=PenRes2.getNumAnni()%></font>
					        Mesi    <font class=campo><%=PenRes2.getNumMesi()%></font>
					        Giorni  <font class=campo><%=PenRes2.getNumGiorni()%></font>&nbsp;&nbsp;
					        Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>&nbsp;
					        
					      </td>
					    </tr>
      
		      <% 		if(LAConcesse.compareTo(new BigDecimal(0)) != 0) 
		      			{ %>
						      <tr>
						        <td class="l" colspan="9">
						          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
						          <font class="campo"><%=LAConcesse%></font>
						        </td>
						      </tr>
		      <% 		} 
		       			
		      			if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) 
		       			{ %>
						      <tr>
						        <td class="l" colspan="9">
						          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
						          <font class="campo"><%=LADaConcedere%></font>
						        </td>
						      </tr>
		      <% 		} 

    }
    else //non libero
    {
      //========================================================================
      // Visualizzo la pena residua calcolata al volo tra la data odierna e la
      // data fine pena prevista
      //========================================================================
	      CalendarUtil lCU = new CalendarUtil();
	      PenRes2.setDataFine   (PenRes1.getDataFine());
	      PenRes2.setDataInizio (DateUtils.getSysDate());
	      PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
	      
	      String lMulta = StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()));
	      String lAmmenda = StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()));
    %>
    		
		  <tr>
		     <td class="l">
		        Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
		        Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
		        Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>&nbsp;&nbsp;&nbsp;
		        
	<%			if( lMulta != "" &&
				    lMulta != null &&
				    !lMulta.startsWith("0"))	
				{%>	        
		        		Multa  <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>&nbsp;
		     <% } 
		     
		    	if( lAmmenda != "" &&
		    		lAmmenda != null &&
		    		!lAmmenda.startsWith("0"))	
				{%>	        
		        		Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
		     <% } %>
        
		     </td>
		     <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td class=l>Data Inizio : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%> </font></td>
		     <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		     <td class=l>Data Fine : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy") %> </font></td>
		  </tr>
		  
	   <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) 
	   	  { %>
	      		<tr>
	        		<td class="l" colspan="9">
	          			<font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
	          			<font class="campo"><%=LAConcesse%></font>
	        		</td>
	      		</tr>
      <% } 
	   
         if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) 
         { %>
			      <tr>
			        <td class="l" colspan="9">
			          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
			          <font class="campo"><%=LADaConcedere%></font>
			        </td>
			      </tr>
      <% } 
    
    } %>
    
	</table>

<%
  }  // Fine Pena Residua
%>

<%
//==============================================================================
// Sezione con i dati del provvedimento
//==============================================================================
%>

<table style="width: 95%;">
  <tr>
    <td class="Titolo">Rideterminazione della pena</td>
  </tr>
</table>

<table style="width: 95%;">  
  <tr>
    <td class="l" colspan=10 > 
      <input type="radio" name="TipoOrd" value="dufficio" checked onClick="javascript:radioBase();">D'ufficio &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="altroUfficio" onClick="javascript:radioBase();">In esecuzione di provvedimento altro ufficio &nbsp;&nbsp;
    </td>
  </tr>
  
  <tr id="OggettoProvvedimentoDufficio" style="display:block">
    	<td class="l" colspan=3>Oggetto Comunicazione:&nbsp;</td>
    	<td class="campo" colspan=7>Pagamento Pena Pecuniaria</td>
   
  </tr>
  
  <tr>
    <td class="l" colspan=2 >Note :&nbsp;</td>
    <td class="l" colspan=8><textarea cols="100" rows="2" name="noteComputo"></textarea></td>
  </tr>
</table>

<%
//==============================================================================
// Div per la visualizzazione della sezione con gli estremi del Provvedimento
// Altra Autorità
// n.b. se presente verrà inserito un evento apposito
//  !!!!!! Attenzione!!!! Per ora non vengono acquisiti. Da rinominare le costanti
//==============================================================================
%>
<div id="DivAltraAutorita" style="display:none">
  <table style="width: 95%;">
    <tr>
      <td class="titolo" colspan="10" width="95%">Dati Provvedimento Altra Autorità</td>
    </tr>

    <tr>
      <td class="l" colspan="3" width="30%">Data ricezione Comunicazione </td>
      <td class="l" colspan="2" width="20%">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" 
        	name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>"   
        	name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" 
        	name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>" maxlength="4" size="4"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td class="l" colspan="3" width="30%">Data emissione Comunicazione</td>
      <td class="l" colspan="2" width="20%">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" 
        	name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA" maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>"   
        	name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA"   maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" 
        	name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA"   maxlength="4" size="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
     </tr>
     
     <tr> 
      <td class="l" colspan="4" width="40%">Anno / Numero Partita di Credito<font class="ob">(*)</font></td>
      <td class="l" colspan="4" width="40%">
         <input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
      <%  // Campo input a testo libero per Num. EX Campione Penale %>
      <td class="l" colspan="2" width="20%">
      	<input type="text" maxlength="20" size="20" name="<%=ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE %>">
	  </td>

    </tr>

    <tr>
      <td class="l" colspan="3" width="30%">Oggetto Provvedimento :</td>
      <td class="campo" colspan="7" width="70%">Pagamento Pena Pecuniaria</td>
   </tr>

    <tr>
      <td class="l" colspan="3" width="30%">Ufficio Recupero Crediti<font class="ob">(*)</font></td>
      <td class="l" colspan="5" width="50%">
        <select Title="Autorità Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>">
        <%=autoritaEsternaAltra%>
        </select>
      </td>
     </tr>
     
     <tr> 
      <td class="l" colspan="2" width="20%">Sede &nbsp;</td>
      <td class="l" colspan="4" width="40%">
        <input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE %>',document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
        <td class="l" colspan="2" width="20%"> Sezione &nbsp;</td> 
        <td class="l" colspan="2" width="20%"><input type="text" maxlength="20" size="20"
		name="<%=ICostantiEvento.CAMPO_COGNOME_SOGGETTO_PRESENTANTE %>">
	  </td>
      
    </tr>

  </table>
</div>

<%
//==============================================================================
//        Sezione per specificare i quantum di rideterminazione
// n.b. è possibile inserire più quantum
//==============================================================================
%>
<table style="width: 95%; border: 0;">

        		<tr>
      				<td colspan=10 class="titolo">Pena Pecuniaria</td>
    			</tr>
</table>

<table style="width: 95%; border: 0;"> 
  <% for (int i=0;i<maxNumComputi;i++ ) 
  { 
   		if (i==0) 
  		{ %>
  				<tr style="display:block" id="annotazione_<%=i%>">
<%		}
  		else
  		{ %>
  				<tr style="display:none" id="annotazione_<%=i%>">
<%		} %>

    	<td>
      		<table width="100%">

        		<tr>
          			<td valign="middle" class="c" width="10%">+/- <font class="ob">(*)</font><br>
            			<select name="PM_<%=i%>" id="PM_<%=i%>">
              				<option value=""></option>
              				<option value="+">+</option>
              				<option value="-">-</option>
            			</select>
          			</td>

		          <td class="c"	width="40%">
		            	<font class="label">Multa</font>
		            		<input style="align:right" type="text" name="Multa_<%=i%>" id="Multa_<%=i%>" maxlength="8" size="6" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		            		,
		            		<input style="align:right" type="text" name="Mul_dec_<%=i%>" id="Mul_dec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		          </td>

		          <td class="c" width="40%">
		            	<font class="label">Ammenda</font>
		            		<input style="align:right" type="text" name="Ammenda_<%=i%>" id="Ammenda_<%=i%>" maxlength="8" size="6" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		            		,
		            		<input style="align:right" type="text" name="Amm_dec_<%=i%>" id="Amm_dec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		          </td>
		          
    <% if (i==0) 
       { %>
          	<td valign="middle" class="c"  id="tdCancella_<%=i%>" width="10%">&nbsp;</td>
    <% }
       else
       { %>
          	<td valign="middle" class="c"  id="tdCancella_<%=i%>" width="10%">&nbsp;</td>

    <% } %>		          
		          
        		</tr>

		        <tr>
		          <td class="c" colspan="1"><font class="label">&nbsp;</font></td>
		          <td class="c" colspan="1"><font class="label">Motivazioni:</font></td>
		          <td class="c" colspan="1"><textarea cols="60" rows="2" name="motivazioni_<%=i%>"></textarea></td>
		        </tr>
		        
		        <tr><td colspan="100%" class="titolo"><hr width="100%"></td></tr>
		        
      		</table>
    	</td>
  	</tr>
  <% } %>
  
	  <tr>
	    <td class="l" colspan="100%">
	      <a href="Javascript:addQuantum('f');">
	        Aggiungi ulteriore computo
	      </a>
	    </td>
	  </tr>
</table>



<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" 
      		 type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" 
             type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" 
             type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>

  <tr>
  	 <td class="l">Firmatario</td>
     <td class="l">
      	<input checked type="radio" name="TipoFir"  value="magistrato"  onClick="visFirmatario();">Magistrato&nbsp;&nbsp;
      	<input type="radio" name="TipoFir"  value="funzionario" onClick="visFirmatario();">Funzionario &nbsp;&nbsp;
     </td>
     
	<td>
		<div id="nomeMag" style="display:none; position:relative;">
			<table style="width: 95%; border:0;padding:0">
				<tr>  
				   <td class="L">
				      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
				      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
				      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
				      	<a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
			        		<img src="/images/filefolder.gif" border=0>
			      	  	</a>
			       </td>	
			    </tr>  	
			</table>
		</div>	
		
		<div id="nomeFunc" style="display:none; position:relative;">
			<table style="width: 95%; border:0;padding:0">
				<tr>  
				   <td class="L">
								<input readonly title="Cognome Funzionario" value="<%=StringUtils.toStringJSP(UtenteConnesso.getCognome().toUpperCase())%>" type="text" name="CognomeFunzionario" maxlength="35" size="25">
								<input readonly title= "Nome Funzionario" value="<%=StringUtils.toStringJSP(UtenteConnesso.getNome().toUpperCase())%>" type="text" name="NomeFunzionario" maxlength="35" size="25">
								<a href="Javascript:ListaFunzionari('f','CognomeFunzionario','NomeFunzionario');"> 
			        		<img src="/images/filefolder.gif" border=0>
			      	  	</a>
			       </td>	
			    </tr>  	
			</table>
		</div>	
	</td>
  </tr>
</table>  
<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>