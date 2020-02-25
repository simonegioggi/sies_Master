<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<%@ page import="siap.siep.sentenzariunita.action.ICostantiSentenzaRiunita" %>
<%@ page import="siap.siep.sentenzariunita.model.SentenzaRiunitaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="sentenzeriunite" scope="request" class="java.util.Vector" />

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresidua" scope="session" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<%
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Sentenza Riunita in Appello</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Sentenze Riunite in Appello</font></td>
      </tr>
    </table>

    <br>

<table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>
          &nbsp;

        <font class="label">Data Iscrizione :</font>
<%
        if(fascicolo.getDataIscrizione()!= null)
        {
%>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>
          </font>
<%
        }
        else
        {
%>
          -
<%
        }
%>
        <font class="label"> Data Arrivo Atto :</font>
<%
        if(fascicolo.getDataArrivoAtto()!= null)
        {
%>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataArrivoAtto(),"dd-MM-yyyy"))%>
          </font>
<%
        }
        else
        {
%>
          -
<%
        }
%>





<%
          if(fascicolo.getFlagCumulante()!=null && fascicolo.getFlagCumulante().equals("S"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
<%
          }

          if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
<%
          }

          if(   fascicolo.getCodStatoFascicolo() != null
             && (fascicolo.getCodStatoFascicolo().equals("01"))
             )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
<%
          }

        if((    penaresidua != null
             && penaresidua.getFlagPenaSospesa()!= null
             && penaresidua.getFlagPenaSospesa().equals("S"))
             || (     fascicolo!= null && fascicolo.getChiaveProgr() != null
                  && (fascicolo.getChiaveProgr().intValue() >= 30000
                  && fascicolo.getChiaveProgr().intValue() < 40000)))
        {
          if( fascicolo!= null && fascicolo.getChiaveProgr() != null
            && ( fascicolo.getChiaveProgr().intValue() >= 30000
            &&   fascicolo.getChiaveProgr().intValue() < 40000) )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
          }
          else
          {
%>
            <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
          }
        }

        if(   penaresidua != null
           && penaresidua.getFlagPenaSospesa()!= null
           && penaresidua.getFlagPenaSospesa().equals("I"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
        }
        if(   penaresidua != null
           && penaresidua.getFlagPenaSospesa()!= null
           && penaresidua.getFlagPenaSospesa().equals("D"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
        }
%>
<%
          if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio())))
          {
%>
            &nbsp;
            <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
<%
          }
%>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">

 <%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
       <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
      }
%>

      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
          <font class="label">del</font>&nbsp;

            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>

        </font>
		<% if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
		 }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="L">
        <font class="label">Data irrevocabilità: </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr--%>
  </table>

    <table cellpadding="4" cellspacing="4" width="100%">

    <tr>
      <td class="l"></td>      
      <td class="int">Data Sentenza</td>
      <td class="int">Anno/Numero Sentenza</td>
      <td class="int">Autorità</td>
      <td class="int">Luogo</td>
      <td class="int">Azioni</td>
    </tr>

<%
    Iterator itx = sentenzeriunite.iterator();
	String flagassociato="";
	int numsenriu=sentenzeriunite.size();
	int conto=0;
    while ( itx.hasNext())
    {
      SentenzaRiunitaFascSiepModel lSen = (SentenzaRiunitaFascSiepModel)itx.next();
      flagassociato="";
	  if (lSen.getFasSieIdFascicoloSiep()!=null)
	      if( (fascicolo.getIdFascicoloSiep()).compareTo(lSen.getFasSieIdFascicoloSiep()) ==0)
			  flagassociato="checked";
%>
    <tr>
  	  <td class="l"><input type="checkbox" name="flagcollegato" value="<%=conto%>"  <%=flagassociato%> onClick=""></td>
      <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSen.getSentenzaRiunitaModel().getDataSentenza(),"dd-MM-yyyy"))%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getSentenzaRiunitaModel().getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lSen.getSentenzaRiunitaModel().getNumeroSentenza())%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getSentenzaRiunitaModel().getDescrTipoAutoritaEmittente())%></td>
      <td class=C><%=StringUtils.toStringJSP(lSen.getSentenzaRiunitaModel().getDescrLuogoEmittente())%></td>
      <td class=C>
        <jsp:include page="<%=ICostantiSentenza.PG_BUTTONS_SENTENZA%>">
           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
           <jsp:param name="CampoIdEntita" value="<%=ICostantiSentenzaRiunita.CAMPO_ID_SENTENZA_RIUNITA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lSen.getSentenzaRiunitaModel().getIdSentenzaRiunita()%>"/>
        </jsp:include>
      </td>
  	  <td class="l"><input type="HIDDEN" name="chiaveSenRiu<%=conto%>" value="<%=lSen.getSentenzaRiunitaModel().getIdSentenzaRiunita()%>"></td>
  	  <td class="l"><input type="HIDDEN" name="chiaveSenRiuFasc<%=conto%>" value="<%=lSen.getIdSentenzaRiunitaFascSiep()%>"></td>
  	  <td class="l"><input type="HIDDEN" name="associato<%=conto%>" value="<%=flagassociato%>"></td>
    </tr>
<%
		conto++;
    }
%>
    </table>
    <table>
        <tr>
      	<td colspan=2>
 	      <br>
        <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit" name="INSERISCI" value="Conferma">
      	</td>
    	</tr>
    </table>

  <input type="HIDDEN" name="Action" value="siap.siep.sentenzariunita.action.ActAssociaFascicoloSentenzaRiunita" >
  <input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>" value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
  <input type="HIDDEN" name="numsenriu" value="<%=numsenriu%>" >

  </FORM>
  <br>

</body>
</html>