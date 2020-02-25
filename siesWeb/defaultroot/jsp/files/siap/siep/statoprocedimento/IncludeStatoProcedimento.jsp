<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.statoprocedimento.model.StatoProcedimentoModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="fascicolo"          scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="FascCompetenteCumulo"    scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<font class="label">Stato Procedimento : </font>
<%
  List lListStatProc = dettagliofascicolo.getStatoProcedimento();
  Iterator lIterProc = lListStatProc.iterator();
  for(int i=0;i<lListStatProc.size();i++)
  {
    StatoProcedimentoModel lStatoProcMod = (StatoProcedimentoModel)lIterProc.next();
   if("0315".equals(lStatoProcMod.getCodStatoProcedimento()) || 
       "0316".equals(lStatoProcMod.getCodStatoProcedimento()))
    {	
   	 	if (FascCompetenteCumulo.getIdFascicoloSiep()!=null && "0315".equals(lStatoProcMod.getCodStatoProcedimento()) ) 
   	 	{	%>
 			<font color="red"> Archiviazione per assorbimento in cumulo in data: <%=StringUtils.toStringJSP(DateUtils.getDateToString (lStatoProcMod.getData(),"dd-MM-yyyy"))%>&nbsp;
 								N.SIEP: <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascCompetenteCumulo.getIdFascicoloSiep()%>" title="Procedimento Competente al Cumulo">
          								<%=StringUtils.toStringJSP(fascicolo.getAnnoFascicoloUnione())%>/<%=StringUtils.toStringJSP(fascicolo.getNumFascicoloUnione())%>
        								</a>&nbsp;
 								Data Cumulo: <%=StringUtils.toStringJSP(DateUtils.getDateToString (fascicolo.getDataUnione(),"dd-MM-yyyy"))%>&nbsp;
 			</font>
<% 	 	}
   	 	else if(FascCompetenteCumulo.getIdFascicoloSiep()!=null && "0316".equals(lStatoProcMod.getCodStatoProcedimento()))
   	 	{	%>
   	 		<font color="red"> Archiviazione per assorbimento in cumulo in data: <%=StringUtils.toStringJSP(DateUtils.getDateToString (lStatoProcMod.getData(),"dd-MM-yyyy"))%>&nbsp;
   	 							Ufficio: <%=StringUtils.toStringJSP(fascicolo.getDescrTipoUfficioUnione())%>
   	 							Sede: <%=StringUtils.toStringJSP(fascicolo.getDescrComuneUfficioUnione())%>
 								N.SIEP: <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascCompetenteCumulo.getIdFascicoloSiep()%>" title="Procedimento Competente al Cumulo">
          								<%=StringUtils.toStringJSP(fascicolo.getAnnoFascicoloUnione())%>/<%=StringUtils.toStringJSP(fascicolo.getNumFascicoloUnione())%>
        								</a>&nbsp;
 								Data Cumulo: <%=StringUtils.toStringJSP(DateUtils.getDateToString (fascicolo.getDataUnione(),"dd-MM-yyyy"))%>&nbsp;
 			</font>
<% 		}   	 		
   	 	else
   	 	{	
		    String lStato = lStatoProcMod.getDescrStatoProcedimento();
		    lStato = lStato.replaceFirst("<data definizione>",DateUtils.getDateToString(lStatoProcMod.getData(),"dd-MM-yyyy"));	
		    lStato = lStato.replaceFirst("<numero Siep>",fascicolo.getAnnoFascicoloUnione()+"/"+fascicolo.getNumFascicoloUnione());	
		    
		    lStato = lStato.replaceFirst("<data cumulo>",StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"dd-MM-yyyy"),""));	
		    if("0316".equals(lStatoProcMod.getCodStatoProcedimento()))
		    {
		        lStato = lStato.replaceFirst("<dati procura>",fascicolo.getDescrTipoUfficioUnione());	
		        lStato = lStato.replaceFirst("<sede procura>",fascicolo.getDescrComuneUfficioUnione());	
		   	
		    }
%>
    		<font color=red><%=lStato%></font>
<%		}
   	 	
    }else{
%>
    <font color=red><%=StringUtils.toStringJSP(lStatoProcMod.getDescrStatoProcedimento())%></font>
<%

     if(lStatoProcMod.getData() != null)
     { 
%>    
      <font class="label"> : </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoProcMod.getData(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;
<%
     }
    }
    
    
  if(lStatoProcMod != null && lStatoProcMod.isCodiceTrasmissione() && dettagliofascicolo.getEventi() != null && !dettagliofascicolo.getEventi().isEmpty())
  {
  EventoNotificaModel lEveNot = (EventoNotificaModel)dettagliofascicolo.getEventi().get(0);
    if(   lEveNot != null
       && lEveNot.getEvento() != null
       && lEveNot.getEvento().getIdEvento() != null
       && lEveNot.getNotifiche() != null
       && lStatoProcMod != null
       && lStatoProcMod.getEveIdEvento() != null
       && lEveNot.getEvento().getIdEvento().compareTo(lStatoProcMod.getEveIdEvento()) == 0)
    {
      for(int j=0;j<lEveNot.getNotifiche().length;j++)
      {
    	  
    	  
        NotificaModel lNotMod = lEveNot.getNotifiche()[j];
        if(j != 0)
        {
%>
          ,
<%
        }

        if(lNotMod != null && lNotMod.getUfficio() != null && lNotMod.getCodTipoNotifica().equals("E")
          && lNotMod.getUfficio().getDescrTipoUfficio() != null
          && !lNotMod.getUfficio().getDescrTipoUfficio().equals(""))
        {
%>
          a<font class="campo"> <%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%> di
          <%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>

<%
  }
  else if(lNotMod != null && lNotMod.getUfficio() != null && lNotMod.getUfficio().getCodTipoUfficio().equals("UDS")
     && lNotMod.getUfficio().getDescrComune() != null
     && !lNotMod.getUfficio().getDescrComune().equals(""))
  {
%>

         a<font class="campo"> Magistrato di Sorveglianza  di <%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>

<%
  }
  else if(lNotMod != null && lNotMod.getUfficio() != null && lNotMod.getUfficio().getCodTipoUfficio().equals("TDS")
     && lNotMod.getUfficio().getDescrComune() != null
     && !lNotMod.getUfficio().getDescrComune().equals(""))
  {
%>
      a<font class="campo"> Tribunale di Sorveglianza di <%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>

<%
  }
  else if(lNotMod != null && lNotMod.getUfficio()!= null && lNotMod.getUfficio().getDescrTipoUfficio() != null
     && !lNotMod.getUfficio().getDescrTipoUfficio().equals(""))
  {
%>
	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      a <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%>&nbsp;<%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>

<%
  }
  else if(lNotMod != null && lNotMod.getIstitutoDetenzione() != null
          && lNotMod.getIstitutoDetenzione().getDescrTipoIstituto() != null
          && !lNotMod.getIstitutoDetenzione().getDescrTipoIstituto().equals(""))
  {
%>

         a <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrTipoIstituto())%>&nbsp;di
         <%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font>

<%
  }
 else if(lNotMod != null && lNotMod.getAutoritaEsterna() != null
    && lNotMod.getAutoritaEsterna().getCodTipoAutorita() != null
    && lNotMod.getAutoritaEsterna().getCodSede()!= null)
{%>


    a <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
 <%if(!lNotMod.getAutoritaEsterna().getDescrSede().equals("-"))
{%>
 <font class="campo"> di <%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede()) %></font>
<%}%>



<%if(lNotMod.getNote() != null && !lNotMod.getNote().equals(""))
    {%>

     <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>

<%  }
    }
   }
  }
 }
}
%>