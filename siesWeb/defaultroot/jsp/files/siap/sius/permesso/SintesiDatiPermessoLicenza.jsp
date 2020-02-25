<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoModel"%>

<%--jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.PermessoDepositoDecretoModel"/--%>
<jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel"/>

<%
	String lDescrizione = "",lCodTipoLicenza = "", lDescrMotivo = "";
	lCodTipoLicenza = permessoDepDecr.getLicenza().getCodTipoLicenza();
	lDescrMotivo = permessoDepDecr.getEvento().getDescrMotivo();
	if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA) )
	  lDescrizione = lDescrMotivo + " concessa con:";
	else if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO) )
	  lDescrizione = lDescrMotivo + " concesso con:";  
%>	

	<table cellspacing=2 cellpadding=2 width="95%">
  	<tr>
  		<td class="Titolo" colspan=6><%=lDescrizione%></td>
  	</tr>
<%
//    LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenzaLibAnticipata();
		LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenza();
//		DepositoDecretoModel lDep = permessoDepDecr.getDepositoDecretoMotivazioni().getDepositoDecreto();
		DepositoDecretoModel lDep = permessoDepDecr.getDepositoDecreto();
%>
    <tr>
    	<td class="l">DECRETO N.</td>
      <td class="l">
      	<font class="campo"><%=StringUtils.toStringJSP(lDep.getAnnoS72() + "/" + lDep.getNumS72())%></font>
      </td>
    </tr>
          
    <tr>
      <td class="l">Data Emissione </td>
      <td class="l">
      	<font class="campo"><%=DateUtils.getDateToString(lDep.getDataEmissione(), "dd/MM/yyyy")%></font>
      </td>
    </tr>
          
    <tr>
      <td class="l">Data Deposito </td>
      <td class="l">
      	<font class="campo"><%=DateUtils.getDateToString(lDep.getDataDeposito(), "dd/MM/yyyy")%></font>
      </td>
    </tr>

    <tr>
    	<td class="l">Durata</td>
      <td class="l">
      	<% if(lLic.getNumeroGiorni() != null) {%> giorni <font class="campo"><%=StringUtils.toStringJSP(lLic.getNumeroGiorni(),"-") + " "%> </font> <% } if(lLic.getNumeroOre() != null) {%> ore <font class="campo"> <%=" " + StringUtils.toStringJSP(lLic.getNumeroOre(),"-")%> </font> <%}%>
      </td>
    </tr>

    <tr>
<% 
		if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA) )
		{
%>
			<td class="l">Luogo Licenza</td>
<%
		}
		else if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO) )
		{
%>
			<td class="l">Luogo Permesso</td>
<% 
		}
%>		
      <td class="l"> 
      	<font class="campo"><%=StringUtils.toStringJSP(lLic.getLuogoSvolgimentoProva(),"-")%></font>
      </td>
   	</tr>
    
    <tr>
      <td class="l">Presenza Scorta</td>
<%         
					if (lLic.getFlagScorta().toUpperCase().compareTo("N") == 0)
          {
%>							
						<td class="l">
							<font class="campo">No</font>
						</td>
<%
          }
          else
          {
%>
           	<td class="l">
           		<font class="campo">Si</font>
           	</td>
<%
					}
%>
    </tr>

    <tr>
      <td class="l">Motivazione provvedimento</td>
      <td class="l">
      	<font class="campo"><%=StringUtils.toStringJSP(lDep.getNote(),"-")%></font>
      </td>
    </tr>
 </table>  