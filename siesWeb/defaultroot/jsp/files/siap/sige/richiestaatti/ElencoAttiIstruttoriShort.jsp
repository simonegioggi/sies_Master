<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="atti"                 scope="request" class="java.util.Vector"/>
<jsp:useBean id="FascicoloSigeEsteso"  scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />

<td class="L">
  <table width="100%" >

<%
  if ( atti.size() == 0 )
  {
%>
        <td class="int" align="left"> Non ci sono richieste di atti per il procedimento.</td>
<%
  }else {
%>
    <tr>
      <td class="int" width="30%">Tipo atto istruttorio richiesto</td>
      <td class="int" width="40%">Destinatario</td>
      <td class="int" width="15%">Data richiesta</td>
      <td class="int" width="15%">Data restituzione</td>
    </tr>
<%
	Iterator itx = atti.iterator();

	while ( itx.hasNext())
	{
  
    	ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel)itx.next();
  
    	EventoNotificaModel lEveNot = lProvEve.getEventoNotifica();
    	NotificaModel[] lNotifiche = lEveNot.getNotifiche();

    for(int j = 0; j < lNotifiche.length; j++)	
    {%>
    
    <tr>
    
    	<td class="l"><font class="campo"><%=lEveNot.getEvento().getDescrMotivo() %></font></td>
        
        <td class="l"><font class="campo">
<%  
        if(lNotifiche[j].getUfficio() != null && lNotifiche[j].getUfficio().getCodUfficio().length() > 1)
        {
%>
			<%=lNotifiche[j].getUfficio().getDescrTipoUfficio() %>&nbsp;di&nbsp; <%=lNotifiche[j].getUfficio().getDescrComune()%>
<%
    	}
    	// Autorità Esterna
    	else if(lNotifiche[j].getAutoritaEsterna()!= null)
    	{
%>
	      	<%=lNotifiche[j].getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;di&nbsp; <%=lNotifiche[j].getAutoritaEsterna().getDescrSede()%>
<%
    	}
    	// Avvocato SIEP
    	else if(lNotifiche[j].getAvvIdAvvocatoFascicoloSige()!=null)
    	{
	      	if( lNotifiche[j].getAvvSiep() !=null)
      		{
%>
            	Avv. &nbsp;<%=StringUtils.toStringJSP(lNotifiche[j].getAvvSiep().getAvvocato().getCognome() +" "+lNotifiche[j].getAvvSiep().getAvvocato().getNome())%>&nbsp;
<%
      		}
    	}
    	// Avvocato SIGE
    	else if(lNotifiche[j].getAvvIdAvvocatoFascicoloSige()!=null)
    	{
	      	if(lNotifiche[j].getAvvSige() !=null)
      		{
%>
          		Avvocato &nbsp;<%=StringUtils.toStringJSP(lNotifiche[j].getAvvSige().getAvvocato().getCognome() +" "+lNotifiche[j].getAvvSige().getAvvocato().getNome())%>&nbsp;
<%
      		}
    	}
    	// UEPE
    	else if(lNotifiche[j].getCssIdCssa()!=null)
    	{
      		if( lNotifiche[j].getCSSA() !=null)
      		{
%>
				UEPE &nbsp;<%=StringUtils.toStringJSP(lNotifiche[j].getCSSA().getIndirizzo() +" "+lNotifiche[j].getCSSA().getComune())%>&nbsp;
<%
      		}
    	} // Gestione dell' Istituto di Detenzione.
    	else if( lNotifiche[j].getIstDetIdIstitutoDetenzione() != null )
    	{
      		if( lNotifiche[j].getIstitutoDetenzione() != null )
      		{
%>
        		<%=lNotifiche[j].getIstitutoDetenzione().getDescrTipoIstituto()%>&nbsp;di&nbsp; <%=lNotifiche[j].getIstitutoDetenzione().getDescrizione()%>
<%
      		}
    	}
%>
        </font></td>
      
        <td class="c">
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotifiche[j].getDataInvio(),"dd-MM-yyyy"),"-") %></font>
        </td>

      	<td class="c">
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotifiche[j].getDataAvvenutaNotifica(),"dd-MM-yyyy"),"-")%></font>
	  	</td>

    </tr>
<%
    } // end for

   } // end while
  } // end if atti.size()
%>
  </table>
</td>