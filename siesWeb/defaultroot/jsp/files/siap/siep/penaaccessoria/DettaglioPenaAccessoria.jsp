<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="penaaccessoria" scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>
<jsp:useBean id="lTipoFunzione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficio"     scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
// Gestione funzione SIGE
boolean modoSIGE = false;
if (modo != null && modo.equalsIgnoreCase("SIGE"))
	modoSIGE = true;


  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

  // Descrizione Tipo Tenore Ordinanza.
  Collection lColTipoTenore = null;
  DecodificheModel lModel = new DecodificheModel();
  IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
  lModel.setContesto("TENORE_ORDINANZA_PA");
  lColTipoTenore = lDecodifiche.ExRicercaDecodifiche(lModel);
  String descrTipoTenore = DecodificheUtils.getDescbyCode(lColTipoTenore, penaaccessoria.getFlagCondonata());
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Pena Accessoria </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="/html/conferma.js"></script>
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

 function DisabilitaCB()
  {

   aForm=document.getElementById("CB");
    Disabilita();
  }

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("APA");

    document.Abbandona.A.disabled = true;
    document.CB.D.disabled = true;
    document.APA.U.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Accessoria</font>
      </td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{
	if(modoSIGE)
	{
		String   lModificabile = (String)request.getAttribute("Modificabile");
		String   lCancellabile = (String)request.getAttribute("Cancellabile");
	 %>     
	      <td class="LBG">
	          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=penaaccessoria.getIdPenaAccessoria()%>" />
	          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
	          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
	        </jsp:include>
	     </td> 
	     
	<%} else { %>     

      <td class="LBG">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=penaaccessoria.getIdPenaAccessoria()%>" />
        </jsp:include>
     </td>
<%}
}%>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

   </tr>
 </table>

  <br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
 	<br>

  </FORM>
  <table cellspacing=2 cellpadding=2 width="70%">
		<tr>
      <td class="l" width="32%">Tipo Pena Accessoria</td>
      <td class="l"><font class="campo"><%=penaaccessoria.getDescrTipoPenaAccessoria() %></font></td>
		</tr>
<%
		if(penaaccessoria.getDescrTipoPenaAccessoria().trim().compareTo("Altre Pene Accessorie")==0)
		{%>
			<tr>
      	<td class="l">Descrizione Altre P. A.</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrAltrePA()) %></font></td>
			</tr>
	<%}
%>

<%
		if(penaaccessoria.getCodNuovoTipoPenaAccessoria().trim().compareTo("-")!=0)
		{%>
			<tr>
      	<td class="l">Nuovo Tipo Pena Accessoria</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrNuovoTipoPenaAccessoria()) %></font></td>
			</tr>
	<%}
%>

		<tr>
      <td class="l">Tipo Durata</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrDurata()) %></font></td>
		</tr>
		<tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumAnni(), "0")%></font>
        Mesi <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumMesi(), "0")%></font>
        Giorni <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumGiorni(), "0")%></font>
      </td>
		</tr>
		<tr>
      <td class="l">Stato</td>
      <td class="l">
        <font class="campo">&nbsp;<%=descrTipoTenore%>
        </font>
      </td>
		</tr>
		<tr>
      <td class="l">Data Fine Validità</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataFineValidita(),"dd-MM-yyyy"))%>&nbsp;</font></td>
		</tr>
<%if(beneficio != null && beneficio.getIdBeneficio() != null) 
{%>		
      <tr><td class="Titolo" colspan=4>Indulto</td></tr>
	  <tr>
         <td class="l">Stato</td>
         <td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrNaturaBeneficio())%></font></td>
	  </tr>		
	  <tr>
         <td class="l">Provvedimento di concessione</td>
         <td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrDpr())%></font></td>
	  </tr>		  
<%} %>				
<%------------------%>
    <tr><td class="Titolo" colspan=4>Estremi Ordinanza Applicazione del GE</td></tr>
		<tr>
      <td class="l">Data Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaGE(), "dd-MM-yyyy"))%></font></td>
		</tr>

    <tr>
      <td class="l">Anno/Numero Ordinanza</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoOrdinanzaGE())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroOrdinanzaGE())%>&nbsp;
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoUfficioOrdinanzaGE())%></font>&nbsp;</td>
    </tr>

    <tr>
      <td class="l">Luogo Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoUfficioOrdinanzaGE())%></font>&nbsp;</td>
    </tr>

    <tr><td class="Titolo" colspan=4>Estremi Ordinanza Condono/Revoca/Sostituzione/Depenalizzazione</td></tr>
		<tr>
      <td class="l">Tenore Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toStringJSP(descrTipoTenore ))%></font></td>
		</tr>

    <tr>
      <td class="l">Data Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(), "dd-MM-yyyy"))%></font></td>
		</tr>

    <tr>
      <td class="l">Anno/Numero Ordinanza</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoOrdinanzaPA())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroOrdinanzaPA())%>&nbsp;
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoUfficioOrdinanzaPA())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Luogo Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoUfficioOrdinanzaPA())%></font>&nbsp;</td>
    </tr>

<%------------%>
      <tr>
        <td class="l" colspan ="2" >
          <font class="l">Fonte&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrFonteGE())%></font>
          <font class="l">&nbsp;&nbsp;Anno&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getAnnoFonteGE())%></font>
          <font class="l">&nbsp;&nbsp;Num.&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumeroFonteGE())%></font>
          <font class="l">&nbsp;&nbsp;Art.&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getArticoloGE())%></font>
          <font class="l">&nbsp;&nbsp;Art.Qualificante&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrSottonumerazioneGE())%></font>
          <font class="l">&nbsp;&nbsp;Comma&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getCommaGE())%></font>
          <font class="l">&nbsp;&nbsp;Let.&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getLetteraGE())%></font>
          <font class="l">&nbsp;&nbsp;Num.&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumeroGE())%></font>
        </td>
      </tr>

<%------------%>

    <tr><td class="Titolo" colspan=4>Revoca Condono</td></tr>
		<tr>
      <td class="l">Revoca Condono</td>
      <td class="l">
        <font class="campo">&nbsp;
<%
        if(penaaccessoria.getFlagRevocaCondono().equals("S"))
        {%>
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
			<%}%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataSentenzaRevoca(), "dd-MM-yyyy"))%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoSentenzaRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroSentenzaRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Sentenza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoUfficioSentenzaRevo())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Luogo Sentenza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoSentenzaRevoca())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. PM</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegePmRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegePmRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. GIP</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegeGipRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegeGipRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. DIB</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegeDibRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegeDibRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CAS</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegeCasRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegeCasRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CAP</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegeCapRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegeCapRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CASAP</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoRegeCasapRevoca())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroRegeCasapRevoca())%>&nbsp;
        </font>
      </td>
    </tr>
		<tr>
      <td class="l">Falsità di documenti</td>
      <td class="l">
        <font class="campo">&nbsp;
<%
        if(penaaccessoria.getFlagDichiarazioneFalsita().equals("S"))
        {
%>
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%
        }
%>
        </font>
      </td>
		</tr>
		<tr>
      <td class="l">Note</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNote())%>&nbsp;</font>
      </td>
		</tr>

		</table>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="APA" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadInserisciPenaAccessoria&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="U" value="Altra Pena accessoria" onclick="Javascript:Disabilita();">
 </FORM>
</td>

<td class="lNoBord">
  <FORM method="POST" name="CB" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActGrigliaIscrizioneBenefici&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Prosegui" onclick="Javascript:DisabilitaCB();">
  </FORM>
</td>
<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadDettaglioPenaAccessoria&<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>=<%=penaaccessoria.getIdPenaAccessoria()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>

  </body>
</html>