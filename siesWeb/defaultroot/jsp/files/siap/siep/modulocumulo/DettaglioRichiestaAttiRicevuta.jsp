<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio" %>
<%@ page import="siap.jms.messaggio.model.MessaggioModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>


<jsp:useBean id="Messaggio"        scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="dettaglioFasSIEP" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="Competenza"       scope="request" class="siap.siep.competenza.model.CompetenzaModel" />


<jsp:useBean id="alertMsg" scope="request" class="java.lang.String" />

<jsp:useBean id="isErrParser"      scope="request" class="java.lang.String" />

<jsp:useBean id="UfficioAccorpatoOrigine" scope="request" class="siap.sico.ufficio.model.UfficioAccorpatoModel" />
<jsp:useBean id="UfficioOld" scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<jsp:useBean id="MessTrasmissione"        scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<% 
FascicoloSiepModel fascicoloSIEP  = null;
SoggettoModel soggettoRicevuto    = null;
SentenzaModel sentenzaRicevuta    = null;

if ("SI".equals(isErrParser)) { 
  fascicoloSIEP    = new FascicoloSiepModel();
  fascicoloSIEP.setChiaveAnno         (Messaggio.getChiaveAnnoFasCumulante());
  fascicoloSIEP.setChiaveProgr        (Messaggio.getChiaveProgrFasCumulante());
  fascicoloSIEP.setDescrTipoUfficio   (Messaggio.getDescrUfficioFasCumulante());
  fascicoloSIEP.setDescrComuneUfficio (Messaggio.getDescrSedeUfficioFasCumulante());
  
  Competenza.setChiaveAnno  (Messaggio.getChiaveAnnoSiep());
  Competenza.setChiaveProgr (Messaggio.getChiaveProgrSiep());
  
  
  soggettoRicevuto = new SoggettoModel();
  sentenzaRicevuta = new SentenzaModel();
} else {  
  fascicoloSIEP    = dettaglioFasSIEP.getFascicoloSiep();
  soggettoRicevuto = fascicoloSIEP.getSoggetto();
  sentenzaRicevuta = fascicoloSIEP.getSentenza();

}
  
%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Atto Ricevuto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >
    
    
    function ricercaProcedimenti(){
   	  var desktop;
      var stringaChiamata = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActRicercaProcedimentiPerTitoloSoggetto";
      stringaChiamata += "&formname=DettaglioRichiesta";
      stringaChiamata += "&CampoAnno=<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>";
      stringaChiamata += "&CampoProgr=<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>";
      
      //stringaChiamata += "&"++"="+;
      
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=Competenza.getCodTipoProvvedimento()%>";
      <% if (Competenza.getAnnoSentenza()!=null) { %>
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>=<%=Competenza.getAnnoSentenza()%>";
      <% } %>
      <% if (Competenza.getNumeroSentenza()!=null) { %>
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>=<%=Competenza.getNumeroSentenza()%>";
      <% } %>

      <% if (Competenza.getCodTipoAutoritaEmittente()!=null) { %>
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>=<%=Competenza.getCodTipoAutoritaEmittente()%>";
      <% } %>

      <% if (Competenza.getDescrLuogoEmittente()!=null) { %>
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>=<%=Competenza.getDescrLuogoEmittente()%>";
      <% } %>
      
      <% if (Competenza.getDataProvvedimento()!=null) { %>
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>=<%=DateUtils.getDateToString(Competenza.getDataProvvedimento(),"dd")%>";
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>=<%=DateUtils.getDateToString(Competenza.getDataProvvedimento(),"MM")%>";
      stringaChiamata += "&<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>=<%=DateUtils.getDateToString(Competenza.getDataProvvedimento(),"yyyy")%>";
      <% } %>   
      
      <% if (Competenza.getDataIrrevocabilita()!=null) { %>
      stringaChiamata += "&<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>=<%=DateUtils.getDateToString(Competenza.getDataIrrevocabilita(),"dd")%>";
      stringaChiamata += "&<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>=<%=DateUtils.getDateToString(Competenza.getDataIrrevocabilita(),"MM")%>";
      stringaChiamata += "&<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>=<%=DateUtils.getDateToString(Competenza.getDataIrrevocabilita(),"yyyy")%>";
      <% } %> 
      
      <% if (soggettoRicevuto.getCognome()!=null) { %>
      stringaChiamata += "&<%=ICostantiSoggetto.CAMPO_COGNOME%>=<%=soggettoRicevuto.getCognome()%>";
      <% } %>
      
      <% if (soggettoRicevuto.getNome()!=null) { %>
      stringaChiamata += "&<%=ICostantiSoggetto.CAMPO_NOME%>=<%=soggettoRicevuto.getNome()%>";
      <% } %>
      
      <% if (soggettoRicevuto.getCodAfis()!=null) { %>
      stringaChiamata += "&<%=ICostantiSoggetto.CAMPO_COD_AFIS%>=<%=soggettoRicevuto.getCodAfis()%>";
      <% } %>
      
      var pupUp="S";
      if (pupUp=="S"){
        stringaChiamata+="&popUp=S";
        desktop = window.open(stringaChiamata, "Ricerca_Procedimenti"
                            , "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=900, height=500");
      }
      else {
        // no pop up 
        stringaChiamata+="&popUp=N";
        stringaChiamata+="&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=Messaggio.getIdMessaggio()%>";
        document.location.href=stringaChiamata;
      }
      
    }
    
    function Verify() { 
      if (typeof (document.DettaglioRichiesta.<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>)!="undefined")  
      {
        if (document.DettaglioRichiesta.<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>.value.length!=4)
        {
          alert("Indicare gli estremi del procedimento da trasmettere");
          document.DettaglioRichiesta.<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>.focus();
          return false;
        }
        
        if (document.DettaglioRichiesta.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.value=="")
        {
          alert("Indicare gli estremi del procedimento da trasmettere");
          document.DettaglioRichiesta.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.focus();
          return false;
        }        
      }
    
      return true;
    }
    
    function Trasmetti()
    {
   	 	document.DettaglioRichiesta.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciTrasmissioneCompetenza";
    }
    
    function Rigetta()
    {
   	 document.DettaglioRichiesta.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciRigettoRichiestaAtti";
    }
    
    </script> 
  </head>
  
<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio richiesta ricevuta&nbsp; </font>
        </td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
  </FORM>
  

  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="DettaglioRichiesta">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="" >
  <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>" >
  
  
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan="4">Oggetto della Trasmissione</td>
    </tr>
    
    <tr>
      <td class="l"><font class="label">Oggetto</font></td>
      <%-- 
      <td class="l"><font class="campo">Richiesta Atti per competenza (per emissione provvedimento di cumulo)</font></td>
      --%>
      
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoMessaggio())%>&nbsp;<%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Ricevuta in data</font></td>  
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></font></td>
    </tr>
     <tr>
      <td class="l"><font class="label">Inviata da</font></td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente())%> di <%=StringUtils.toStringJSP(Messaggio.getDescrSedeUfficioMittente())%></font></td>      
    </tr>    
    <tr><td>&nbsp;</td></tr>


  <% if ("SI".equals(isErrParser)) { %>
    <tr>
      <td class="L" colspan=4>
        <font color="red">Attenzione! Non è possibile visualizzare il Dettaglio degli atti ricevuti in quanto inviati con una versione SIES differente da quella attualmente in uso in questo Distretto.</font>
      </td>
    </tr>
  <% } %>

   <%
    //========================================================================
    // Estremi atti richiesti. Questi dati si trovano sul record Competenza
    //========================================================================
    %>
    <tr>
     <td class="Titolo" colspan="4">Titolo Richiesto</td>
    </tr>
    <tr>
      <td class="l">Tipo Provvedimento</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrTipoProvvedimento(),"&nbsp;")%>&nbsp;</font></td>
      <td class="l">Anno/Numero</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getAnnoSentenza())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(Competenza.getNumeroSentenza())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Provvedimento</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Competenza.getDataProvvedimento(),"dd-MM-yyyy"),"&nbsp;")%>&nbsp;</font></td>
      <td class="l">Data Irrevocabilità</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Competenza.getDataIrrevocabilita(),"dd-MM-yyyy"),"&nbsp;")%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l" width="25%">Pronunciata da</td>   
      <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrTipoAutoritaEmittente(),"&nbsp;")%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l">Luogo</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrLuogoEmittente(),"&nbsp;")%>&nbsp;</font></td>
      <td class="l">Sezione</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getNumSezioneAutoritaEmittente(),"&nbsp;")%>&nbsp;</font></td>
    </tr>
    
    <% 
    if (Competenza.getChiaveAnno()!=null && Competenza.getChiaveProgr()!=null) 
    {
      String lAccorpato = "";
      BigDecimal lProgressOrigine = Competenza.getChiaveProgr();
      if (   UfficioAccorpatoOrigine!=null 
          && UfficioAccorpatoOrigine.getCodUfficio()!=null
          && !UfficioAccorpatoOrigine.getCodUfficio().equals("")
         )
      {
        lProgressOrigine = lProgressOrigine.subtract(new BigDecimal (UfficioAccorpatoOrigine.getIncrProgressivo()));
     
     
        lAccorpato += "  <font class=\"cRosso\">(Ex "+StringUtils.toStringJSP(UfficioOld.getDescrTipoUfficio());
        lAccorpato += " di "+StringUtils.toStringJSP(UfficioOld.getDescrComune());
        lAccorpato += " ) </font>";
      }

    %>
    <tr> 
      <td class="l" width="25%">Iscritto al Procedimento N. </td>
      <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getChiaveAnno(),"&nbsp;")%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(lProgressOrigine,"&nbsp;")%></font><%=lAccorpato%></td>
    </tr>
    
    <%  } else if(MessTrasmissione!=null && MessTrasmissione.getIdMessaggio()!=null ) {	%>

	<tr> 
      <td class="l" width="25%">Iscritto al Procedimento N. </td>
      <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(MessTrasmissione.getChiaveAnnoSiep(),"&nbsp;")%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(MessTrasmissione.getChiaveProgrSiep(),"&nbsp;")%></font></td>
    </tr>
    
    <% } else { %>
    <tr>
      <td class="L"><font class="label">Iscritto al Procedimento N. </font> </td>
      <td class="L" colspan="3">
        <font class="cRosso">L'ufficio richiedente non ha indicato gli estremi del procedimento di esecuzione. Per poter procedere alla trasmissione telematica è necessario indicare gli estremi negli appositi campi o ricercarlo utilzzando il tasto Ricerca</font>
        <!--font class="cRosso">Non indicato nella richiesta. Indicare il numero di procedimento da trasmettere o ricercarlo utilzzando il tasto Ricerca</font>&nbsp;  -->
        <br>
        Anno/Numero SIEP&nbsp;
        <input type="text" title="Anno" value="" name="<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>" maxlength="4" size="4" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero SIEP" name="<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">

         <input type="button" class="bottone" name="associa" value="Ricerca" 
               onclick="Javascript:ricercaProcedimenti();">
      </td>
    </tr>
      <!--
      td class="L" colspan="3"><font class="cRosso">Attenzione! L'ufficio richiedente non ha indicato gli estremi del procedimento di esecuzione del titolo richiesto</font></td>
      --> 
    <% } %>
    
    <%
    //========================================================================
    // Sentenza dell'ufficio che effettua la richiesta
    // Questi dati si trovano sul record sentenzaRicevuta
    //========================================================================
    %>
    <tr>
     <td class="Titolo" colspan="4">Titolo Che Determina La Competenza</td>
    </tr>
    <tr>
      <td class="l">Tipo Provvedimento</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getDescrTipoProvvedimento())%></font>
        N. 
        <font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(sentenzaRicevuta.getNumeroSentenza())%></font>
        del 
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenzaRicevuta.getDataProvvedimento(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Pronunciata da</td>   
      <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getDescrTipoAutoritaEmittente())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Luogo</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getDescrLuogoEmittente())%></font>&nbsp;</td>
      <td class="l">Sezione</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getNumSezioneAutoritaEmittente(),"")%></font>&nbsp;</td>
    </tr>
    <%
    //========================================================================
    // Dati del procedimento che determina la compentenza 
    // Questi dati si trovano sul record Fascicolo SIEP
    //========================================================================
    %>
    <%--
    <tr>
      <td class="Titolo" colspan="4">Ufficio Competente all'emissione del Provvedimento di Cumulo</td>
    </tr>
    <tr>
      <td class="l">Ufficio del Pubblico Ministero</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(fascicoloSIEP.getDescrTipoUfficio(),"&nbsp;")%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(fascicoloSIEP.getDescrComuneUfficio(),"&nbsp;")%></font>
      </td>
    </tr>
    --%>
    <tr>
      <td class="l" width="25%">Iscritto al Procedimento N.</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(fascicoloSIEP.getChiaveAnno(),"&nbsp;")%>/<%=StringUtils.toStringJSP(fascicoloSIEP.getChiaveProgr(),"&nbsp;")%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(fascicoloSIEP.getDescrTipoUfficio(),"&nbsp;")%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(fascicoloSIEP.getDescrComuneUfficio(),"&nbsp;")%></font>
      </td>
    </tr>
    
    <tr>
      <td class="Titolo" colspan="4">A carico di</td>
    </tr>
    <%
    String lStringaSoggetto = "";
    
    lStringaSoggetto += "<font class='campo'>"
                       +StringUtils.toStringJSP(soggettoRicevuto.getCognome())+" "
                       +StringUtils.toStringJSP(soggettoRicevuto.getNome())+"</font>&nbsp;";
    
    if (soggettoRicevuto.getSesso().compareTo("F")==0)
      lStringaSoggetto += "<font class='label'>nata il :</font>&nbsp;";
    else 
      lStringaSoggetto += "<font class='label'>nato il :</font>&nbsp;";

    // Data Nascita
    if (soggettoRicevuto.getDataNascita()!=null)
      lStringaSoggetto += "<font class='campo'>"+StringUtils.toStringJSP (DateUtils.getDateToString (soggettoRicevuto.getDataNascita(),"dd-MM-yyyy"))+"</font>";
    else if (soggettoRicevuto.getDataNascitaPresunta().equals("S"))
      lStringaSoggetto += "<font class='campo'>"+StringUtils.toStringJSP (soggettoRicevuto.getAnnoNascita())+"</font>";
    else 
      lStringaSoggetto += "<font class='campo'>***</font>";


    // Comune nascita
    lStringaSoggetto += "<font class='label'>&nbspin: &nbsp;</font>";
    lStringaSoggetto += "<font class='campo'>";
    if (soggettoRicevuto.getDescrComuneNascita().compareTo("-")==0)
      lStringaSoggetto += "&nbsp;"+soggettoRicevuto.getDescComuneNascitaEstero()+"&nbsp;("+soggettoRicevuto.getDescrStatoNascita().toUpperCase()+")";
    else
      lStringaSoggetto += "&nbsp;"+soggettoRicevuto.getDescrComuneNascita()+"&nbsp;("+soggettoRicevuto.getCodProvinciaNascita()+")";
    lStringaSoggetto += "</font>";
    
    
    lStringaSoggetto += "&nbsp;<font class='label'>Codice CUI : </font>"
                       +"<font class='campo'>"+StringUtils.toStringJSP(soggettoRicevuto.getCodAfis())+"</font>";

    %>
    <tr>
      <td class="l">Soggetto</td>
      <td class="L" colspan="3"><%=lStringaSoggetto%></td>
    </tr>
    
</table>
<br>    
     <%
    //=====================================
    // Eventuali solleciti pervenuti
    //=====================================
    %>
   <%if(Messaggio.getMessaggiSollecito()!=null && Messaggio.getMessaggiSollecito().size()>0)
   	 {	%>
   	 	<table cellspacing=2 cellpadding=2 width="95%">
    	<tr>
     	  <td class="Titolo" colspan="4"> Solleciti Pervenuti </td>
    	</tr>
   <%	Vector VecSoll = Messaggio.getMessaggiSollecito(); 
        Iterator itxSoll = VecSoll.iterator();
        while ( itxSoll.hasNext())
        {
         	  MessaggioModel lMessSoll = (MessaggioModel) itxSoll.next();
         	  if(lMessSoll.getDataInvio()!=null )
         	  {	 %> 	
		 		<tr>
		      	  <td class="l" width="15%">Sollecito Pervenuto il </td>
		      	  <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy"),"&nbsp;")%>&nbsp;</font></td>
		      	</tr>
		      	<tr>  
		      	  <td class="l" ><center>Note</center></td>
		      	  <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(lMessSoll.getNote(),"&nbsp;")%>&nbsp;</font></td>
		    	</tr>
  <% 		  }
        }	%>
        </table>
        <br>
 <%  }    %>  
     
    <% if (!"".equals(alertMsg)) {%>
    <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="l" colspan="3"><font class="cRosso"><%=alertMsg%></font></td>
    </tr>
    </table>
    <% } %>

  
  <%if ("".equals(alertMsg))
  	{	
  		if(Messaggio.getCodEsito()!=null && 
  			Messaggio.getCodEsito().compareTo("01006")!=0 &&
  			Messaggio.getCodEsito().compareTo("01007")!=0 ) 
  		{	%>
	  		<table width="40%">
	  		 <tr>	
				<td align="center"  class="tab2" >  
			  		<input class="bottone" name="Trasmissione" value="Trasmissione" type="submit" onclick="javascript:Trasmetti();">
			  	</td>
			  	<td>&nbsp;</td>	
			  	<td align="center" class="tab2" >
			  		<input class="bottone" name="Rigetto" value="Rigetto Richiesta" type="submit" onclick="javascript:Rigetta();">
			  	</td>	
		 	 </tr>
			</table>
<% 		}
  	}	%>
</form>
  
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("DettaglioRichiesta");
  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script> 
<%// } // end isErrParser%>
</body>
</html>