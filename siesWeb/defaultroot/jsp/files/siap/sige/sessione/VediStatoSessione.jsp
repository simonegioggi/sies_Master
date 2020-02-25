<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>
<%@ page import="java.util.Enumeration" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="MemoriaUtilizzata" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel fascicolo=(FascicoloSiepModel)session.getAttribute("fascicolo");
  SentenzaModel sentenza=(SentenzaModel)session.getAttribute("sentenza");
  SoggettoModel soggetto=(SoggettoModel)session.getAttribute("soggetto");
  FascicoloSigeEstesoModel fasSigeEsteso =(FascicoloSigeEstesoModel)session.getAttribute("FascicoloSigeEsteso");
  String LastFunctionID=(String)session.getAttribute("LastFunctionID");
%>

<html>
<head>
<title> [S.I.E.S.] - Dettaglio Stato Sessione SIGE - </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        function Dettaglio(id)
        {
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>="+id;
    window.close();
        }

        function DettaglioSIGE(id)
        {
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>="+id;
    window.close();
        }

  function CleanIT()
        {
    window.opener.top.frames['centrale'].frames['body'].location.href="/html/blankGray.htm";
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.sessionstate.action.ActCleanWorkSessionSIGE";
        }
</script>
</head>

<BODY class="corpo">

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="LBG"><font class="label">Dati Disponibili nella Sessione di Lavoro</font></td></tr>
  </table>
  <br>


<!-- procedimento SIGE -->
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Ultimo Procedimento SIGE Trattato</font></td></tr>
  </table>
<%
  if (fasSigeEsteso !=null && fasSigeEsteso.getFascicoloSige() != null && fasSigeEsteso.getFascicoloSige().getIdFascicoloSige() != null )
  {
%>
          <table cellspacing=0 cellpadding=0 width=95%>
            <tr>
              <td class="L">
                <font class="label">Procedimento : N.</font>
            <a class="cliccabile" href="javascript:DettaglioSIGE('<%=fasSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>');" title="Procedimento">
              <%=fasSigeEsteso.getFascicoloSige().getChiaveAnno()%>
              /
              <%=fasSigeEsteso.getFascicoloSige().getChiaveProgr()%>
            </a>
              </td>
            </tr>
            <%if (fasSigeEsteso.getSoggetto() != null) { %>
            <tr>
              <td class="L" width=100%><font class="label">Soggetto :</font>
              <font class="campo">
          <%=fasSigeEsteso.getSoggetto().getCognome()%>&nbsp;<%=fasSigeEsteso.getSoggetto().getNome()%>
              </font>&nbsp;
<%
                if (fasSigeEsteso.getSoggetto().getSesso().compareTo("F")==0)
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
              <font class="campo"><%=DateUtils.getDateToString(fasSigeEsteso.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
              <font class="label">in : </font>
              <font class="campo">
<%
              if (fasSigeEsteso.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
              {
%>
                <%=fasSigeEsteso.getSoggetto().getDescrStatoNascita()%>
<%
              }
              else
              {
%>
                <%=fasSigeEsteso.getSoggetto().getDescrComuneNascita()+ "  ("+fasSigeEsteso.getSoggetto().getCodProvinciaNascita()+")" %>
<%
              }
%>
              </font>
             </td>
            </tr>
            <% } // endif soggetto %>
          </table>
<%
        }
  else //fascicolo=null
        {
%>
                <table cellspacing=0 cellpadding=0 width=95%>
                <tr><td><font class="label">Nessun procedimento SIGE attualmente disponibile</font></td></tr>
                </table>
<%
        }
%>
  <table><tr><td><br>&nbsp;</td></tr></table>
<!-- fine procedimento SIGE -->



<!-- fascicolo -->
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Ultimo Procedimento SIEP Trattato</font></td></tr>
  </table>
<%
  if (fascicolo != null && fascicolo.getIdFascicoloSiep() != null)
  {
    SoggettoModel Fsoggetto =null;
    SentenzaModel Fsentenza = null;
    if (fascicolo.getSoggetto() !=null)
          {Fsoggetto = fascicolo.getSoggetto();}
    if (fascicolo.getSentenza() !=null)
          {Fsentenza = fascicolo.getSentenza();}
%>
          <table cellspacing=0 cellpadding=0 width=95%>
            <tr>
              <td class="L">
                <font class="label">Procedimento : N.</font>
            <a class="cliccabile" href="javascript:Dettaglio('<%=fascicolo.getIdFascicoloSiep()%>');" title="Procedimento">
              <%=fascicolo.getChiaveAnno()%>
              /
              <%=fascicolo.getChiaveProgr()%>
            </a>
              </td>
            </tr>
<%  if (Fsoggetto !=null) {%>

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
<%  }
    if (Fsentenza !=null ) {%>
            <tr>
              <td class="L">
                <font class="label"><%=Fsentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+Fsentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
                <font class="campo">
                  <%=Fsentenza.getAnnoSentenza()%> / <%=Fsentenza.getNumeroSentenza()%>&nbsp;
                  <font class="label">del</font>&nbsp;
                    <%=DateUtils.getDateToString(Fsentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
                </font>
                &nbsp;<font class="label"> Emessa da: </font>
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
            <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
            <%--
            //  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
            tr>
              <td class="L">
                <font class="label">Data irrevocabilità: </font>&nbsp;
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Fsentenza.getDataIrrevocabilita(), "dd-MM-yyyy"), "-")%></font>
              </td>
            </tr--%>
          </table>
<%
  }

        }
  else //fascicolo=null
        {
%>
                <table cellspacing=0 cellpadding=0 width=95%>
                <tr><td><font class="label">Nessun procedimento SIEP attualmente disponibile</font></td></tr>
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
      <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp; :
<%
			if (sentenza.getNumeroSentenza()!=null && sentenza.getNumeroSentenza().length()>0)
			{%>
 				<font class="label"> N.</font>
				<font class="campo"> 
					<%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
				</font>
		<%}else{%>
				<font class="cRosso">
 						NON NUMERATA 
				</font>&nbsp;&nbsp;
		<%}%>

      <font class="label">del</font>&nbsp;
      <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
      </font>
      &nbsp;<font class="label"> Emessa da: </font>
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
	<%--  
	//  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
	tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy"), "-" )%></font>
      </td>
    </tr--%>
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
<% if (MemoriaUtilizzata != null && MemoriaUtilizzata.length() > 0) { %>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class=LBG><font class="label">Memoria java in uso <%=MemoriaUtilizzata%> %</font></td></tr>
 <%
        // Ritorna elenco chiavi
        Enumeration lEnum = System.getProperties().keys();
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( " - Elenco delle Properties - ");

        while( lEnum.hasMoreElements() )
        {
            // Nome della chiave.
            String lKey = (String)lEnum.nextElement();
            // Stampa chiave valore.
     boolean debug = false; // flag per attivare la visualizzazione
     if (debug)
    {
%>
   <tr><td ><font class="label"><%=lKey%> = </font> <font class="crosso"> <%=System.getProperty(lKey)%></font></td></tr>
<%
     }
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( lKey + " = " + System.getProperty(lKey)  );
        }
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( " - ----------------- - ");

%>


 </table>
<% } %>
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