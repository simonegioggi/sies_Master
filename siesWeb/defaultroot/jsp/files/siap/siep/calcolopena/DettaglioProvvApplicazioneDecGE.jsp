<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<jsp:useBean id="ListaAnnotazioni"     scope="request" class="java.util.Vector" />
<jsp:useBean id="ProvvedimentoMod"     scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="AnnotazioneOrdinanza" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel" />

<%
//==============================================================================
// Finestra di visualizzazione del dettaglio dei Provvedimenti di Applicazione
// delle Deisioni del GE
//  - Amnistia/Indulto    (04-0284 - Applicazione Amnistia / Indulto )
//  - Depenalizzazione    (04-0285 - Applicazione depenalizzazione)
//  - Incostituzionalità  (04-2086 - Applicazione incostituzionalita)
//
// Utilizzata SOLO nel caso di provvedimentoi Validato
// 
// Vengono visualizzati i dettagli dell'ordinanza del GE e delle annotazioni
//==============================================================================

String lPageGE = "";

if(ProvvedimentoMod.getCodMotivo().equals("0284")) {
  lPageGE = "AMNI";
} 
else if(ProvvedimentoMod.getCodMotivo().equals("0285")) {
  lPageGE = "DEPEN";
} 
else if (ProvvedimentoMod.getCodMotivo().equals("0286")) {
  lPageGE = "INCOST";
}

%>

<head>
  <title> [S.I.E.S.] - Dettaglio Provvedimento Applicazione Benefici - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if(lPageGE.equals("AMNI")) { %>
        <font class="campo">Dettaglio Amnistia / Indulto</font>
        <% } else if(lPageGE.equals("DEPEN")) { %>
        <font class="campo">Dettaglio Depenalizzazione</font>
        <% } else if (lPageGE.equals("INCOST")) {%>
        <font class="campo">Dettaglio Incostituzionalità</font>
        <% } %>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <%
  //============================================================================
  // S E Z I O N E    C O N    I   D A T I    D E L   P R O V V E D I M E N T O
  //============================================================================
  if(ProvvedimentoMod!=null && ProvvedimentoMod.getIdEvento() != null)
  {
  %>
  <table width="70%">
    <tr><td colspan=3 class="Titolonocap">Provvedimento di Applicazione</td></tr>
    <%  if(ProvvedimentoMod.getDataEmissione()!= null) { %>
    <tr>
      <td class="l">Data Emissione provvedimento</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvedimentoMod.getDataEmissione(), "dd-MM-yyyy") )%></font>
      </td>
    </tr>
    <% } %>
    
    <% if(ProvvedimentoMod != null && ProvvedimentoMod.getCodTipoProvvedimento() != null) { %>
    <tr>
      <td class="l">Tipo Provvedimento</td>
      <td class="l">
        <font class="campo"> <%=StringUtils.toStringJSP(ProvvedimentoMod.getDescrTipoProvvedimento())%></font>
      </td>
    </tr>
    <% } %>

    <% if(ProvvedimentoMod != null && ProvvedimentoMod.getDescrUfficioEmittente() != null && !ProvvedimentoMod.getCodUfficioEmittente().equals("-")) { %>
    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(ProvvedimentoMod.getDescrUfficioEmittente())%></font>&nbsp; di
         <font class="campo"> <%=StringUtils.toStringJSP(ProvvedimentoMod.getDescrLuogoEmittente())%></font>
      </td>
    </tr>
    <% } %>

    <% if(ProvvedimentoMod != null && ProvvedimentoMod.getCodMotivo() != null) { %>
    <tr>
      <td class="l">Oggetto Decisione</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(ProvvedimentoMod.getDescrMotivo())%></font>
      </td>
    </tr>
    <% } %>
  </table>  
  <%
  }
  %>
    
  <%
  //============================================================================
  //   S E Z I O N E    C O N     I    D A T I    D E L L' O R D I N A N Z A
  //============================================================================
  if(AnnotazioneOrdinanza.getAnnotazioneManuale() != null && AnnotazioneOrdinanza.getEvento() != null)
  {
    AnnotazioneManualeModel OrdinanzaGEAnn = AnnotazioneOrdinanza.getAnnotazioneManuale();
    EventoModel OrdinanzaGEEve = AnnotazioneOrdinanza.getEvento();
  %>
  <br>
  <table width="70%">
    <tr><td colspan=3 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
    <tr>
      <td class="l">Declaratoria :</td>
      <td class="l">
        Anno/Numero Ordinanza
        <font class="campo">&nbsp;
          <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
        </font>
      </td>
      <td class="l">
        <font class="label">in data </font>
        &nbsp;&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
<%	if(OrdinanzaGEAnn.getChiaveAnnoSige()!= null && OrdinanzaGEAnn.getChiaveNumeroSige()!=null)
	{%>
    	<tr>
      	<td>&nbsp;&nbsp;&nbsp;</td>
      	<td class="l">
        	Anno/Numero Procedimento SIGE
        	<font class="campo">&nbsp;
          	<%=StringUtils.toStringJSP( OrdinanzaGEAnn.getChiaveAnnoSige() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getChiaveNumeroSige())%>
        	</font>
      	</td>
      	</tr>	
<%	}
	%>	    
    <tr>
      <td class="l">Ufficio :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getMotivazioni())%>&nbsp;
        </font>
      </td>
    </tr>
  </table>
  <% } %>

  <%
  //============================================================================
  //                       LISTA DEI PERIODI CONCESSI
  //============================================================================
  if(!ListaAnnotazioni.isEmpty()) {%>
  <br>
  <table width="70%">
    <tr>
      <td colspan=9 class="Titolonocap">Contenuto Decisione</td>
    </tr>
    <%
    for (Iterator lIter = ListaAnnotazioni.iterator(); lIter.hasNext(); ) {
      AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();

      if(lPageGE.equals("AMNI")) 
      { %>
	      <tr>
	        <td class="l">Computo beneficio :</td>
	        <td class="l" >
	          <font class="campo">
	            <%=StringUtils.toStringJSP( lAnnMan.getDescrTipoAnnotazione(),"-")%>
	          </font>
	        </td>
	        <td class="l">DPR :</td>
	        <td class="l" >
	          <font class="campo">
	            <%=StringUtils.toStringJSP( lAnnMan.getDescrDpr(),"-")%>
	          </font>
	        </td>
	      </tr>
 <% 		if(lAnnMan.getFlagConforme().equals("R") ||
   				lAnnMan.getFlagConforme().equals("I") ||
   				lAnnMan.getFlagConforme().equals("U") ||
   				lAnnMan.getFlagConforme().equals("C") ||
   				lAnnMan.getFlagConforme().equals("D") ||
   				lAnnMan.getFlagConforme().equals("-") )
      		{	%>
      			<tr>
      				<td class="l">Esito : </td>
      				
      		<%		if(lAnnMan.getFlagConforme().equals("R"))
      				{    	%>
      					<td class="l" >	
      						<font color="red" > RIGETTO </font>
      					</td>
      					
      		<%		}
      				else if(lAnnMan.getFlagConforme().equals("I"))
      				{	%>
      					<td class="l" >	
      						<font color="red" > DICHIARA INAMMISSIBILE  </font>
      					</td>
     		<%		}
     				else if(lAnnMan.getFlagConforme().equals("U"))
      				{	%>
      					<td class="l" >	
      						<font color="red" > RIUNISCE  </font>
      					</td>	
      		<%		} 
     				else if(lAnnMan.getFlagConforme().equals("C"))
      				{	%>
      					<td class="l" >	
      						<font color="red" > ACCOLTA IN CONFOMITA'  </font>
      					</td>	
      		<%		} 
     				else if(lAnnMan.getFlagConforme().equals("D"))
      				{	%>
      					<td class="l" >	
      						<font color="red" > ACCOLTA IN DIFFORMITA'  </font>
      					</td>	
      		<%		}
     				else if(lAnnMan.getFlagConforme().equals("-"))
      				{	%>
      					<td class="l" >	
      						<font color="red" > ACCOLTA  </font>
      					</td>	
      		<%		} 
      				else
      				{  
      				}%>
      					
      			</tr>	
      			
      		<%	if(lAnnMan != null && lAnnMan.getMotivazioni() != null)
       			{	%>
      				<tr>
      					<td class="l">Motivazioni :</td>
    					<td class="l" colspan=3>
          					<textarea cols="58" rows="4" name="<%=ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP(lAnnMan.getMotivazioni())%></textarea>
        				</td>
        			</tr>	
        	<%	}  %>

				<tr>
      				<td colspan=9 class="Titolonocap"></td>
    			</tr>
 <%			}     
       }

      if(lPageGE.equals("INCOST")) { %>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
          <%if(lAnnMan.getAnnoCc() == null || lAnnMan.getAnnoCc().compareTo(new BigDecimal(0))==0)  {%>
          -
          <% } else { %>
          <%=StringUtils.toStringJSP(lAnnMan.getAnnoCc() )%>
          <%} %>
          / <%=StringUtils.toStringJSP(lAnnMan.getNumeroCc(), "-")%>
         </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
    <%
    }

    if(lPageGE.equals("DEPEN")) { %>
      <tr>
        <td class="l" width="20%">Fonte </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrFonte(),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">
        Anno
        <%if(lAnnMan.getAnnoFonte() == null || lAnnMan.getAnnoFonte().compareTo(new BigDecimal(0))==0){%>
            -
        <%}else{%>
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getAnnoFonte())%></font>
        <%}%>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumeroFonte(),"-")%></font>
          Art.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getArticolo(),"-")%></font>
        </td>
      </tr>
      <tr>
        <td class="l" width="20%">Art. Qualificante </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrSottonumerazione(),"-")%>&nbsp;</font>
        </td>
        <td class="l">
          Comma
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getComma(),"-")%></font>
          Let.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getLettera(),"-")%></font>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumero(),"-")%></font>
        </td>
      </tr>
      <%}%>
      
      
      <tr>
<%if(lPageGE.equals("DEPEN")){%>
        <td class="l" colspan="2">Reclusione :
<%}else{%>
        <td class="l">Reclusione :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"-")%></font>
        </td>
        <td class="l">Multa :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoMulta(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
      
      <tr>
<%if(lPageGE.equals("DEPEN")){%>

        <td class="l" colspan="2">Arresto :
<%}else{%>
        <td class="l">Arresto :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniArresto(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiArresto(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniArresto(),"-")%></font>
        </td>
        <td class="l">Ammenda :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoAmmenda(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
      
      <tr>
<%if(lPageGE.equals("DEPEN")){%>
        <td class="l" colspan="2">Note :</td>
<%}else{%>
        <td class="l">Note :</td>
<%}%>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getNoteReclusione(),"")%>&nbsp;
          </font>
        </td>
      <tr>

<%
    }
%>
    </table>
<%
  }
%>
  <br>
</body>
</html>