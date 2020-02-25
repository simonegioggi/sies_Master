<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="reati" 			scope="request" class="java.util.Vector" />
<jsp:useBean id="ComingFromInsert" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

  BigDecimal lProgrPrimoReato = ((ReatoModel)reati.get(0)).getIdReato();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Reato</title>
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

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("Pro");

    document.Abbandona.A.disabled = true;
    document.Pro.P.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
  </head>
<%
ReatoModel lReato=new ReatoModel();
Iterator itx = reati.iterator();
if (reati.size()==1)
{
    lReato=(ReatoModel)reati.get(0);
}else
    while ( itx.hasNext())
   {
    ReatoModel reato = (ReatoModel)itx.next();
     if(reato.getProgrCircostanza().intValue() == 1)
       lReato=reato;
   }
%>
  <body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Elenco Norme</font></td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="LBG">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_REATO_ONLYCOMBO%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiReato.CAMPO_ID_REATO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lReato.getIdReato()%>" />
            <jsp:param name="FlagReato" value="<%=lReato.isReato()%>" />
            <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
          </jsp:include>
        </td>
<%}%>
    </tr>
  </table>
</form>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <div align="center">
  <table>
    <tr>
      <td class="int">N. Reato</td>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art. Qual.</td>
      <td class="int">Comma</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
%>
      <td class="int">Comma Qual.</td>

      <td class="int">Lettera</td>
      <td class="int">Numero</td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="int" width="5%">Azioni</td>
<%}%>
    </tr>

<%
  itx = reati.iterator();
  while ( itx.hasNext())
  {
    ReatoModel reato= new ReatoModel();
    reato = (ReatoModel)itx.next();
    String PenaDet=new String("");
	if (reato.getNumAnni() != null)
      PenaDet += "anni " +reato.getNumAnni();
	if (reato.getNumMesi() != null)
      PenaDet += " mesi "+ reato.getNumMesi();
	if (reato.getNumGiorni() != null)
      PenaDet += " giorni "+ reato.getNumGiorni();

  String lProgressivo = "";
  if(reato.getProgrCircostanza().intValue() == 1)
   lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();

%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(lProgressivo)%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getAnnoFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumeroFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getArticolo(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrSottonumerazione(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getComma(),"-")%>&nbsp;</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
%>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrCommaQualificante(),"-")%>&nbsp;</td>

      <td class="l"><%=StringUtils.toStringJSP(reato.getLettera(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumero(),"-")%>&nbsp;</td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="c">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiReato.CAMPO_ID_REATO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=reato.getIdReato()%>" />
           <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        </jsp:include>
      </td>
<%}%>
    </tr>
<%
  }
%>
  </table>

  </div>

<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
<table align="center">
<tr>
<td class="lNoBord" colspan ="2">
  <FORM method="POST" name="Pro" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadDettaglioReato&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=lReato.getIdReato()%>">
      <br><INPUT class="bottone" type="button" name="P" value="Prosegui" onclick="Javascript:Disabilita();">
  </FORM>
</td>

<td class="lNoBord" colspan ="2">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActRicercaUlterioriNorme&<%=ICostantiReato.CAMPO_PROGR_REATO%>=<%=lReato.getProgrReato()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>

  </body>
</html>