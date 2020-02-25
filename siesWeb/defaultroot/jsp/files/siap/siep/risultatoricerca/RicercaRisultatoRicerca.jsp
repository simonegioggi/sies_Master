<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.risultatoricerca.model.RisultatoRicercaModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.risultatoricerca.action.ICostantiRisultatoRicerca"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<jsp:useBean id="risultatoricerca" scope="request" class="java.util.Vector"/>

<jsp:useBean id="lAnnoInzio" scope="request" class="java.lang.String"/>
<jsp:useBean id="lNumeroInzio" scope="request" class="java.lang.String"/>
<jsp:useBean id="lAnnoFine" scope="request" class="java.lang.String"/>
<jsp:useBean id="lNumeroFine" scope="request" class="java.lang.String"/>
<jsp:useBean id="lDataReato" scope="request" class="java.lang.String"/>
<jsp:useBean id="lAnniRes" scope="request" class="java.lang.String"/>
<jsp:useBean id="lGiorniRes" scope="request" class="java.lang.String"/>
<jsp:useBean id="lMesiRes" scope="request" class="java.lang.String"/>
<jsp:useBean id="lAnniSen" scope="request" class="java.lang.String"/>
<jsp:useBean id="lGiorniSen" scope="request" class="java.lang.String"/>
<jsp:useBean id="lMesiSen" scope="request" class="java.lang.String"/>
<jsp:useBean id="lPosAggregata" scope="request" class="java.lang.String"/>
<jsp:useBean id="lDescPosGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="lIdRisultatoRicerca" scope="request" class="java.lang.String"/>
<jsp:useBean id="lNazione" scope="request" class="java.lang.String"/>

<jsp:useBean id="elencocompleto" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Elenco Provvedimenti </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione: </font> <font class="campo">Elenco Procedimenti per Applicazione Benefici</font>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
     </td>
        <td class="LBG">
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
  </table>


  <table>
    <tr><td>&nbsp;</td></tr>
    <tr><td class="titolo" colspan="4">Criteri Selezionati</td></tr>
    <tr>
      <td class="L">Anno/Numero Iniziale: </font></td>
<%
  if(lAnnoInzio != null && lAnnoInzio.equals("null"))
    {
     lAnnoInzio="";
     lNumeroInzio="";
    }
%>
      <td class="l"><%=StringUtils.toStringJSP(lAnnoInzio,"-")%>/<%=StringUtils.toStringJSP(lNumeroInzio,"-")%></td>
      <td class="L">Anno/Numero Finale: </font></td>
<%
  if(lAnnoFine != null && lAnnoFine.equals("null"))
    {
     lAnnoFine="";
     lNumeroFine="";
    }
%>
      <td class="l"><%=StringUtils.toStringJSP(lAnnoFine,"-")%>/<%=StringUtils.toStringJSP(lNumeroFine,"-")%></td>

    </tr>
    <tr>
      <td class="L">Data massima di commesso reato: </td>
      <td class="L"><%=StringUtils.toStringJSP(lDataReato,"-")%></td>
    </tr>
    <tr>
<%
  if(lAnniRes != null && lAnniRes.equals("null"))
    {
     lAnniRes="0";
    }
  if(lMesiRes != null && lMesiRes.equals("null"))
    {
     lMesiRes="0";
    }
  if(lGiorniRes != null && lGiorniRes.equals("null"))
    {
     lGiorniRes="0";
    }
%>
      <td class="L">Quantum di pena residua da espiare: </td>
      <td class="L">
       Anni: <%=StringUtils.toStringJSP(lAnniRes,"-")%>
       Mesi: <%=StringUtils.toStringJSP(lMesiRes,"-")%>
       Giorni: <%=StringUtils.toStringJSP(lGiorniRes,"-")%>
    </td>
    </tr>
    <tr>
      <td class="l">Posizione giuridica aggregata: </td>
<%
         if(lPosAggregata != null)
         {
          if(lPosAggregata.equals("0"))
          {
%>
            <td class="l">Nessuna</td>
<%
          }else if(lPosAggregata.equals("1"))
          {
%>
          <td class="l">In espiazione pena in carcere</td>
<%
          }else if(lPosAggregata.equals("2"))
          {
%>
          <td class="l">In misura tutte</td>
<%
          }else if(lPosAggregata.equals("3"))
          {
%>
            <td class="l">Libero e assimilati</td>
<%
          }
         }
%>
    </tr>
    <tr>
     <td class="L">Singola posizione giuridica: </td>
     <td class="L"><%=StringUtils.toStringJSP(lDescPosGiuridica,"-")%></td>
    </tr>
    <tr>
<%
  if(lAnniSen != null && lAnniSen.equals("null"))
    {
     lAnniSen="0";
    }
  if(lMesiSen != null && lMesiSen.equals("null"))
    {
     lMesiSen="0";
    }
  if(lGiorniSen != null && lGiorniSen.equals("null"))
    {
     lGiorniSen="0";
    }
%>
     <td class="L">Quantum pena irrogata in sentenza: </td>
     <td class="L">
      Anni: <%=StringUtils.toStringJSP(lAnniSen,"-")%>
      Mesi: <%=StringUtils.toStringJSP(lMesiSen,"-")%>
      Giorni: <%=StringUtils.toStringJSP(lGiorniSen,"-")%>
     </td>
    </tr>

    <tr>
     <td class="L">Nazionalità: </td>
<%
         if(lNazione != null)
         {
          if(lNazione.equals("I"))
          {
%>
            <td class="l">Italiana</td>
<%
          }else if(lPosAggregata.equals("S"))
          {
%>
          <td class="l">Straniera</td>
<%
          }
         }
%>    </tr>
    
  </table>
<%
String lClasse = "small";
if(elencocompleto != null && elencocompleto.equals("N"))
{
 lClasse = "l";
%>

<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <form  method="POST"  action="<%= IWebConstants.PG_MAIN%>" name="f">

    <input type="HIDDEN" name="lAnnoInzio" value="<%=lAnnoInzio%>">
    <input type="HIDDEN" name="lNumeroInzio" value="<%=lNumeroInzio%>">
    <input type="HIDDEN" name="lAnnoFine" value="<%=lAnnoFine%>">
    <input type="HIDDEN" name="lNumeroFine" value="<%=lNumeroFine%>">
    <input type="HIDDEN" name="lDataReato" value="<%=lDataReato%>">
    <input type="HIDDEN" name="lAnniRes" value="<%=lAnniRes%>">
    <input type="HIDDEN" name="lGiorniRes" value="<%=lGiorniRes%>">
    <input type="HIDDEN" name="lMesiRes" value="<%=lMesiRes%>">
    <input type="HIDDEN" name="lAnniSen" value="<%=lAnniSen%>">
    <input type="HIDDEN" name="lGiorniSen" value="<%=lGiorniSen%>">
    <input type="HIDDEN" name="lMesiSen" value="<%=lMesiSen%>">
    <input type="HIDDEN" name="lPosAggregata" value="<%=lPosAggregata%>">
    <input type="HIDDEN" name="lDescPosGiuridica" value="<%=lDescPosGiuridica%>">
    <input type="HIDDEN" name="lNazione" value="<%=lNazione%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.risultatoricerca.action.ActRicercaRisultatoRicerca">
    <input type="HIDDEN" name="<%=ICostantiRisultatoRicerca.CAMPO_ID_RICERCA%>" value="<%=lIdRisultatoRicerca%>">

  <table>
    <tr>
      <td>
        <a class="cliccabile" href="javascript:document.f.submit();" title="Elenco Completo per Stampa Videata">
       		Elenco Completo per Stampa Videata
        </a>
      </td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <!-- NUOVO BOTTONE PER STAMPA EXCEL -->
	  <td>
		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.risultatoricerca.action.ActRicercaRisultatoRicercaExcel&IdRisultatoRicerca=<%=lIdRisultatoRicerca%>">
		  	Genera Foglio Excel
	    </a>
	  </td>
    </tr>
  </table>
 </form>
<%}%>
 <br>
  <div align="center">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">N°SIEP</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Luogo di Nascita</td>
      <td class="int">Data di Nascita</td>
      <td class="int">Data Fine Pena</td>
<%
    if (!lAnniRes.equals("0") || !lMesiRes.equals("0")|| !lGiorniRes.equals("0"))
        {
%>
          <td class="int">Pena Residua</td>
<%
        }
    else if (!lAnniSen.equals("0") || !lMesiSen.equals("0")|| !lGiorniSen.equals("0"))
        {
%>
          <td class="int">Pena Complessiva</td>
<%
        }
    else
      {
%>
          <td class="int">Pena Residua</td>
<%
      }
%>

      <td class="int">Posizione Giuridica</td>
      <td class="int">Nazionalità</td>
<%
if(elencocompleto != null && elencocompleto.equals("N"))
{
%>
     <td class="int">Azioni</td>
<%
}
%>
    </tr>
<%
  Iterator itx = risultatoricerca.iterator();
  while ( itx.hasNext())
  {
    RisultatoRicercaModel lRis = (RisultatoRicercaModel)itx.next();
%>
    <tr>
      <td class="<%=lClasse%>">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lRis.getIdFascicoloSiep()%>" title="Procedimento">
             <%=lRis.getChiaveAnno()%>/<%=lRis.getChiaveProgr()%>
          </a>
      </td>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lRis.getCognome(),"-")%></td>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lRis.getNome(),"-")%></td>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lRis.getLuogoNascita(),"-")%></td>
      <td nowrap class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRis.getDataNascita(),"dd-MM-yyyy"),"-")%></td>
      <td nowrap class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRis.getDataFinePena(),"dd-MM-yyyy"),"-")%></td>
      <td class="<%=lClasse%>">
        Anni: <%=StringUtils.toStringJSP(lRis.getNumAnniPenaRes(),"-")%>
        Mesi: <%=StringUtils.toStringJSP(lRis.getNumMesiPenaRes(),"-")%>
        Giorni: <%=StringUtils.toStringJSP(lRis.getNumGiorniPenaRes(),"-")%>
      </td>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lRis.getDescrPosizioneGiuridica(),"-")%></td>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lRis.getNazionalita(),"-")%></td>
<%
if(elencocompleto != null && elencocompleto.equals("N"))
{
%>
      <td class="c">
        <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lRis.getIdFascicoloSiep()%>" />
          <jsp:param name="Modificabile" value="SI" />
        </jsp:include>
      </td>
<%
}
%>
    </tr>
<%
   }
%>
  </table>
  </div>
</body>
</html>