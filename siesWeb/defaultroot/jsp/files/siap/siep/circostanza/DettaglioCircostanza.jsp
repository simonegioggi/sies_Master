<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="circostanza" scope="request" class="siap.siep.circostanza.model.CircostanzaModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicolo = null;

//Gestione funzione SIGE
boolean modoSIGE = false;

if (modo != null && modo.equalsIgnoreCase("SIGE"))
{
	modoSIGE = true;
}else {
  	lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
}
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio CircostanzaReato </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>
<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Aggravanti soggettive/Attenuanti</font>
        </td>
 <%	if(modoSIGE)
	{
		String   lModificabile = (String)request.getAttribute("Modificabile");
		String   lCancellabile = (String)request.getAttribute("Cancellabile");
	 %>     
	      <td class="LBG">
	          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>" />
            <jsp:param name="ValoreIdEntita" value="<%=circostanza.getIdCircostanza()%>" />
	          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
	          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
	        </jsp:include>
	     </td> 
   <%} else { %>     
         
        <td class="LBG">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>" />
            <jsp:param name="ValoreIdEntita" value="<%=circostanza.getIdCircostanza()%>" />
            <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
          </jsp:include>
        </td>
 <%} %>
 
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
 
      </tr>
    </table>

  <br>
  <%if (!modoSIGE) { %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
   <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
	 <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%} %>
  <br>

  </FORM>
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
          
          //**************************************************************************************************
          //Federica - a9-rr-078
          //aggiunto campo Comma-Qualificante 
          if(circostanza.getDescrCommaQualificante() != null && !circostanza.getDescrCommaQualificante().equals("") && !circostanza.getDescrCommaQualificante().equals("-"))
            out.println(" "+circostanza.getDescrCommaQualificante());
          //**************************************************************************************************
          
          if(circostanza.getLettera() != null && !circostanza.getLettera().equals(""))
            out.println(" l. "+circostanza.getLettera());
          if(circostanza.getNumero() != null && !circostanza.getNumero().equals(""))
            out.println(" n. "+circostanza.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
</body>
</html>