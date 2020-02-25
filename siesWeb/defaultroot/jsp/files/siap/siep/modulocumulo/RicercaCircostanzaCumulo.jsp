<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiCircostanzaCumulo" %>
<%@ page import="siap.siep.modulocumulo.model.CircostanzaCumuloModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="circostanzaCum"	scope="request" class="java.util.Vector" />

<%
//==================================================================================
//						 RicercaCircostanzaCumulo.jsp
//Form per la Ricerca e visualizzazione delle CIRCOSTANZE (Aggravanti e Attenuanti)
//legate a un certo Titolo.(CUMULO)
//
//La form presenta un elenco di CIRCOSTANZE già presenti con la possibilità di 
//modificarle, cancellarle o inserirne di nuove 
//==============================================================================

FascicoloSiepModel lFascicolo = null;
lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

String flagSentenzaApplicazPena = null;
String descrBilanciamentoCircostanze = null;
String flagGiudizioAbbreviato = null;
String noteBilanciamento = null;
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>{S.I.E.S.] - Ricerca Circostanza Cumulo</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaCircosCumulo.submit();
    }
    
  	//==========================================================================
    // Visualizza, nasconde i record con le note del motivo inserimento/modifica
    // della circostanza  
    //==========================================================================
    function visualizzaMotivo(idRecord)
    {
      var rigamot = document.getElementById(idRecord);  
      if (rigamot.style.display =="none" )
      {
        rigamot.style.display = "block";
      }
      else 
      {
        rigamot.style.display = "none";
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaCircosCumulo()
    {
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciCircostanzaCumulo";
        document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.RicercaCircosCumulo.submit();
    }
    
    //==========================================================================
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdCirco, aStato, aMotivoModifica)
    {
        if (aTipoAzione=='Dettaglio')
        {
          lAzione = "siap.siep.modulocumulo.action.ActDettaglioCircostanzaCumulo";
          document.RicercaCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>.value = aIdCirco;
          document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaCircosCumulo.submit();
        }
        else if (aTipoAzione=='Modifica')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadModificaCircostanzaCumulo";
          document.RicercaCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>.value = aIdCirco;
          document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaCircosCumulo.submit();
        }
        else if (aTipoAzione=='Cancella')
        {
            if (aStato=='E' || aStato=='M')
            {
                // Cancellazione Logica Richiedo Motivazione
                lAzione = "siap.siep.modulocumulo.action.ActCancellaCircostanzaCumulo";
                document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                document.RicercaCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO%>.value = aIdCirco;
                document.RicercaCircosCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO %>.value = aStato;
                var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"RicercaCircosCumulo"
                                     + "&" + "<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
                
                window.parent.close();
          
                // N.B. la submit viene effettuata direttamnete dalla finestra di popup
            }
            else if (aStato=='I')
            {
                // Cancellazione fisica richiedo conferma
                var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
                if (window.confirm(msgConfirm)) 
                {
                  lAzione = "siap.siep.modulocumulo.action.ActCancellaCircostanzaCumulo";
                  document.RicercaCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                  document.RicercaCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO%>.value = aIdCirco;
                  document.RicercaCircosCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
                  document.RicercaCircosCumulo.submit();
                }
            }
        
          } // Chiude Elsif aTipoAzione=='Cancella
      
    } // Chiude  function eseguiAzione
    
    </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Elenco Aggravanti soggettive/Attenuanti relative ad un Titolo Cumulato</font></td>
	  
	  <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
      	<a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>     

    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
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
	<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="RicercaCircosCumulo">
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  	<input type="hidden" name="<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>" value="">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>" value="">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" value="" >
 	  
  <br>
  <table cellspacing="1" cellpadding="2" width="95%">
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Articolo Qualificante</td>
      <td class="int">Comma</td>
      <td class="int">Comma Qualificante</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
	  <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <td class="int">Azioni</td>

    </tr>
<%
  if (circostanzaCum == null || circostanzaCum.size()==0)
  { %>
	<tr>
	  <td align="left">  Nessun dato presente  </td>
	</tr>
<%}
  else
  {	 
	int id_record = 0;
	Iterator itx = circostanzaCum.iterator();
	while ( itx.hasNext())
	{
	  id_record = id_record +1;
	    
	  String lStato = "";
	  String lDescStato = "";
	  String lFontColor = "";
	
	  CircostanzaCumuloModel CiReato = (CircostanzaCumuloModel)itx.next();
	  
	  flagSentenzaApplicazPena 		= CiReato.getFlagSentenzaApplicazPena();
	  descrBilanciamentoCircostanze = CiReato.getDescrBilanciamentoCircostanze();
	  noteBilanciamento 			= CiReato.getNoteBilanciamento();
	  flagGiudizioAbbreviato 		= CiReato.getFlagGiudizioAbbreviato();
	      
	  if      (CiReato.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	  else if (CiReato.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	  else if (CiReato.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	  else if (CiReato.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getComma(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrCommaQualificante(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumero(),"-")%></td>

<% 	if (CiReato.getMotivoModifica()!=null && CiReato.getMotivoModifica().length()>0)
	{ %>
      <td class="c"> 
          <a href="javascript:visualizzaMotivo('record_<%=id_record%>')" title="<%=lDescStato%>">
              <%=lStato%>
          </a>
      </td>    
<%  }
	else
	{ %>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
<%  } %>

	  
      <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
        //======================================================================
      %>
      <td class="c" style="text-align:left" nowrap> &nbsp;
        <a href="javascript:eseguiAzione('Dettaglio',<%=CiReato.getIdCircostanzaCumulo() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
  
      <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================        
      if(IstruttoriaCumulo.getFlagStato().equals("A") && !CiReato.getFlagStato().equals("C"))
      { %>
        <a href="javascript:eseguiAzione('Modifica',<%=CiReato.getIdCircostanzaCumulo() %>)">
            <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=CiReato.getIdCircostanzaCumulo() %>,'<%=CiReato.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(CiReato.getMotivoModifica()),"") %>')">
            <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
  <%  } %>
      </td>
    </tr>
    
    <!-- Record Hidden con le motivazioni -->
    <tr style="display:none" id="record_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(CiReato.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr> 
  
<% } // Chiude while ( itx.hasNext()) %>
  	</table>
  	
    <table cellspacing="2" cellpadding="2" width="95%">
      <tr>
        <td class="l">Sentenza di applicazione pena</td>
        <td class="l">
          <font class="campo">
<% 	if(flagSentenzaApplicazPena != null && flagSentenzaApplicazPena.equals("S"))
	{ %>
          	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%  }
	else
	{	%>
			&nbsp; - &nbsp;
<%	} %>			
          </font>&nbsp;
        </td>

        <td class="l">Bilanciamento circostanze</td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(descrBilanciamentoCircostanze)%>
          </font>&nbsp;
        </td>

		<td class="l">Giudizio abbreviato</td>
        <td class="l">
          <font class="campo">
<%	if(flagGiudizioAbbreviato != null && flagGiudizioAbbreviato.equals("S"))	            
    {	%>
          	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%  }
	else
	{	%>
			&nbsp; - &nbsp;
<%	} %>
          </font>&nbsp;
        </td>
	  </tr>
	  <tr>
 		<td class="l">Annotazioni Bilanciamento circostanze</td>
 		<td class="l" colspan="5">
          <font class="campo"><%=(noteBilanciamento==null)? "&nbsp;" : noteBilanciamento%></font>
        </td>
 	  </tr>
    </table>
    <br>

<% } // Chiude la else di if (circostanzaCum == null || circostanzaCum.size()==0)  %>

<% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  	<table cellspacing="2" cellpadding="2" width="90%">
  	  <tr>
      	<td>
        	<INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuova Circostanza" onClick="javascript:nuovaCircosCumulo();">
      	</td>
      </tr>
   </table>
<%  } %>     
    
    <br>
  </body>
</html>