<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="reato"  				scope="request" class="siap.siep.reato.model.ReatoModel"/>
<jsp:useBean id="lTipoFunzione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="reatoSigeModificabile" scope="request" class="java.lang.String"/>

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
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaUA()
  {

   aForm=document.getElementById("UltArt");
    Disabilita();
  }

 function DisabilitaACI()
  {

   aForm=document.getElementById("AltroIm");
    Disabilita();
  }

function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("PenaRe");

    document.Abbandona.A.disabled = true;
    document.PenaRe.P.disabled = true;
    document.UltArt.U.disabled = true;
    document.AltroIm.D.disabled = true;

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
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Reato</font>
        </td>
<%
	if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
	{ 
%>
	<td class="LBG">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_REATO%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiReato.CAMPO_ID_REATO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=reato.getIdReato()%>" />
            <jsp:param name="FlagReato" value="<%=reato.isReato()%>" />
   			<jsp:param name="FlagValidato" value="" />
 			</jsp:include>
	</td>
	
	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

<%
	}  // endif lTipoFunzione
%>
      </tr>
    </table>
    
<%	
	if (!modoSIGE) 
	{%>
		<br>
			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
		<br>
<%} else {%>
		<br>
			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
			<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
		<br>
<%}%>

  </FORM>
  <table cellspacing="2" cellpadding="2" width="95%">
    <tr><td class="Titolo">Reato</td></tr>
    <tr>
      <td class="l">
        <font class="campo">
<%
          String lProgressivo = "";
          if(reato.getProgrCircostanza().intValue() == 1)
            lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();

          if(!lProgressivo.equals(""))
          {
%>
            <font class="campoNoCap">
<%
              out.print("Reato N."+lProgressivo+": ");
%>
            </font>
<%
          }
          boolean lFlagAnnoNumero = false;
          if( reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals("")
              && reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte()+" ");
            if(reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals(""))
              out.println(reato.getAnnoFonte());
            if(reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals(""))
              out.println("/"+reato.getNumeroFonte());
          }

          if(reato.getArticolo() != null && !reato.getArticolo().equals(""))
            out.println("art."+reato.getArticolo());
          if(reato.getDescrSottonumerazione() != null && !reato.getDescrSottonumerazione().equals("") && !reato.getDescrSottonumerazione().equals("-"))
            out.println(" "+reato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte());
          }

          if(reato.getComma() != null && !reato.getComma().equals(""))
            out.println(" c. "+reato.getComma());
          
          //**************************************************************************************************
          //Federica - a9-rr-078
          //aggiunto campo Comma-Qualificante 
          if(reato.getDescrCommaQualificante() != null && !reato.getDescrCommaQualificante().equals("") && !reato.getDescrCommaQualificante().equals("-"))
            out.println(" "+reato.getDescrCommaQualificante());
          //**************************************************************************************************
          
          if(reato.getLettera() != null && !reato.getLettera().equals(""))
            out.println(" l. "+reato.getLettera());
          if(reato.getNumero() != null && !reato.getNumero().equals(""))
            out.println(" n. "+reato.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza().intValue() == 1)
  {
%>
    <table cellspacing="2" cellpadding="2" width="95%">
<%
    if(reato.getDescrTipoReato() != null && !reato.getDescrTipoReato().equals("") && !reato.getDescrTipoReato().equals("-"))
    {
%>
      <tr>
				<td class="l">Tipo Reato</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescrTipoReato())%>&nbsp;</font></td>
  		</tr>
<%
    }
    if(reato.getDescLuogo() != null && !reato.getDescLuogo().equals(""))
    {
%>
      <tr>
        <td class="l">Luogo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescLuogo())%>&nbsp;</font></td>
  		</tr>
<%
    }
    if(reato.getCodPeriodoConsumazione() != null && !reato.getCodPeriodoConsumazione().equals("") && !reato.getCodPeriodoConsumazione().equals("-"))
    {
%>
      <tr>
        <td class="l">Periodo Consumazione</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescrPeriodoConsumazione())%>&nbsp;</font></td>
  		</tr>
<%
    }
    if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
    {
%>
      <tr>
        <td class="l">&lt;Data1&gt;</td>
        <td class="l">
          <font class="campo">
<%
        String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
        if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
        if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
%>
            <%=lStrGGInizio%>
            -
            <%=lStrMMInizio%>
            -
            <%=lStrAAInizio%>
          </font>
        </td>
      </tr>
<%
    }
    if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
    {
%>
      <tr>
        <td class="l">&lt;Data2&gt;</td>
        <td class="l">
<%
        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
        if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
        if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
%>
          <font class="campo">
            <%=lStrGGFine%>
            -
            <%=lStrMMFine%>
            -
            <%=lStrAAFine%>
          </font>
        </td>
      </tr>
<%
    }
    if(reato.getNote() != null && !reato.getNote().equals(""))
    {
%>
      <tr>
        <td class="l"><font class="label">Note</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getNote())%>&nbsp;</font></td>
      </tr>
<%
    }
%>
    </table>
<%
  }
%>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="UltArt" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadInserisciUlterioriReati&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=reato.getIdReato()%>">
      <br><INPUT class="bottone" type="button" name="U" value="Ulteriori Articoli" onclick="Javascript:DisabilitaUA();">
 </FORM>
</td>

<td class="lNoBord">
  <FORM method="POST" name="AltroIm" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadInserisciReato&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Altro Capo Imputazione" onclick="Javascript:DisabilitaACI();">
  </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="PenaRe" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadInserisciPenaReato&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=reato.getIdReato()%>">
      <br><INPUT class="bottone" type="button" name="P" value="Prosegui" onclick="Javascript:Disabilita();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadDettaglioReato&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=reato.getIdReato()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>
</body>
</html>