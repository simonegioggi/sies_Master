<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>


<jsp:useBean id="IstruttoriaCumulo"       scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="fascicoloTrovato"        scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="ListaIstruttorieCumulo"  scope="request" class="java.util.Vector"/>

<jsp:useBean id="ListaTitoli"             scope="request" class="java.util.Vector"/>
<%
//==============================================================================
//             MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
//         Form per l'Annullamento/trasferimento di un'istruttoria cumulo
//==============================================================================
%>

<% 
String msgConfirm = "Procedendo l'istruttoria corrente verra' chiusa. ";
if (ListaIstruttorieCumulo.size()>0) {
  IstruttoriaCumuloModel lIstrCumuloTrovata = (IstruttoriaCumuloModel) ListaIstruttorieCumulo.elementAt(0);
  if (lIstrCumuloTrovata.getFlagStato().equals("A"))
      msgConfirm += "I dati dei titoli dell'istruttoria corrente verranno importati nell'istruttoria gia' aperta sul procedimento indicato. Confermi?";
  else
      msgConfirm += "Verra' aperta una nuova istruttoria sul procedimento indicato ed importati i titoli dell'istruttoria corrente. Confermi?";
}
else
    msgConfirm += "Verra' aperta una nuova istruttoria sul procedimento indicato ed importati i titoli dell'istruttoria corrente. Confermi?";
%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Form di Ricerca
    //==========================================================================
    function tornaIndietro(action)
    {  
      document.TrasferisciIstruttoria.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.TrasferisciIstruttoria.submit();
    }
    
    function Verify()  
    {
        var msgConfirm = "<%=msgConfirm%>";
        if (window.confirm(msgConfirm)){
        	return true;
        }
        else {
        	return false;
        }
        return true;
     }
  </script> 
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Trasferimento Istruttoria Cumulo</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadTrasferisciIstruttoria')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>  
 
 

  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
        <%-- Inserire qui le intestazioni delle colonne che si vogliono visualizzare --%>
        <td class="int">Titolo</td><!-- Sentenza/Decreto/Cumulo-->
        <td class="int">N&deg;</td>
        <td class="int">Data Titolo</td>
        <td class="int">Autorita' Emittente</td>
        <td class="int">Anno/Numero <br>Reg.Gen.</td>
        <td class="int">Definitivo il</td>
        <td class="int">Anno/Numero <br>SIEP</td>
        <td class="int">Autorita'</td>
    </tr>
<%
{ // Apertura blocco per evitare duplicazone variabili
boolean isTitoloManuale = false;
int id_record = 0;
Iterator itxTitoliInIstruttoriaCorrente = ListaTitoli.iterator();
while (itxTitoliInIstruttoriaCorrente.hasNext()) {
    id_record = id_record+1;
    TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) itxTitoliInIstruttoriaCorrente.next();
    boolean isTitoloCumulante = false;
    if (lTitoloModel.getProcedimentoCumulato() != null
            && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null
            && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine().compareTo(IstruttoriaCumulo.getFasSieIdFascicoloSiep()) == 0) {
        isTitoloCumulante = true;
    }
    String reg = StringUtils.toStringJSP(lTitoloModel.getTipoRegGen(), "");
    String anno_reg = StringUtils.toStringJSP(lTitoloModel.getAnnoRegGen(), "");
    String num_reg = StringUtils.toStringJSP(lTitoloModel.getNumeroRegGen(), "");
    String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente() + " di " + lTitoloModel.getDescrLuogoEmittente();
    if (lTitoloModel.getNumSezioneAutoritaEmittente() != null)
        AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();
    String nSiep = "";
    String AutoritaSiep = "";
    ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
    if (lProcedimentoCumulatoModel != null) {
        if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato())) {
            UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();
            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() + "/" + lProcedimentoCumulatoModel.getChiaveProgrOrigine();
            nSiep += "<br> <font class=\"cRosso\">(Ex " + lUfficioOrigine.getCodTipoUfficio() + " di " + lUfficioOrigine.getDescrComune() + ")</font>";
        } else {
            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
        }
        AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
    }
    
    if (isTitoloCumulante) {
%> 
    <tr style="background-color: rgb(255,255,153);">
<%
    } else {
%>
    <tr>
<%
    }
    // Grigio i dati dei procedimenti momentaneamente esclusi dal cumulo
    String lFontColor = "";
    if ("S".equals(lTitoloModel.getFlagEscluso())) {
        lFontColor = "style='color:grey;'";
    }
%>
        <td class="c" nowrap>
<% 
    String lDescrtipoTitolo = lTitoloModel.getDescrTipoProvvedimento();
    if ("02".equals(lTitoloModel.getCodTipoProvvedimento())) {
        String [] lUfficiSorv = new String[] {"UDS", "TDS", "UDSM"};
        if (!Arrays.asList(lUfficiSorv).contains(lTitoloModel.getCodTipoAutoritaEmittente())) {
            lDescrtipoTitolo = "Decreto Penale";
        }
    }
    String tipoCaricamento = "";
    if (lTitoloModel.getIdSentenzaOrigine() != null || "03".equals(lTitoloModel.getTipoIscrizione())) {
        tipoCaricamento = "";
    } else {
        tipoCaricamento = " <font class=\"label\"  style=\"font-size:8px;vertical-align: super;\" >(*)</font>";
        isTitoloManuale = true;
    }
%>
                <%=lDescrtipoTitolo%><%=" " + tipoCaricamento%>
        </td>
        <td class="c" <%=lFontColor%> nowrap>          
            <%=lTitoloModel.getAnnoSentenza()%> / <%=lTitoloModel.getNumeroSentenza()%>
        </td>
        <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
        <td class="c" <%=lFontColor%>>&nbsp;<%=AutEmi%></td>
        <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=anno_reg%>/<%=num_reg%>&nbsp;<%=reg%></td>
        <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataIrrevocabilita(),"dd-MM-yyyy"))%></td>
<%
    if (lProcedimentoCumulatoModel != null) {
%>
        <td class="c" <%=lFontColor%> nowrap><%=nSiep%></td>
<%
    } else {
%>
        <td class="c" <%=lFontColor%> nowrap>&nbsp;</td>
<%
    }
%>
        <td class="c" <%=lFontColor%> >&nbsp;<%=AutoritaSiep%></td>
    </tr>
<%
    } // End while
} // chiusura blocco per evitare duplicazone variabili
%>        
  </table>
  
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="TrasferisciIstruttoria">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActTrasferisciIstruttoria">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
    <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=fascicoloTrovato.getIdFascicoloSiep()%>">
    
    <%-- Eventuale Istruttori del nuovo fascicolo --%>
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>_target" value="">

    <table cellpadding="2" cellspacing="2"  width="85%" align="center">
        <tr><td width="90%" class="Titolo">Esito Ricerca Procedimento su cui trasferire l'istruttoria</td></tr>
        <tr>
            <td>
                <table cellspacing="2" cellpadding="2" width="100%">
	                <tr>
	                  <td class="L"> 
	                    <font class="label">Procedimento : N.</font>
				        <font class="campo"><%= fascicoloTrovato.getChiaveAnno() %>/<%=fascicoloTrovato.getChiaveProgr()%></font>
	                  </td>
	                </tr>
                </table>
                   
				<%
				//==============================================================
				// Dati della Sentenza
                //==============================================================
				SentenzaModel sentenzaTrovata = fascicoloTrovato.getSentenza();
				%>
				<table cellspacing="2" cellpadding="2" width="100%">
				    <tr>
				      <td class="Titolo" colspan="4"> Titolo</td>
				    </tr>
				    <tr>  
				      <td class="L" colspan="4">
				        <font class="label"><%=sentenzaTrovata.getDescrTipoProvvedimento().substring(0,1).toUpperCase()
				          +sentenzaTrovata.getDescrTipoProvvedimento().substring(1).toLowerCase()%>
				        </font>
				        <font class="label"> N.</font>
				        <font class="campo">
				          <%=sentenzaTrovata.getAnnoSentenza()%>/<%=sentenzaTrovata.getNumeroSentenza()%>
				        </font>
				        <font class="label">del</font>&nbsp;
				        <font class="campo">
				          <%=DateUtils.getDateToString(sentenzaTrovata.getDataProvvedimento(), "dd-MM-yyyy")%>
				        </font>
				        &nbsp;<font class="label"> Emessa da: </font>
				        <font class="campo"><%=sentenzaTrovata.getDescrTipoAutoritaEmittente()%></font>&nbsp;
				        <% if (sentenzaTrovata.getNumSezioneAutoritaEmittente() != null){%>
				          <font class="label">(Sez.</font> <font class="campo"><%=sentenzaTrovata.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
				        <% } %>
				        <font class="label"> di </font>
				        <font class="campo"><%=StringUtils.toStringJSP(sentenzaTrovata.getDescrLuogoEmittente())%></font>
				      </td>
				    </tr>
				    <tr>
				      <td class="L" colspan=4>
				        <font class="label">Numero Reg. Gen.: </font>
				        <font class="campo"><%=StringUtils.toStringJSP(sentenzaTrovata.getStringRegGen())%>&nbsp;</font>
				      </td>
				    </tr>
				    <tr>
				      <td class="L" width=100% colspan=4>
				        <font class="label">Data Irrevocabilità:</font>
				        <font class="campo"><%=DateUtils.getDateToString(fascicoloTrovato.getDataIrrevocabilita(), "dd-MM-yyyy") %></font>
				      </td>
				    </tr>				    
				</table>            
                
                <%-- ========================================================== --%>          
                <%
                //==============================================================
                // Dati del Soggetto
                //==============================================================
                SoggettoModel soggettoTrovato = fascicoloTrovato.getSoggetto();
                %>
                <table cellspacing="2" cellpadding="2" width="100%">
                    <tr>
                      <td class="Titolo" colspan="4"> Soggetto</td>
                    </tr>
                    <tr>
                      <td class="L" width="100%" colspan="4">
                        <font class="label">Soggetto : </font>
                        <font class="campo"><%=soggettoTrovato.getCognome()%>&nbsp;<%=soggettoTrovato.getNome()%></font>
                        <%if (soggettoTrovato.getSesso().compareTo("F")==0){%>
                                <font class="label">nata il :</font>&nbsp;
                        <%} else {%>
                                <font class="label">nato il :</font>&nbsp;
                        <%} %>
                      
                        <% 
                        if(soggettoTrovato.getDataNascita() == null)
                        {
                          if(soggettoTrovato.getDataNascitaPresunta().equals("S")) {%>
                              <font class="campo"><%=StringUtils.toStringJSP(soggettoTrovato.getAnnoNascita())%></font>&nbsp;
                        <%}
                          else{%>
                              <font class="campo">***</font>&nbsp;
                        <%}
                        }else{%>
                            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoTrovato.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
                        <%}%>
                      
                        <font class="label">in : </font>
                        <font class="campo"> 
                        <%if (soggettoTrovato.getDescrComuneNascita().compareTo("-")==0){%>
                              <%=soggettoTrovato.getDescComuneNascitaEstero()%>  (<%=soggettoTrovato.getDescrStatoNascita().toUpperCase()%>)
                        <%}else{%>
                              <%=soggettoTrovato.getDescrComuneNascita()%> (<%=soggettoTrovato.getCodProvinciaNascita()%>)
                        <% }%>
                        </font>
                        <font class="label">&nbsp;Codice CUI: </font>
                        <font class="campo"><%=StringUtils.toStringJSP(soggettoTrovato.getCodAfis(),"n.d.")%></font>
                      </td>
                    </tr>
                </table>        
            
                <br>
                <table cellspacing="2" cellpadding="2" width="100%">
                    <tr>
                      <td class="Titolo" colspan="6"> Istruttorie Presenti sul procedimento  <%=fascicoloTrovato.getChiaveAnno() %>/<%=fascicoloTrovato.getChiaveProgr()%></td>
                    </tr>
                    
                     
                <% if (ListaIstruttorieCumulo.size()==0) { %>               
                    <tr>
                      <td class="L"> Nessuna istruttoria attualmente presente sul fascicolo indicato </td>
                    </tr> 
                <% } else { %>
				    <tr>
				      <td class="int">N&deg;</td>
				      <td class="int">Data Apertura</td>
				      <td class="int">Data Chiusura</td>
				      <td class="int">Provvedimento Cumulo</td>
				      <td class="int">Stato</td>
				    </tr>                  
				    <%
				    int conta_rec_visualizzati = 0;
				    Iterator itx = ListaIstruttorieCumulo.iterator();
				    while ( itx.hasNext()) 
				    {
				        IstruttoriaCumuloModel lIstruttoriaCumulo = (IstruttoriaCumuloModel)itx.next();    

				        conta_rec_visualizzati++;
				        
				        int numTitoli = 0;
				        if (lIstruttoriaCumulo.getTitoliCumulati()!=null)
				            numTitoli = lIstruttoriaCumulo.getTitoliCumulati().size();
				    %>
				    <tr>
				        <td class="c" nowrap>
				            <%=lIstruttoriaCumulo.getAnnoProtocollo()%>/<%=lIstruttoriaCumulo.getNumProtocollo()%>
				        </td>
				        <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"),"&nbsp;")%></td>
				        <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataChiusura(),"dd-MM-yyyy"),"&nbsp;")%></td>
				        <td class="c">&nbsp;
				        <% if (lIstruttoriaCumulo.getFlagStato().equals("C")) { %>
				            <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getProvvedimentoCumulo().getDescrMotivo()) %>
				            <% if ("A".equals(lIstruttoriaCumulo.getProvvedimentoCumulo().getFlagDocumentoRegistrato())) { %>
				            <br><font class="cRosso">PROVVEDIMENTO ANNULLATO</font>
				            <% } %>      
				        <% } %>      
				        </td>
				        
				        <% if (lIstruttoriaCumulo.getFlagStato().equals("A")) { %>
				        <td class=c>&nbsp;<font color="red">Aperta</font></td>
				        <% } else if (lIstruttoriaCumulo.getFlagStato().equals("C")) {%>
				        <td class=c>&nbsp;Chiusa</td>
				        <% } else if (lIstruttoriaCumulo.getFlagStato().equals("N")) {%>
				        <td class=c>&nbsp;Annullata</td>
				        <% } %>
				    </tr>
				    
				    <%-- ============================================== --%>
					<%--       ELENCO TITOLI GIA' IN ISTRUTTORIA        --%>
					<%-- ============================================== --%>
                    <% if (lIstruttoriaCumulo.getTitoliCumulati()!=null ) { %>
				    <tr style="display:block">
				      <td class="c" colspan="100%" style="padding-top:15px; padding-bottom:15px;">
						<table cellspacing="2" cellpadding="2" width="95%">				      
						    <tr>
						        <%-- Inserire qui le intestazioni delle colonne che si vogliono visualizzare --%>
						        <td class="int">Titolo</td><!-- Sentenza/Decreto/Cumulo-->
						        <td class="int">N&deg;</td>
						        <td class="int">Data Titolo</td>
						        <td class="int">Autorita' Emittente</td>
						        <td class="int">Anno/Numero <br>Reg.Gen.</td>
						        <td class="int">Definitivo il</td>
						        <td class="int">Anno/Numero <br>SIEP</td>
						        <td class="int">Autorita'</td>
						    </tr>
							<% 
							Vector vListaTitoli = new Vector (lIstruttoriaCumulo.getTitoliCumulati());
							for (int j=0;j<vListaTitoli.size();j++)
							{
							    TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) vListaTitoli.elementAt(j);
							    boolean isTitoloCumulante = false;
							    if (   lTitoloModel.getProcedimentoCumulato() != null
							        && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null
							        && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine().compareTo(lIstruttoriaCumulo.getFasSieIdFascicoloSiep()) == 0) 
							    {
							        isTitoloCumulante = true;
							    }
    
							    // Grigio i dati dei procedimenti momentaneamente esclusi dal cumulo
							    String lFontColor = "";
							    if ("S".equals(lTitoloModel.getFlagEscluso())) {
							        lFontColor = "style='color:grey;'";
							    }
							    
							    // Descrizione tipo titolo
							    String lDescrtipoTitolo = lTitoloModel.getDescrTipoProvvedimento();
							    if ("02".equals(lTitoloModel.getCodTipoProvvedimento())) {
							        String [] lUfficiSorv = new String[] {"UDS", "TDS", "UDSM"};
							        if (!Arrays.asList(lUfficiSorv).contains(lTitoloModel.getCodTipoAutoritaEmittente())) {
							            lDescrtipoTitolo = "Decreto Penale";
							        }
							    }
							    String tipoCaricamento = "";
							    boolean isTitoloManuale = false;
							    if (lTitoloModel.getIdSentenzaOrigine() != null || "03".equals(lTitoloModel.getTipoIscrizione())) {
							        tipoCaricamento = "";
							    } else {
							        tipoCaricamento = " <font class=\"label\"  style=\"font-size:8px;vertical-align: super;\" >(*)</font>";
							        isTitoloManuale = true;
							    }
    
							    // Autorità emittente
							    String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente() + " di " + lTitoloModel.getDescrLuogoEmittente();
							    if (lTitoloModel.getNumSezioneAutoritaEmittente() != null)
							        AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();
							
							    // Anno e num registro
							    String reg = StringUtils.toStringJSP(lTitoloModel.getTipoRegGen(), "");
							    String anno_reg = StringUtils.toStringJSP(lTitoloModel.getAnnoRegGen(), "");
							    String num_reg = StringUtils.toStringJSP(lTitoloModel.getNumeroRegGen(), "");
 
							    // Anno Numero SIEP
							    String nSiep = "";
							    String AutoritaSiep = "";
							    ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
							    if (lProcedimentoCumulatoModel != null) {
							        if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato())) {
							            UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();
							            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() + "/" + lProcedimentoCumulatoModel.getChiaveProgrOrigine();
							            nSiep += "<br> <font class=\"cRosso\">(Ex " + lUfficioOrigine.getCodTipoUfficio() + " di " + lUfficioOrigine.getDescrComune() + ")</font>";
							        } else {
							            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
							        }
							        AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
							    }    
							%>  

							<%  if (isTitoloCumulante) { %> 
							<tr style="background-color: rgb(255,255,153);">
							<% } else { %>
							<tr>
							<% } %>
								<td class="c" nowrap><%=lDescrtipoTitolo%><%=" " + tipoCaricamento%></td>
							    <td class="c" <%=lFontColor%> nowrap><%=lTitoloModel.getAnnoSentenza()%> / <%=lTitoloModel.getNumeroSentenza()%></td>
							    <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
							    <td class="c" <%=lFontColor%>>&nbsp;<%=AutEmi%></td>
							    <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=anno_reg%>/<%=num_reg%>&nbsp;<%=reg%></td>
							    <td class="c" <%=lFontColor%> nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataIrrevocabilita(),"dd-MM-yyyy"))%></td>
								<% if (lProcedimentoCumulatoModel != null) { %>
								<td class="c" <%=lFontColor%> nowrap><%=nSiep%></td>
								<% } else { %>
								<td class="c" <%=lFontColor%> nowrap>&nbsp;</td>
								<% } %>
							    <td class="c" <%=lFontColor%> >&nbsp;<%=AutoritaSiep%></td> 
							</tr>
						<%
						}
						%>
						</table> 
						<% } %>   
				      </td>
				    </tr>
				    <% } // end while su iterator %>
				    <tr style="display:none;">
                      <td class="l" colspan="5">Attenzione.</td>
                    </tr>
                <% } %>
                </table>
                
                <br>
                 <table cellspacing="2" cellpadding="2" width="100%">
                    <tr>
                      <td colspan="2">
                        <input class="bottone" type="submit" name="TRASFERISCI ISTRUTTORIA" value="TRASFERISCI ISTRUTTORIA">
                      </td>
                    </tr>                                                        
                </table>                
                
                <%-- ========================================================== --%>
            </td>
        </tr>    
    </table>
  </form>  
  <script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("TrasferisciIstruttoria");
     frmvalidator.setAddnlValidationFunction("Verify");  
  </script>
</body>
</html>


