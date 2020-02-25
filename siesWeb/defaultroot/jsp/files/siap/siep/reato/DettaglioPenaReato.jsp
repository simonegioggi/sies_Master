<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>


<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="reato"  scope="request" class="siap.siep.reato.model.ReatoModel"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="reatoSigeModificabile"       scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

	// Gestione Reato SIGE
	boolean modoSIGE = false;
	String validatoSige = "N";
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{	
		modoSIGE = true;
		// Se il reato risulta non modificabile è come se fosse validato
		if(reatoSigeModificabile.equalsIgnoreCase("N"))
				validatoSige = "S";
	}

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.A.P] - Gestione Reato - </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%
    if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
    {
%>
    <script language="JavaScript">
      var aForm=null;
      function Verify()
      {
        alert("La funzione di Iscrizione Guidata è stata Interrotta");
        aForm=document.getElementById("Abbandona");
        Disabilita();
      }

      function DisabilitaCI()
      {
        aForm=document.getElementById("AltroIm");
        Disabilita();
      }

      function DisabilitaPen()
      {
        aForm=document.getElementById("PenaRe");
        Disabilita();
      }

      function Disabilita()
      {
        if (aForm==null)
          aForm=document.getElementById("UltArt");

        document.Abbandona.A.disabled = true;
        document.UltArt.U.disabled = true;
        document.AltroIm.D.disabled = true;
        document.PenaRe.P.disabled = true;

        aForm.submit();
    }
  </script>
<%
  }
%>
  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Pena Reato</font>
        </td>
<%
  if(lTipoFunzione.equals("") )  // lTipoFunzione per capire che si proviene da iscrizione guidata
  {
   if(modoSIGE)
   {
%> 
   	<td class="LBG">
    <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_REATO%>">
    <jsp:param name="CampoIdEntita" value="<%=ICostantiReato.CAMPO_ID_REATO%>" />
    <jsp:param name="ValoreIdEntita" value="<%=reato.getIdReato()%>" />
    <jsp:param name="FlagReato" value="<%=reato.isReato()%>" />
	<jsp:param name="FlagValidato" value="<%=validatoSige%>" />
	</jsp:include>
</td>
<%
   }else {
%>
        <td class="LBG">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiReato.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lFascicolo.getIdFascicoloSiep()%>" />
            <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
          </jsp:include>
        </td>
       
<%
  }}
%>
	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
    </table>
 <%if (!modoSIGE) { %>
		<br>
			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
		<br>
<%} else {%>
		<br>
			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
			<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
		<br>
    	<jsp:include page="/jsp/files/siap/siep/reato/DettaglioReatoAssociato.jsp"/>
		<br>
<%}%>

  </FORM>
  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Tipo Pena Detentiva</td>
      <td class="l"><font class="campo"><%=reato.getDescrTipoPenaDetentiva()%>&nbsp;</font></td>
		</tr>
		<tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumAnni(), "0")%>&nbsp;</font>
        Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumMesi(), "0")%>&nbsp;</font>
        Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumGiorni(), "0")%></font>
      </td>
		</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <tr>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(reato.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(reato.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
--%>
    <tr>
      <td class="l">Durata Isolamento Diurno</td>
      <td class="L" >
        <font class="label">Anni </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumAnniIsolamentoDiurno(),"0")%></font>&nbsp;
        <font class="label">Mesi </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumMesiIsolamentoDiurno(),"0")%></font>&nbsp;
        <font class="label">Giorni </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumGiorniIsolamentoDiurno(),"0")%></font>
      </td>
    </tr>
		<tr>
      <td class="l">Tipo Sanzione</td>
      <td class="l"><font class="campo"><%=reato.getDescrTipoSanzione()%>&nbsp;</font></td>
      </td>
		</tr>
		<tr>
      <td class="l">Sanzione Pecuniaria</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toEuroFormat(reato.getSanzionePecuniaria())%></font> Euro
      </td>
		</tr>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<tr>
<td class="lNoBord">
<FORM method="POST" name="UltArt" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadInserisciCircostanza&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="U" value="Aggravanti/Attenuanti" onclick="Javascript:Disabilita();">
 </FORM>
</td>

<td class="lNoBord">
  <FORM method="POST" name="AltroIm" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadInserisciReato&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Altro Capo Imputazione" onclick="Javascript:DisabilitaCI();">
  </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="PenaRe" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="P" value="Pena Complessiva" onclick="Javascript:DisabilitaPen();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadDettaglioPenaReato&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=reato.getIdReato()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>

<%}%>

  </table>
</body>
</html>