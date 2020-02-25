<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel" %>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil"%>

<jsp:useBean id="listaMisureSius" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="eventoUltimoPAS" scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="IdFascicoloSiep"  scope="request" class="java.lang.String"/>

<%-- 16/05/2008 richiesta conferma di cancellazione PAS in caso di Provvedimento validato --%>
<script language="JavaScript">
function cancella(idEvento, flagMotivo, flagDocumentoRegistrato, idPeriodoAltraMisura, idFascicoloSius )
{
    if ( flagDocumentoRegistrato == "S" && flagMotivo=="03")
    {
			if (window.confirm('Attenzione: Periodo agganciato ad un Decreto o Ordinanza. <br> Si vuole procedere con la cancellazione?'))
   		{
        str = "/jsp/Main.jsp?Action=siap.sius.misurasicurezza.action.ActLoadCancellaInizioMisuraSicurezzaUDS&<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA%>="+idPeriodoAltraMisura+"&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>="+idFascicoloSius;
        window.location.href=str;
      }
    }else{
        str = "/jsp/Main.jsp?Action=siap.sius.misurasicurezza.action.ActLoadCancellaInizioMisuraSicurezzaUDS&<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA%>="+idPeriodoAltraMisura+"&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>="+idFascicoloSius;
        window.location.href=str;
    }

}

</script>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<%	if (Utils.isPresent(IdFascicoloSiep))
	{%>    
    	<title>[S.I.E.P.] - Misure Sicurezza</title>
<%	}
	else
	{
	%> 
		<title>[S.I.U.S.] - Misure Sicurezza</title>	
<%	} %>	   	
  </head>

<body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo"> Elenco Periodi Misura Sicurezza</font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int">Data Decorrenza</td>
      <td class="int">Data Scadenza </td>
      <td class="int">Durata Misura Sicurezza</td>
      <td class="int">Note</td>
      <td class="int">Motivo</td>
  <%   	Object lObj = null;
      	lObj = session.getAttribute("fascicoloSiusGP");
      	if( lObj != null )   {    %>
      		<td class="int">Azione</td>
   <%  	}
    %>
     </tr>
<%
    if( !listaMisureSius.isEmpty() )
    {
      Iterator itx = listaMisureSius.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
    	PeriodoAltraMisuraModel lPeriodoAltraMisuraModel = (PeriodoAltraMisuraModel)itx.next();

%>
        <tr>
          <td class="c">
    	      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraMisuraModel.getDataInizioEsecuzione(),"dd-MM-yyyy"),"-")%>
          </td>
          <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodoAltraMisuraModel.getDataScadenza(),"dd-MM-yyyy"), "-")%>
          </td>
<%
		CalendarModel lCal = new CalendarModel();
		lCal.setDataInizio(lPeriodoAltraMisuraModel.getDataInizioEsecuzione());
		lCal.setDataFine(lPeriodoAltraMisuraModel.getDataScadenza());
		CalendarUtil lCalUtil=new CalendarUtil();
		lCal=lCalUtil.CalcolaNumGiorniMesiAnni(lCal, false);
    // metto false altrimenti in fase di elenco periodi MS sbaglia il calcolo
    // ossia manca un giorno  paolo c. 23/04/2010
    
		if( CalendarUtil.getTotGiorni(lCal)>=0 ) {
	        %>
	          <td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=lCal.getNumAnni()%></font>
	              <font class="label">Mesi</font>
	              <font class="campo"> <%=lCal.getNumMesi()%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"> <%=lCal.getNumGiorni()%></font>&nbsp;
	          </td>
          <td class="c">
            <%=StringUtils.toStringJSP(lPeriodoAltraMisuraModel.getMotivazione(), "-")%>
          </td>
           <td class="c">
            <%=StringUtils.toStringJSP(lPeriodoAltraMisuraModel.getDescrMotivo(), "-")%>
          </td>
          <td class="c">
<%           if (i == (listaMisureSius.size() -1)) {

				 if( lObj != null ) {
%>
       			<%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.misurasicurezza.action.ActLoadCancellaInizioMisuraSicurezzaUDS&<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA%>=<%=lPeriodoAltraMisuraModel.getIdPeriodoAltraMisura()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lPeriodoAltraMisuraModel.getFasSiuIdFascicoloSius()%>">
                    <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a--%>
             <a class="cliccabile" href="javascript:cancella('<%=lPeriodoAltraMisuraModel.getEveIdEvento()%>', '<%=lPeriodoAltraMisuraModel.getFlagMotivo()%>', '<%=eventoUltimoPAS.getFlagDocumentoRegistrato()%>', '<%=lPeriodoAltraMisuraModel.getIdPeriodoAltraMisura()%>', '<%=lPeriodoAltraMisuraModel.getFasSiuIdFascicoloSius()%>');" title="Cancella">
             <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0"></a>

<% 				}
			} %>
              </td>
	     </tr>
	        <%
	        }
	      }
	    }
%>
    </table>
  </form>
</body>
</html>