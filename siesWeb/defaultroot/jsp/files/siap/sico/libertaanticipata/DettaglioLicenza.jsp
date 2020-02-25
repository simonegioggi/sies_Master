<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>


<jsp:useBean id="Licenze"     scope="request" class="java.util.Vector"/>

<%
  if (Licenze.size() > 0)
  {
    Iterator itx = Licenze.iterator();
    LicenzaLibAnticipataModel licenzalibanticipata = (LicenzaLibAnticipataModel) Licenze.get(0);
%>
    <table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> Licenza <td>
    </tr>

	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
                    <td class="l">IdLicenzaLibanticipata</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getIdLicenzaLibanticipata() %></font></td>
    </tr>
    <tr>
                    <td class="l">CodTipoLicenza</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodTipoLicenza() %></font></td>
    </tr--%>
    <tr>
   		<td class="l">Numero Mesi</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getNumeroMesi(),"-") %></font></td>
    </tr>    
    <tr>
   		<td class="l">Numero Giorni</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getNumeroGiorni(),"-") %></font></td>
    </tr>    
    <tr>
      <td class="l">Numero Ore</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getNumeroOre(),"-") %></font></td>
    </tr>
    <tr>
    	<td class="l">Data Inizio</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzalibanticipata.getDataInizio(),"dd-MM-yyyy"),"-")%> </font></td>
    </tr>
    <tr>
    	<td class="l">Ora Inizio</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getOraInizio(),"-") %></font></td>
    </tr>
    <tr>
    	<td class="l">Data Fine</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzalibanticipata.getDataFine(),"dd-MM-yyyy"),"-")%> </font></td>
    </tr>
    <tr>
      <td class="l">Ora Fine</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getOraFine(),"-") %></font></td>
    </tr>
    <tr>
      <td class="l">Luogo Svolgimento Prova</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(licenzalibanticipata.getLuogoSvolgimentoProva(),"-")%></font></td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
                    <td class="l">DataDetenzRifDa</td>
                    <td class="l"><font class="campo"><%=DateUtils.getDateToString(licenzalibanticipata.getDataDetenzRifDa(),"dd-MM-yyyy")%> </font></td>
    </tr>
    <tr>
                    <td class="l">DataDetenzRifA</td>
                    <td class="l"><font class="campo"><%=DateUtils.getDateToString(licenzalibanticipata.getDataDetenzRifA(),"dd-MM-yyyy")%> </font></td>
    </tr>
    <tr>
                    <td class="l">FlagInfrazioneObblighi</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFlagInfrazioneObblighi() %></font></td>
    </tr>
    <tr>
                    <td class="l">DataInfrazioneObblighi</td>
                    <td class="l"><font class="campo"><%=DateUtils.getDateToString(licenzalibanticipata.getDataInfrazioneObblighi(),"dd-MM-yyyy")%> </font></td>
    </tr>
    <tr>
                    <td class="l">DescrInfrazioneObblighi</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getDescrInfrazioneObblighi() %></font></td>
    </tr>
    <tr>
                    <td class="l">FlagScomputo</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFlagScomputo() %></font></td>
    </tr>
    <tr>
                    <td class="l">CodOperatoreInserimento</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodOperatoreInserimento() %></font></td>
    </tr>
    <tr>
                    <td class="l">DataInserimento</td>
                    <td class="l"><font class="campo"><%=DateUtils.getDateToString(licenzalibanticipata.getDataInserimento(),"dd-MM-yyyy")%> </font></td>
    </tr>
    <tr>
                    <td class="l">CodUfficioInserimento</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodUfficioInserimento() %></font></td>
    </tr>
    <tr>
                    <td class="l">CodOperatoreAggiornamento</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodOperatoreAggiornamento() %></font></td>
    </tr>
    <tr>
                    <td class="l">DataAggiornamento</td>
                    <td class="l"><font class="campo"><%=DateUtils.getDateToString(licenzalibanticipata.getDataAggiornamento(),"dd-MM-yyyy")%> </font></td>
    </tr>
    <tr>
                    <td class="l">CodUfficioAggiornamento</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodUfficioAggiornamento() %></font></td>
    </tr>
    <tr>
                    <td class="l">FasSiuIdFascicoloSius</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFasSiuIdFascicoloSius() %></font></td>
    </tr>
    <tr>
                    <td class="l">EveIdEvento</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getEveIdEvento() %></font></td>
    </tr>
    <tr>
                    <td class="l">FasSieIdFascicoloSiep</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFasSieIdFascicoloSiep() %></font></td>
    </tr>
    <tr>
                    <td class="l">FlagConcesso</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFlagConcesso() %></font></td>
    </tr>
    <tr>
                    <td class="l">FlagElaborato</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFlagElaborato() %></font></td>
    </tr>
    <tr>
                    <td class="l">FlagScorta</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getFlagScorta() %></font></td>
    </tr>
    <tr>
                    <td class="l">CodStatoPermesso</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getCodStatoPermesso() %></font></td>
    </tr>
    <tr>
                    <td class="l">DescrStatoPermesso</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getDescrStatoPermesso() %></font></td>
    </tr>
    <tr>
                    <td class="l">NumeroOre</td>
                    <td class="l"><font class="campo"><%=licenzalibanticipata.getNumeroOre() %></font></td>
    </tr--%>
    <tr> <td> <br></td></tr>
   </table>
<%
  }
%>