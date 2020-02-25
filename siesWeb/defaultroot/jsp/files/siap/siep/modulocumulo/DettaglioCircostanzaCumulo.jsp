<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.modulocumulo.model.CircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiCircostanzaCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="org.apache.log4j.Logger"%>

<jsp:useBean id="IstruttoriaCumulo" 		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="circostanza" 	scope="request" class="siap.siep.modulocumulo.model.CircostanzaCumuloModel"/>
<jsp:useBean id="modo"       	scope="request" class="java.lang.String"/>

<!--  	DettaglioCircostanzaCumulo 		-->

<html>
<head>
<title>[S.I.E.S.] - Gestione Circostanze Aggravanti/Attenuanti Cumulo - Dettaglio CircostanzaReato </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
//==========================================================================
// Richiama Inserimento Circostanza_Cumulo 
//==========================================================================
function VaiadInserire(action)
{
	  document.DettaglioCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
	  document.DettaglioCircosCumulo.modalita.value="I";
	  document.DettaglioCircosCumulo.submit();
}

//==========================================================================
//Richiama Modifica Circostanza_Cumulo 
//==========================================================================
function VaiaModificare(action,aIdCirco)
{
	  document.DettaglioCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
	  document.DettaglioCircosCumulo.modalita.value="M";
	  document.DettaglioCircosCumulo.submit();
}

//==========================================================================
//Richiama Cancellazione Logica o Fisica Circostanza_Cumulo 
//==========================================================================
function VaiaCancellare(action, aIdCirco, aStato, aMotivoModifica)
{
  if (aStato=='E' || aStato=='M')
  {
    // Cancellazione Logica Richiedo Motivazione
  	document.DettaglioCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    
      document.DettaglioCircosCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
      document.DettaglioCircosCumulo.modalita.value="C";
      var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                   + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaglioCircosCumulo"
                                   + "&" + "<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                   , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
      window.parent.close();
        
        // N.B. la submit viene effettuare direttamnete dalla finestra di popup
  }
  else if (aStato=='I')
  {
    // Cancellazione fisica richiedo conferma
      var retValue = true;
      retValue = confirm("Si vuole procedere con la cancellazione dei dati?"); 
      if (retValue) 
      {
          document.DettaglioCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

          document.DettaglioCircosCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
          document.DettaglioCircosCumulo.modalita.value="C";
          document.DettaglioCircosCumulo.submit();
      }

  }
  
}

//==========================================================================
// Ritorna all elenco dei reati/Circostanze Aggravanti (torna Indietro)
//==========================================================================
function eseguiFunzione(action)
{
    document.DettaglioCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.DettaglioCircosCumulo.submit();
}

</script>  
</head>
<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Aggravanti soggettive/Attenuanti relative al titolo cumulato</font>
        </td>
<% 		if( IstruttoriaCumulo.getFlagStato().equals("A") )
		{ %>   
      	  <td class="LBG">
            <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciCircostanzaCumulo')">
          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
<% 			if(!circostanza.getFlagStato().equals("C"))
        	{ %>   
        	  <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadModificaCircostanzaCumulo', <%=circostanza.getIdCircostanzaCumulo()%>)">
         		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        	  <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActCancellaCircostanzaCumulo', <%=circostanza.getIdCircostanzaCumulo()%>, '<%=circostanza.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(circostanza.getMotivoModifica()),"") %>')">
         		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
<% 			} %>
          </td>
<% 		} %>
 
  <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <!--  a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo')" -->
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>  
 
 <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
        </td>
    </tr>
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
        </td>
    </tr>
  </table>
  <br>
 
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioCircosCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>"  value="">
  
    <!-- Campi sempre presenti sulle form dei dati analitici -->
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  	<input type="hidden" name="<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>" value="<%=circostanza.getIdCircostanzaCumulo() %>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>" value="<%=circostanza.getFlagStato() %>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" value="" > 
   
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l" colspan="2">
        <font class="campo">
<%
          boolean lFlagAnnoNumero = false;
          if( circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().equals("")
              && circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("") && !circostanza.getDescrFonte().equals("-"))
              out.println(circostanza.getDescrFonte()+" ");
            if(circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().equals(""))
              out.println(circostanza.getAnnoFonte());
            if(circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals(""))
              out.println("/"+circostanza.getNumeroFonte());
          }

          if(circostanza.getArticolo() != null && !circostanza.getArticolo().equals(""))
            out.println("art."+circostanza.getArticolo());
          if(circostanza.getDescrSottonumerazione() != null && !circostanza.getDescrSottonumerazione().equals("") && !circostanza.getDescrSottonumerazione().equals("-"))
            out.println(" "+circostanza.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("") && !circostanza.getDescrFonte().equals("-"))
              out.println(circostanza.getDescrFonte());
          }

          if(circostanza.getComma() != null && !circostanza.getComma().equals(""))
            out.println(" c. "+circostanza.getComma());
          
          if(circostanza.getDescrCommaQualificante() != null && !circostanza.getDescrCommaQualificante().equals("") && !circostanza.getDescrCommaQualificante().equals("-"))
            out.println(" "+circostanza.getDescrCommaQualificante());
           
          if(circostanza.getLettera() != null && !circostanza.getLettera().equals(""))
            out.println(" l. "+circostanza.getLettera());
          if(circostanza.getNumero() != null && !circostanza.getNumero().equals(""))
            out.println(" n. "+circostanza.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
 
  <br>
      <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="l">Sentenza di applicazione pena</td>
        <td class="l">
          <font class="campo">
   <%	if(circostanza.getFlagSentenzaApplicazPena()!=null && 
  			circostanza.getFlagSentenzaApplicazPena().equals("S") )
  		{  %>        
              <img align="middle" src="/images/V.gif" border="0">
   <%	}
  		else
  		{ %>
  			&nbsp;&nbsp;-&nbsp;&nbsp;
  	<%	} %>			           
          </font>&nbsp;
        </td>
      </tr>
      
      <tr>
        <td class="l">Bilanciamento circostanze</td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(circostanza.getDescrBilanciamentoCircostanze(), "&nbsp; - &nbsp;")%></font>&nbsp;
        </td>
      </tr>
      
      <tr>
 		<td class="l">Annotazioni Bilanciamento circostanze</td>
 		<td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(circostanza.getNoteBilanciamento(), "&nbsp; - &nbsp;")%></font>
        </td>
 	  </tr>
      
      <tr>
        <td class="l">Giudizio abbreviato</td>
        <td class="l">
          <font class="campo">
   <%	if(circostanza.getFlagGiudizioAbbreviato()!=null && 
  			circostanza.getFlagGiudizioAbbreviato().equals("S") )
  		{  %>        
              <img align="middle" src="/images/V.gif" border="0">
   <%	}
  		else
  		{ %>
  			&nbsp;&nbsp;-&nbsp;&nbsp;
  	<%	} %>
          </font>&nbsp;
        </td>
      </tr>
    </table>
    <br>
    
<!-- 	Descrizione dello stato del dato Analitico  -->

  <%    String lStato = "";
    	String lDescStato = "";
        if      ( circostanza.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( circostanza.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( circostanza.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( circostanza.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
    %>
    <table cellspacing=2 cellpadding=6 width=95%>
       <tr>
        <td class="l" width="25%">Stato</td>
        <td class="l">
          <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
        </td> 
      </tr>     
   
 <% //================================================================================================
 	// Descrizione del Motivo Inserimento/Modifica visualizzato solo in fase di modifica del dato   
    if(circostanza.getMotivoModifica()!= null && !circostanza.getMotivoModifica().equals("") )
    {
   	 	if(circostanza.getFlagStato().equals("I") || circostanza.getFlagStato().equals("M") )
   	 	{	
%>
	      <tr>
	        <td class="l" colspan=1 width=20%><font class="label">Motivo Inserimento/Modifica </font></td>
	        <td class="l" colspan=5 width=80%><font class="campo"><%=StringUtils.toStringJSP(circostanza.getMotivoModifica())%></font></td>
	      </tr>
   
<%  	}
   	}		%> 

   </table>
    
</FORM>  
</body>
</html>