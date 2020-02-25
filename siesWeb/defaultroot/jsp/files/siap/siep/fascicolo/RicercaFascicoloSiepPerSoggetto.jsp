<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="StrCodiceDistrettoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="ufficiAltroDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufficiDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="StrNomeSoggettoAlias" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="strTipoRicerca" scope="request" class="java.lang.String" />

  <%
  String lCodTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
  boolean isSIUS = (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0 || lCodTipoUfficio.compareToIgnoreCase("UDS") == 0) ? true : false;

  Iterator itxControlla = fascicoli.iterator();
  String lprocAltreBDI = "N";
  String lproc = "S";

    while ( itxControlla.hasNext())
    {
         FascicoloSiepModel Contollafascicolo = (FascicoloSiepModel)itxControlla.next();
         if(!Contollafascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente))
         {
           lprocAltreBDI="S";
         }
          else
         {
           lproc="N";

         }
    }
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti di :</font></td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
<table width ="100%">
<%

if(StrNomeSoggettoAlias.length() > 0)
 {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
         		<%=StrNomeSoggettoAlias%>
      		</font>
    	</td>
    </tr>
    <tr>
    	<td class="l" >Alias di: &nbsp;
    		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
       		</font>
    	</td>
   </tr>
<%}
  else
  {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
      		</font>
    	</td>
   </tr>
<%}%>
  
  <tr>
<%
  if(soggetto.getSesso().equals("M"))
  {
%>
    	<td class="l" >Nato il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
  }
  else
  {
%>
    	<td class="l" >Nata il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%
  }
%>
  			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%>
  			</font>&nbsp;&nbsp; in:  &nbsp;&nbsp;&nbsp;
<%
  if (soggetto.getDescrComuneNascita().compareTo("-")==0)
  {
%>
    		<font class="campo"><%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)&nbsp; 
			</font>
<%
  }
  else
  {
%>
    		<font class="campo"><%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)&nbsp;
			</font>
<%
  }
%>
		</td>	
  </tr>
</table>
<table cellspacing=2 cellpadding=2>
<%
  if((lproc.equals("N") ))
  {
%>
    <tr>
      <td class ="Titolo" colspan=8>
      	<%if(strTipoRicerca != null && strTipoRicerca.equals("ufficio")) 
        	{%>
      			Elenco Procedimenti associati al soggetto dell' ufficio
      	  <%}
      	    else if (strTipoRicerca != null && !strTipoRicerca.equals("ufficio"))
      	    {%>
      	    	Elenco Procedimenti associati al soggetto del distretto
      	  <%}%>  
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio Esecuzione</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
  }

   Iterator itx = fascicoli.iterator();
   Iterator itxUffDistr = ufficiDistretto.iterator();

    while ( itx.hasNext())
    {
      FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
// Se utente SIUS non vengono visualizzati i fascicoli nello stato ISCRITTO (02)
      if (!isSIUS || fascicolo.getCodStatoFascicolo().compareTo("02") != 0)
      {
        if(fascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente))
        {
          UfficioModel lUfficio = (UfficioModel)itxUffDistr.next();

%>
          <tr>
            <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
            <td class="c"><font class="label"><%=lUfficio.getCodTipoUfficio()%> di <%=lUfficio.getDescrComune()%> </font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
            <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
            <%--td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
            <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
            <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>
            <td class="c">
              <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
                 <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
                 <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getIdFascicoloSiep()%>" />
                 <jsp:param name="FlagValidato" value="<%=fascicolo.getFlagValidato()%>" />

                 <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
                 <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />


              </jsp:include>
            </td>
          </tr>
<%
        }
      }
    }
%>
  </table>
  <br>
  <table cellspacing=2 cellpadding=2>
<%
    if((lprocAltreBDI.equals("S") ))
    {
%>
      <tr>
        <td class="Titolo" colspan =9>Elenco Procedimenti associati al soggetto da altri distretti</td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="int">Data Titolo Esecutivo</td>
        <td class="int">Autorità Titolo Esecutivo</td>
        <td class="int">Data Irrevocabilità</td>
        <td class="int">Numero SIEP</td>
        <td class="int">Ufficio Esecuzione</td>
        <td class="int">Data di Iscrizione</td>
        <td class="int">Stato del Procedimento</td>
        <td class="int">Data di Trasferimento</td>
        <td class="int">Azioni</td>
      </tr>
<%
    }

    Iterator itxalteBDI = fascicoli.iterator();
    Iterator uff = ufficiAltroDistretto.iterator();
    while ( itxalteBDI.hasNext())
    {
      FascicoloSiepModel fascicoloSiep = (FascicoloSiepModel)itxalteBDI.next();

      if(!fascicoloSiep.getCodDistretto().equals(StrCodiceDistrettoUtente))
      {
        UfficioModel lUff = (UfficioModel)uff.next();
%>
        <tr>
          <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicoloSiep.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
          <td class="c"><font class="label"><%=fascicoloSiep.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicoloSiep.getSentenza().getDescrLuogoEmittente()%></font></td>
          <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicoloSiep.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
          <td class="c"><font class="label"><%=fascicoloSiep.getChiaveAnno()%>/<%=fascicoloSiep.getChiaveProgr()%></font></td>
          <td class="c"><font class="label"><%=lUff.getCodTipoUfficio()%> di <%=lUff.getDescrComune()%></font></td>
          <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiep.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
          <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
          <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
          <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloSiep.getDescrStatoProcedimento())%>&nbsp;</font></td>
          <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiep.getDataAggiornamento(),"dd-MM-yyyy"))%>&nbsp;</font></td>
          <td class="c">
            <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
               <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
               <jsp:param name="ValoreIdEntita" value="<%=fascicoloSiep.getIdFascicoloSiep()%>" />
               <jsp:param name="FlagValidato" value="<%=fascicoloSiep.getFlagValidato()%>" />
            </jsp:include>
            <a href="/jsp/Main.jsp?Action=siap.siep.jms.action.ActRicercaEstesaFascicoloPerTrasferimento&<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>=<%=fascicoloSiep.getChiaveAnno()%>&<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>=<%=fascicoloSiep.getChiaveProgr()%>&chiaveUfficio=<%=fascicoloSiep.getChiaveUfficio()%>&ricercaSoggetto=S">
              <img src="/images/net24.gif" width="12" height="12" alt="Aggiorna Trasferimenti" border="0">
            </a>
          </td>
        </tr>
<%
      }
    }
%>
    </table>
  </form>
  <br>
  </body>
</html>