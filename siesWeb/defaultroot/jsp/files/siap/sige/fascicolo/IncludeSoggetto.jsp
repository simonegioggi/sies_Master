<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>



<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FlagFasSiepe"     scope="request" class="java.lang.String"/>

<%
  SoggettoModel soggetto =FascicoloSigeEsteso.getSoggetto();
  FascicoloSiepModel fascicolo = FascicoloSigeEsteso.getFascicoloSiep();
  // richiesto per MEV_57
  String comuneEsteroNascita ="";
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String nomeSoggetto="";
  String cognomeSoggetto="";
  if (soggetto != null) {
	  nomeSoggetto=soggetto.getNome();
	  cognomeSoggetto=soggetto.getCognome();
  }
  
  boolean isIgnoto=true;
  
  if (!nomeSoggetto.equalsIgnoreCase("IGNOTO") && !cognomeSoggetto.equalsIgnoreCase("IGNOTO"))
	  isIgnoto=false;
	  
  String codCui="";
  
  if (soggetto.getCodAfis() != null)
       codCui = soggetto.getCodAfis();
  
  
%>
 <% if (soggetto != null) {
	 if (!isIgnoto) {
 %>
       <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%><%=retParam%>">
          <%=cognomeSoggetto%>&nbsp;<%=nomeSoggetto%>
        </a>
      </font>&nbsp;
<%
	 } else {
%>
     <font class="campo">
        <%=cognomeSoggetto%>&nbsp;<%=nomeSoggetto%>
      </font>&nbsp;
<% 		 
	 }
        if (!isIgnoto) {

            if (soggetto.getSesso().compareTo("F")==0)
            {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }else {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"), "-")%></font>&nbsp;
      <font class="label">in : </font>
      
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0) {
    	if(soggetto.getDescComuneNascitaEstero().compareTo("-")!=0){
    		comuneEsteroNascita = soggetto.getDescComuneNascitaEstero();
    	}
%>
        <font class="campo"><%=comuneEsteroNascita + "  ("+  soggetto.getDescrStatoNascita() +")" %></font>
        <%-- Ticket#20220628015 -  non visualizza il codice CUI in quanto la sezione si trova nell'if del comune   --%>
       <%-- 
       <font class="label">Cod Cui : </font> 
        &nbsp;
        <font class="campo">
        <%=codCui%>
        --%>
        
        
<%
      }
      else
      {
%>
        <font class="campo"><%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %></font>
<%
      }

  }
%>
   <%-- Ticket#20220628015 - Visualizzazione CUI portata fuori dall'IF--%>
       <font class="label">Cod Cui : </font> 
        &nbsp;
        <font class="campo"><%=codCui%></font>
   <%-- Ticket#20220628015 - FINE --%>

 <% } %>