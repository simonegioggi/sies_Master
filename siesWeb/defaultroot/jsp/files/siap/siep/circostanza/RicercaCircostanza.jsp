<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

	<jsp:useBean id="circostanza" 		scope="request" class="java.util.Vector" />
	<jsp:useBean id="ComingFromInsert" 	scope="request" class="java.lang.String"/>
	<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>
	<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%

	// Gestione funzione SIGE
	boolean modoSIGE = false;
	String   lModificabile = "";
	String   lCancellabile = "";

	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{
		modoSIGE = true;
		lModificabile = (String)request.getAttribute("Modificabile");
		lCancellabile =  (String)request.getAttribute("Cancellabile");
	}


  	FascicoloSiepModel lFascicolo = null;
	if (!modoSIGE)
		lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

  	String flagSentenzaApplicazPena = null;
  	String descrBilanciamentoCircostanze = null;
  	String flagGiudizioAbbreviato = null;
  	String noteBilanciamento = null;
  
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Circostanza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%if(lTipoFunzione != null &&  !lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaAltre()
  {
   aForm=document.getElementById("Altre");
    Disabilita();
  }

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("Pena");

    document.Abbandona.A.disabled = true;
    document.Pena.P.disabled = true;
    document.Altre.D.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Elenco Aggravanti soggettive/Attenuanti</font></td>
  <% 
     if(modoSIGE)
     {
    	 
 %>     
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>"/>
          <jsp:param name="ValoreIdEntita" value="0"/>
          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
        </jsp:include>
     </td> <%} %>     
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

	<%if(circostanza.size() > 0)
	{%>
  <br>
  <table cellspacing="1" cellpadding="2" width="95%">
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Articolo Qualificante</td>
      <td class="int">Comma</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="int">Comma Qualificante</td>

      <td class="int">Lettera</td>
      <td class="int">Numero</td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="int">Azioni</td>
<%}%>
    </tr>
<%
  for ( int i=0;i<circostanza.size();i++)
  {
   
	CircostanzaModel CiReato = (CircostanzaModel)circostanza.get(i);
    flagSentenzaApplicazPena = CiReato.getFlagSentenzaApplicazPena();
    descrBilanciamentoCircostanze = CiReato.getDescrBilanciamentoCircostanze();
    flagGiudizioAbbreviato = CiReato.getFlagGiudizioAbbreviato();
    noteBilanciamento = CiReato.getNoteBilanciamento();
    
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getComma(),"-")%></td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrCommaQualificante(),"-")%></td>

      <td class="l"><%=StringUtils.toStringJSP(CiReato.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumero(),"-")%></td>

<% if(modoSIGE) { %> 
        <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=CiReato.getIdCircostanza()%>" />
           <jsp:param name="Modificabile" value="<%=lModificabile%>" />
        </jsp:include>
       </td>
     
<%}else if(lTipoFunzione != null && lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="c">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=CiReato.getIdCircostanza()%>" />
           <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        </jsp:include>
      </td>
<%}
}
%>
    </tr>
    </table>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="l">Sentenza di applicazione pena</td>
        <td class="l">
          <font class="campo">
<% 
// Federica - a9-rr-078
         //   if(lSentenza.getFlagSentenzaApplicazPena() != null && lSentenza.getFlagSentenzaApplicazPena().equals("S"))
            if(flagSentenzaApplicazPena != null && flagSentenzaApplicazPena.equals("S"))
            {
%>
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%
            }
%>
          </font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Bilanciamento circostanze</td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(descrBilanciamentoCircostanze)%>
          </font>&nbsp;
        </td>
      </tr>
      <tr>
 		<td class="l">Annotazioni Bilanciamento circostanze</td>
 		<td class="l">
          <font class="campo"><%=(noteBilanciamento==null)? "&nbsp;" : noteBilanciamento%></font>
        </td>
 	</tr>
      <tr>
        <td class="l">Giudizio abbreviato</td>
        <td class="l">
          <font class="campo">
<%
//Federica - a9-rr-078
//   if(lSentenza.getFlagGiudizioAbbreviato() != null && lSentenza.getFlagGiudizioAbbreviato().equals("S"))
            if(flagGiudizioAbbreviato != null && flagGiudizioAbbreviato.equals("S"))	            
            {
%>
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%
            }
%>
          </font>&nbsp;
        </td>
      </tr>
    </table>
 <%
   }else { %>
    <table width="80%">
    <tr>
          <td class="int" align="left">Nessuna Circostanza definita </td>
  </tr>
  <% }// endif  %>
     
    
    <br>
<%if( lTipoFunzione != null && lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{
  if (ComingFromInsert !=null && ComingFromInsert.equals("YES"))
  {
%>
    <table>
      <tr>
        <td class="LGB">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadInserisciCircostanza">Inserimento ulteriori Circostanze</a>
        </td>
      </tr>
    </table>
<%
  }
}
%>
<%if( lTipoFunzione != null && !lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
<table>
<tr>
<td class="lNoBord">
  <FORM method="POST" name="Altre" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadInserisciCircostanza&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Altre  Aggravanti/Attenuanti" onclick="Javascript:DisabilitaAltre();">
  </FORM>
</td>

<td class="lNoBord">
  <FORM method="POST" name="Pena" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="P" value="Prosegui" onclick="Javascript:Disabilita();">
  </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActRicercaCircostanza&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>
  </body>
</html>