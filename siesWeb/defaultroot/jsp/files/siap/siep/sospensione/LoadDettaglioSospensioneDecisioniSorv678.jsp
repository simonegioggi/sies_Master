<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>



<jsp:useBean id="eventonotifica"       scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="misuraalternativa"    scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"         scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>

  <jsp:useBean id="nuovapenaresidua"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="UffTDS"              scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"              scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioUDS"   scope="request" class="java.lang.String"/>
<jsp:useBean id="daticssa"            scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="autoritaEsternaC"    scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="NoteAutC"            scope="request" class="java.lang.String"/>
<jsp:useBean id="lIstMod"             scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<%-- MEV10-s3: aggiunti useBean --%>
<jsp:useBean id="descrTipoUfficioTDS" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrTipoUfficioUDS" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  UtenteModel lUtenteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  String tipoUfficioUtenteConnesso = lUtenteMod.getUfficioUtente().getCodTipoUfficio(); 
  
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript">
    function modifica() {
      document.modifica.submit();
    }
  </script>  
</head>

<body class="corpo">

<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Applicazione Sospensione dell'esecuzione della pena (ART.678 C.1 TER C.P.P.)</font>
    </td>
    <%if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
          || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
         )
    {%>     
			  <td class="LBG">
			    <a href="Javascript:modifica()">
			      <img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
			    </a>
			  </td>   
			  <!-- BOTTONE DI STAMPA -->  
       <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
         <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneDecisioniSorv678&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
       </jsp:include>
       <!-- BOTTONE DI VALIDAZIONE DIRETTA -->
		   <td class="LBG">
		     <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActUploadSospensioneDecisioniSorv678&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv678&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		       <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		     </a>
		   </td>
    <%}%>
  </tr>
</table>

<br>

<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<%
if (   eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
    || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
   )
{
%>  
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="modifica">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento() %>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActLoadModificaSospensioneDecisioniSorv678">
</form>
<% } %>  

<table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
        <% if( "S".equals(lFascicoloAssociato.getFlagAltraCausa()))  {%>
              DETENUTO PER ALTRA CAUSA
        <%} else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
       </font>
      </td>
    </tr>

<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if(lAltraCausa.getIstitutoDetenzione() != null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>


  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
<%
          }
        }%>

<% if (penaresidua.getDataInizio() != null) { %>
  <tr>
    <td class="l">Data Decorrenza Pena</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  </tr>
<% } %>


<% if ("S".equals(penaresidua.getFlagErgastolo())) { %>
  <tr>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
  </tr>
<% } else if ("D".equals(penaresidua.getFlagErgastolo())) { %>
  <tr>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
  </tr>
<% } %>


<tr>
<%
if ( !penaresidua.isErgastolo() && penaresidua.getDataFine()!=null)
{
   if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
   %>
   <td class="l">Data Fine Pena</td>
   <td class="L" >
    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
   </td>
   <% } else { %>
   <td class="l">Data Fine Pena</td>
   <td class="lRosso" >
     <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
   </td>
   <% }
}
%>
</tr>



<%
if ( penaresidua.getIdPenaResidua() != null && !penaresidua.isErgastolo() )
{
  if ( !penaresidua.isQuantumReclusioneZero() )
  {
  %>
  <tr>
    <td class="l">Reclusione</td>
    <td class="l" >
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
    </td>
    <% if (penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){ %>
    <td class="l">Multa</td>
    <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    <% } %>
  </tr>
  <% } %>


  <% if (!penaresidua.isQuantumArrestoZero() ){ %>
  <tr>
    <td class="l" >Arresto</td>
    <td class="l" >
       <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
       <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
       <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
    </td>
    <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
    <td class="l">Ammenda</td>
    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
    <% } %> 
  </tr>
  <% } %>
<% } %>
</table>

<br>

<table>
<tr>
  <td class="l">Data Emissione</td>
  <td class="L" >
    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
  </td>

  <%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length>0){%>
  <td class="l">Data Trasmissione</td>
  <td class="L">
    <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
  </td>
  <%}%>
</tr>

<%
//========================================================
//  Dati del provvedimento della sorveglianza
//========================================================
%>
<tr>
  <td class="l">Anno / Numero Sius</td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
  </td>
  <td class="l"> Anno / Numero Provvedimento </td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
    <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
  </td>
</tr>

<tr>
  <td class="l">Tipo Provvedimento</td>
  <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoDecisione())%></font></td>
</tr>

<tr>
  <td class="l">Autorità emittente</td>
    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
      String descrTipoUfficio = StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio());
      if (("PM".equals(tipoUfficioUtenteConnesso) || "PMM".equals(tipoUfficioUtenteConnesso) || "PGCAP".equals(tipoUfficioUtenteConnesso)) &&
          "UDSM".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
        descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
      }
    %>
  <td class="l"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
</tr>
<tr>
   <td class="l">Oggetto Decisione </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
   <td class="l">Data Emissione Provvedimento </td>
   <td class="l">
    <font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
  </tr>
<%if(misuraalternativa != null && misuraalternativa.getNote() != null){%>
<tr>
<td class="l">Motivazioni</td>
    <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%>&nbsp;</font>
    <td>
</tr>
<%}%>


<tr>
  <td class="l">Data sospensione esecuzione</td>
  <td class="l">
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%>
    </font>
  </td>
  <%if("PROC".equals(misuraalternativa.getCodTipoUfficioScarcerazione())){%>
  <td class="l">Da Scarcerare</td>
  <%}else{%>
  <td class="l">Libero per avvenuta Scarcerazione</td>
  <%}%>
</tr>

<% if (!sospensione.isQuantumEspiataZero()) { %>
<tr>
  <td class="l">
    <font class="label">Pena Espiata</font>
  </td>
  <td class="l">
    <font class="label">Anni</font>
    <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
    <font class="label">Mesi</font>
    <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
    <font class="label">Giorni</font>
    <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
  </td>
</tr>
<% } %>

<%
if (  "N".equals(penaresidua.getFlagErgastolo())
    && (   !sospensione.isQuantumReclusioneResiduoZero() 
        || !sospensione.isQuantumArrestoResiduoZero()
       )
   )
{
%>
<tr>
  <td class="l">
    <font class="label">Pena Residua</font>
  </td>
  <td class="l">
    <% if (!sospensione.isQuantumReclusioneResiduoZero() ) {  %>
      <font class="label">Reclusione : </font>
      <font class="label">Anni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
      <font class="label">Mesi</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
      <font class="label">Giorni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
      <% if(sospensione.getMultaResidua()!=null && sospensione.getMultaResidua().compareTo(new BigDecimal(0))!=0) {%>
        <font class="label">Multa </font>
        <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>&nbsp;€&nbsp;
      <% } %>
    <%}%>

    <% if (!sospensione.isQuantumArrestoResiduoZero()) {  %>
      <font class="label"> Arresto : </font>
      <font class="label">Anni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
      <font class="label">Mesi</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
      <font class="label">Giorni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
      <% if(sospensione.getAmmendaResidua()!=null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0))!=0){ %>
        <font class="label">Ammenda </font>
        <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>&nbsp;€&nbsp;
      <% } %>
    <% } %>
  </td>
</tr>
<% } %>
          
    

<% if("S".equals(penaresidua.getFlagErgastolo())) { %>
<tr>
  <td class="l">
    <font class="label">Pena Complessiva</font>
  </td>
  <td class="l">
    <font class="campo">ERGASTOLO</font>
  </td>
</tr>
<% } else if("D".equals(penaresidua.getFlagErgastolo())) { %>
<tr>
  <td class="l">
    <font class="label">Pena Complessiva</font>
  </td>
  <td class="l">
    <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
  </td>
</tr>
<% } %>



<% if(magistrato != null) { %>
<tr>
  <td class="l">Magistrato Firmatario
  <td class="L">
    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
  </td>
</tr>
<% } %>

<% if (  lIstMod.getIdIstitutoDetenzione() != null
     && !lIstMod.getIdIstitutoDetenzione().equals(""))
  {
%>
<tr>
  <td class="l">Istituto Detenzione</td>
  <td class="l">
    <font class="campo"> <%=StringUtils.toStringJSP(lIstMod.getDescrTipoIstituto())%></font>&nbsp;di
    <font class="campo"><%=StringUtils.toStringJSP(lIstMod.getDescrComune())%></font>
  </td>
</tr>
<% } %>

<%
if (daticssa.getComune() != null && !daticssa.getIndirizzo().equals("")) { %>
<tr>
  <td class="l"><%=StringUtils.toStringJSP(daticssa.getTipoDesc())%> Competente</td>
  <td class="l">
    <font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
  </td>
</tr>
<% } %>

<% if (UffTDS != null && !UffTDS.equals("")) { %>
<tr>
  <td class="l">Destinatario</td >
  <td class="L">
    <font class="campo"><%=StringUtils.toStringJSP(descrTipoUfficioTDS)%></font> di <font class="campo"><%=StringUtils.toStringJSP(UffTDS)%></font>
  </td>
</tr>
<% } %>

<% if (UffUDS != null && !UffUDS.equals("")) { %>
<tr>
  <td class="l">Destinatario</td>
      <%
      String descTipoUfficioUDS = StringUtils.toStringJSP(descrTipoUfficioUDS);
      if (("PM".equals(tipoUfficioUtenteConnesso) || "PMM".equals(tipoUfficioUtenteConnesso) || "PGCAP".equals(tipoUfficioUtenteConnesso)) &&
          "UDSM".equals(codTipoUfficioUDS)) {
        descTipoUfficioUDS = "Magistrato di Sorveglianza per i Minorenni";
      }
      %>
  <td class="L">
    <font class="campo"><%=descTipoUfficioUDS%></font> di <font class="campo"><%=StringUtils.toStringJSP(UffUDS)%></font>
  </td>
</tr>
<% } %>

<% if (autoritaEsternaC.getCodTipoAutorita()!= null && autoritaEsternaC.getCodSede()!= null) {%>
  <tr>
    <td class="l">Autorità di Polizia Competente per territorio</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(autoritaEsternaC.getDescrTipoAutorita())%>
      </font>
      
      <%if(autoritaEsternaC !=null && !autoritaEsternaC.getDescrSede().equals("-")){%>
        di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %></font>
      <%}%>
    </td>
  </tr>

  <% if (!NoteAutC.equals("")) {%>
  <tr>
    <td class="l">Indirizzo</td>
    <td class="L">
      <font class="campo"><%=NoteAutC%>&nbsp;</font>
    </td>
  </tr>
  <%}%>
<%}%>
</table>

<br>

<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table>
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActUploadSospensioneDecisioniSorv678">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv678">
        </td>
      </tr>
    </table>
  </form>
</div>

<br>
<br>
</body>
</html>