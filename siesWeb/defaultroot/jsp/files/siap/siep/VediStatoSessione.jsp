<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<jsp:useBean id="penaresidua" scope="session" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%
  FascicoloSiepModel fascicolo=(FascicoloSiepModel)session.getAttribute("fascicolo");
  SentenzaModel sentenza=(SentenzaModel)session.getAttribute("sentenza");
  SoggettoModel soggetto=(SoggettoModel)session.getAttribute("soggetto");
  String LastFunctionID=(String)session.getAttribute("LastFunctionID");
%>

<html>
<head>
<title> [S.I.E.S.] - Dettaglio Stato Sessione - </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	function Dettaglio(id)
	{
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>="+id;
    window.close();
	}

  function CleanIT()
	{
    window.opener.top.frames['centrale'].frames['body'].location.href="/html/blankGray.htm";
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.sessionstate.action.ActCleanWorkSessionSIEP";
	}
</script>
</head>

<BODY class="corpo">
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="LBG"><font class="label">Dati Disponibili nella Sessione di Lavoro</font></td></tr>
  </table>
  <br>
<!-- fascicolo -->
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Ultimo Fascicolo Trattato</font></td></tr>
  </table>
<%
  if (fascicolo != null && fascicolo.getIdFascicoloSiep() != null)
  {
	  SoggettoModel Fsoggetto = fascicolo.getSoggetto();
	  SentenzaModel Fsentenza = fascicolo.getSentenza();
%>
	  <table cellspacing=0 cellpadding=0 width=95%>
	    <tr>
	      <td class="L">
	        <font class="label">Procedimento : N.</font>
          <a class="cliccabile" href="javascript:Dettaglio('<%=fascicolo.getIdFascicoloSiep()%>');" title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>&nbsp;
<%
          if(fascicolo.getFlagCumulante() != null && fascicolo.getFlagCumulante().equals("S"))
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

          if(   ( penaresidua != null
               && penaresidua.getFlagPenaSospesa()!= null
               && penaresidua.getFlagPenaSospesa().equals("S") )
               || (     fascicolo != null && fascicolo.getChiaveProgr() != null
                    && ( fascicolo.getChiaveProgr().intValue() >= 30000
                    &&   fascicolo.getChiaveProgr().intValue() < 40000) ) )
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
	      </td>
	    </tr>
	    <tr>
	      <td class="L" width=100%><font class="label">Soggetto :</font>
	      <font class="campo">
          <%=Fsoggetto.getCognome()%>&nbsp;<%=Fsoggetto.getNome()%>
	      </font>&nbsp;
<%
	        if (Fsoggetto.getSesso().compareTo("F")==0)
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
%>
	      <font class="campo"><%=DateUtils.getDateToString(Fsoggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
	      <font class="label">in : </font>
	      <font class="campo">
<%
	      if (Fsoggetto.getDescrComuneNascita().compareTo("-")==0)
	      {
%>
	        <%=Fsoggetto.getDescrStatoNascita()%>
<%
	      }
	      else
	      {
%>
	        <%=Fsoggetto.getDescrComuneNascita()+ "  ("+Fsoggetto.getCodProvinciaNascita()+")" %>
<%
	      }
%>
	      </font>
	     </td>
	    </tr>
	    <tr>
	      <td class="L">
	        <font class="label"><%=Fsentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+Fsentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
	        <font class="campo">
	          <%=Fsentenza.getAnnoSentenza()%> / <%=Fsentenza.getNumeroSentenza()%>&nbsp;
	          <font class="label">del</font>&nbsp;
	            <%=DateUtils.getDateToString(Fsentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
	        </font>
			<% if(!Fsentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
			 }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
	        <font class="campo"><%=Fsentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
	        if (Fsentenza.getNumSezioneAutoritaEmittente() != null)
	        {
%>
	          <font class="label">(Sez.</font> <font class="campo"><%=Fsentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
	        }
%>
	        <font class="label"> di </font>
	        <font class="campo"><%=Fsentenza.getDescrLuogoEmittente()%></font>
	      </td>
	    </tr>
	    <tr>
	      <td class="L">
	        <font class="label">Data irrevocabilità : </font>&nbsp;
	        <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
	      </td>
	    </tr>
	  </table>
<%
	}
  else //fascicolo=null
	{
%>
		<table cellspacing=0 cellpadding=0 width=95%>
  		<tr><td><font class="label">Nessun fascicolo attualmente disponibile</font></td></tr>
		</table>
<%
	}
%>
  <table><tr><td><br>&nbsp;</td></tr></table>
<!-- fine fascicolo -->

<!-- sentenza -->
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Ultima Sentenza Trattata</font></td></tr>
  </table>
<%
 if (sentenza !=null)
 {
%>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
      <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp; :<font class="label"> N.</font>
      <font class="campo">
        <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
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
	  <%-- tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr---%>
  </table>
<%
	}
  else //sentenza=null
	{
%>
		<table cellspacing=0 cellpadding=0 width=95%>
  		<tr><td><font class="label">Nessuna sentenza attualmente disponibile</font></td></tr>
		</table>
<%
	}
%>
  <table><tr><td><br>&nbsp;</td></tr></table>
<!-- fine sentenza -->


<!-- soggetto -->
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Ultimo Soggetto Trattato</font></td></tr>
  </table>
<%
  if (soggetto !=null)
  {
%>
    <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto :</font>
      <font class="campo">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
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
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>

 </table>
<%
  }
  else //soggetto=null
	{
%>
    <table cellspacing=0 cellpadding=0 width=95%>
      <tr><td><font class="label">Nessun soggetto attualmente disponibile</font></td></tr>
    </table>
	<%
	}
%>
<!-- fine soggetto -->
<br><br>
  <table width=100%>
    <tr>
      <td class="C" width=100%>
        <a class=cliccabile href="Javascript:CleanIT();">Pulisci sessione di lavoro</a>
      </td>
    </tr>
  </table>
  <table width=100%>
    <tr>
      <td valign=bottom class=small align=center><br><b>ID : <%=LastFunctionID%></b></td>
    </tr>
  </table>
<table width=100%>
    <tr>
      <td valign=bottom class=c><input style=bottone value="Chiudi" type=button onclick="Javascript:window.close()"></td>
    </tr>
  </table>
</body>
</html>
<script language="Javascript">
window.focus();
</script>