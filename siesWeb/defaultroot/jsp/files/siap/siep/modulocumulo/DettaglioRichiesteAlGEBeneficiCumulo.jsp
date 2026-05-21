<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento"     scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="UfficioDestinatarioProvv" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="lReato" scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel"/>

<html>
<head>
  <title> Dettaglio Richieste al GE Applicazione/Revoca Benefici </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    { 
      if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaRichBenGE";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richieste al GE Applicazione/Revoca Benefici &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">

  <input type="hidden" name="modalita" value="">


<div id="divPosizionamento" align="left" style="padding-left: 25px;">

  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">

    <tr>
      <td class="l">Provvedimento</td>
      <td class="l" colspan = "3">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
        </font> 
        del 
        <font class="campo">
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
    Vector <ComputiCumuloModel> lListaComputi = Provvedimento.getListaComputi();
    
if (lListaComputi!=null) {

  Iterator itxComputi = lListaComputi.iterator();
  int ind = 0;
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    ind++;
    // Dati Autorità Emittente Provvedimento
    if (ind == 1                  &&
      lComputo.getDataEmissioneProvv() != null )
    {
      // Dati dei ReatoCumulo
      if (lReato != null && 
        lReato.getIdReatoCum() != null )
      { 
%>
        <table cellspacing="2" cellpadding="2" width="95%" align="center">
          <tr><td colspan=7 class="Titolonocap">Titoli di reato</td></tr>
          <tr>
            <td class="c">Reato</td>
            <td class="c">Durata</td>
            <td class="c">Sanzione</td>
          </tr>
<%
          boolean lFlagAnnoNumero = false;
    
          if(  lReato.getAnnoFonte() != null
             && !lReato.getAnnoFonte().equals("")
             &&  lReato.getNumeroFonte() != null
             && !lReato.getNumeroFonte().equals("") )
            {
              lFlagAnnoNumero = true;
            }
%>
          <tr>
            <td class="l">
              <font class="label">
              <% if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) { %>
                n.<%=lReato.getProgrNumeroManuale()%>: 
              <% } else { %>
                n.<%=lReato.getProgrReato()%>:
              <% } %>
              </font>
              <font class="L">
<%
                if(lFlagAnnoNumero)
                {
                   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte()+" ");
                   if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
                      out.println(lReato.getAnnoFonte());
                   if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
                      out.println("/"+lReato.getNumeroFonte());
                }
  
                if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
                    out.println("art."+lReato.getArticolo());
                if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lReato.getDescrSottonumerazione());
  
                if(!lFlagAnnoNumero)
                {
                   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte());
                }
          
                if(lReato.getComma() != null && !lReato.getComma().equals(""))
                    out.println(" c. "+lReato.getComma());
                if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
                    out.println(" l. "+lReato.getLettera());
                if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
                    out.println(" n. "+lReato.getNumero());%>
              </font>
              
              <% if(lReato.getStringaConsumazione()!= null) { %>
                  <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
              <% } %>
            
              <%if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
                  <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
              <% } %>
        
              <% if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
                  <font class="label">Luogo</font>&nbsp;
                  <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
              <% } %>
            </td>

            <td class="l">
              <table>
                <tr>
                  <td class="lnobord"><font class="label">AA</font></td>
                  <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
                  <td class="lnobord"><font class="label">MM</font></td>
                  <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
                  <td class="lnobord"><font class="label">GG</font></td>
                  <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
                </tr>
              </table>
            </td>
            <td class="r">
              <font class="campo">
                <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
              </font>
      €
              <% if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0) { %>
                di
                <font class="campo">
                  <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
                </font>&nbsp;
              <% } %>
            </td>
            </tr>
          </table>
  <%  } %>

<%  } %>


    <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">    
      <tr>
          <td class="titolo" colspan="4">Richiesta al Giudice dell' Esecuzione</td>
      </tr>
      <% if ("002".equals(lComputo.getCodTipoAnnotazione()) || "003".equals(lComputo.getCodTipoAnnotazione()) ) { %>
      <%-- AMNISTIA/INDULTO --%>
<%--      <tr>
        <td class="l" width="20%"> Beneficio </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoAnnotazione())%></font></td>
      </tr>  --%>
      <tr>
        <td class="l" width="20%">Computo Beneficio </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici() , lComputo.getCodTipoAnnotazione() ))%></font>&nbsp;
        </td>
        <td class="l">DPR </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getDPR() , lComputo.getCodDpr() ))%></font>&nbsp;
        </td>
      </tr>
      <% } else if ("013".equals(lComputo.getCodTipoAnnotazione()) )  { %>  
      <%-- INCOSTITUZIONALITA' --%>
      <tr>
        <td class="l" width="20%"> Beneficio </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoAnnotazione())%></font></td>
      </tr> 
      <tr>
        <td class="l" width="20%">Sentenza Corte Costituzionale</td>
        <td class="l">
          Anno/Numero
          <font class="campo">
          <%if(lComputo.getAnnoSentenza() == null || lComputo.getAnnoSentenza().compareTo(new BigDecimal(0))==0)  {%>
          -
          <% } else { %>
          <%=StringUtils.toStringJSP(lComputo.getAnnoSentenza() )%>
          <%} %>
          / <%=StringUtils.toStringJSP(lComputo.getNumeroSentenza(), "-")%>
         </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataSentenza(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
      <% } else if ("004".equals(lComputo.getCodTipoAnnotazione()) || "017".equals(lComputo.getCodTipoAnnotazione())) { %>  
      <%-- DEPENALIZAZIONE --%>
      <tr>
        <td class="l" width="20%"> Beneficio </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lComputo.getDescrTipoAnnotazione())%></font></td>
      </tr>       
      <tr>
        <td class="l" width="15%">Fonte </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lComputo.getDescrFonte(),"-")%>&nbsp;
          </font>
        </td>
        <td class="l" colspan="2" nowrap>
        Anno
        <%if(lComputo.getAnnoFonte() == null || lComputo.getAnnoFonte().compareTo(new BigDecimal(0))==0){%>
            -
        <%}else{%>
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoFonte())%></font>
        <%}%>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumeroFonte(),"-")%></font>
          Art.
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getArticolo(),"-")%></font>
        </td>
      </tr>
      <tr>
        <td class="l" width="15%">Art. Qualificante </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lComputo.getDescrSottonumerazione(),"-")%>&nbsp;</font>
        </td>
        <td class="l" colspan="2" nowrap>
          Comma
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getComma(),"-")%></font>
          Let.
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getLettera(),"-")%></font>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lComputo.getNumero(),"-")%></font>
        </td>
      </tr>      
      <% } %>
        <tr> 
          <td class="l">Ufficio</td>
          <td class="l" width="25%">
             <font class="campo"><%=StringUtils.toStringJSP( UfficioDestinatarioProvv.getDescrTipoUfficio() ) %></font>&nbsp;di &nbsp;
             <font class="campo"><%=StringUtils.toStringJSP( UfficioDestinatarioProvv.getDescrComune() ) %></font>&nbsp;
          </td>
        </tr>
        
<%      // Dati Pena pecuniaria
        if (ind==1) {
%>
        <tr>
          <td class="titolo" colspan="4">Quantum Pena </td>
        </tr>
<%      } %>

		<%
		String lClassComputi = "campo";
		String lFlagPiu = "";
		if ("+".equals(lComputo.getFlagPiuMeno())) {
		  //isRevoche = true;
		  lClassComputi = "cRosso";
		  lFlagPiu = "<font class='cRosso'>+</font>";
		}
		%>

        <tr>
          <td class="l" colspan="1" nowrap>Reclusione :
            <% if (!lComputo.isQuantumReclusioneZero()) {%>&nbsp;<%=lFlagPiu%>
            Anni
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"0")%></font>
            Mesi
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"0")%></font>
            Giorni
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"0")%></font>
            <%}%>
          </td>
          <td class="l" colspan="3">Multa :
            <% if (!lComputo.isMultaZero()) {%><%=lFlagPiu%><%}%>
            <font class="<%=lClassComputi%>">
              &nbsp;<%=StringUtils.toStringJSP(lComputo.getImportoMulta(),"-")%>&nbsp;
            </font>
          </td>      
        </tr>
            
        <tr>
          <td class="l" colspan="1">Arresto :
            <% if (!lComputo.isQuantumArrestoZero()) {%>&nbsp;<%=lFlagPiu%>
            Anni
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumAnniArresto(),"-")%></font>
            Mesi
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumMesiArresto(),"-")%></font>
            Giorni
            <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumGiorniArresto(),"-")%></font>
            <%}%>
          </td>
          <td class="l" colspan="3">Ammenda :
            <% if (!lComputo.isAmmendaZero()) {%><%=lFlagPiu%><%}%>
            <font class="<%=lClassComputi%>">
              <%=StringUtils.toStringJSP(lComputo.getImportoAmmenda(),"-")%>&nbsp;
            </font>
          </td>      
        </tr>

        <tr><td>&nbsp;</td></tr>
<%  if(lComputo.getFlagAppProvvisoria()!=null || 1==1) { %>
      <tr>
        <td class="l">Anticipazione degli effetti :</td>
        
        <% if("A".equals(lComputo.getFlagAppProvvisoria() )) { %>
        <td class="l" colspan="3"><img src="/images/V.gif"> </td>
        <% } else if("R".equals(lComputo.getFlagAppProvvisoria() )) {  %>
        <td class="l" colspan="3"><font class="label" style="color:red" > NO </font></td>
        <% } else { %>
        <td class="l" colspan="3">&nbsp;</td>
        <% }  %>
      </tr>
<%  } %>        

  <%  } // end while%>
<%  } // end if (lListaComputi!=null) %>

    
</table>
</div>
</form>
</body>
</html>