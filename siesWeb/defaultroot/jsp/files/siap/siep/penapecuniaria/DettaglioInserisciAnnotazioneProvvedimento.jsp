<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="RichiCon"      scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="ScaSan"      scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>
<jsp:useBean id="FascicoloSius"   scope="request" class="siap.sius.fascicolo.model.FascicoloSiusModel"/>
<jsp:useBean id="magistrato"      scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  MagistratoModel lMagistrato = magistrato;  
  

    
    if(lPosizione == null)
      lPosizione = new PosizioneGiuridicaModel();

    if(lLuogoDetenzione == null)
        lLuogoDetenzione = new LuogoDetenzioneModel();

    if(lAltraCausa == null)
        lAltraCausa = new AltraCausaModel();
  
//     if (magistrato == null)
//     	lMagistrato = new MagistratoModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Annotazione Provvedimenti Decisioni Sorveglianza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>   

  <script language="JavaScript">

    function ListaDocumentiSius(a_formname)
    {
      var desktop;  
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scambiosanzione.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }
    
  </script> 
</head>

<body class="corpo">

  <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
          <font class="campo">Dettaglio Annotazione Provvedimento Decisioni Sorveglianza</font>
      </td>
      <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
        if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
        {%>
          <!-- BOTTONE DI STAMPA -->
          <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.penapecuniaria.action.ActStampaTrasmAnnotazioneProvvedimentoSor&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
          </jsp:include>
		  <!-- BOTTONE DI UPLOAD -->
		  <td class="LBG">
		    <a  href="#1" onclick="javascript:lookUpload();">
		      <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
		    </a>
			</td>
       <% }%>           

        <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)      
         {%>    
          <!-- BOTTONE DI STAMPA -->
          <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.penapecuniaria.action.ActStampaTrasmAnnotazioneProvvedimentoSor&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
          </jsp:include>

		  <!-- BOTTONE DI UPLOAD -->
		  <td class="LBG">
		    <a  href="#1" onclick="javascript:lookUpload();">
		      <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
		    </a>
			</td>
        <% }%>    

              <!-- TOOLBAR HEADER -->

    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>    

<% //======================= BLOCCO POSIZIONE GIURIDICA ========================= %>
  <table >
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=1>
        <font class="campo">
<%          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
            {%>
                  DETENUTO PER ALTRA CAUSA
<%          }
        else
          {%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
      <%  }%>
        </font>
      </td>
    </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
      if( lAltraCausa.getIstitutoDetenzione()!= null)
      {
%>
             <tr>
               <td class="l">Detenuto presso </td>
               <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
          else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
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
          }
        }%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
   <%-- input type="HIDDEN" title="Codice Posizione"
      value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>"
      type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"
      maxlength="6" size="6" --%>

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }
%>
</table>

<table width="80%" cellspacing=6 cellpadding=6>
  
  <tr>
    <td class="l"> Pena pecuniaria da convertire</td>
  </tr>
  
  <tr>  
    <td class="l" >Multa: Importo</td>
    <td class="l" colspan="2">
      <font class="campo"><%=StringUtils.toEuroFormat(RichiCon.getImportoMulta())%>
      </font>&nbsp;
    </td>
  <%  if (RichiCon.getFlagImprescrittibileMulta().equals("S"))  
    {
  %>    
      <td class="l">Imprescrittibile</td>
  <%  
    }
    else
    {
  %>  
      <td class="l">Data Prescrizione</td>
      <td class="l" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiCon.getDataPrescrizioneMulta(),"dd-MM-yyyy"))%> 
        </font>&nbsp;
      </td>   
  <%  } %>
  </tr>
  
  <tr>  
    <td class="l" >Ammenda: Importo</td>
    <td class="l" colspan="2">
      <font class="campo"><%=StringUtils.toEuroFormat(RichiCon.getImportoAmmenda())%>
      </font>&nbsp;
    </td>
  <%  
      if (RichiCon.getFlagImprescrittibileAmmenda().equals("S"))  
      {
  %>    
        <td class="l">Imprescrittibile</td>
  <%  
      }
      else
      {
  %>  
        <td class="l">Data Prescrizione</td>
        <td class="l" colspan="2">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiCon.getDataPrescrizioneAmmenda(),"dd-MM-yyyy"))%> 
          </font>&nbsp;
        </td>   
  <%    }
      %>

  </tr> 
  

</table>
 
<table width="80%" cellspacing=6 cellpadding=6>
    <tr>
      <td class="l" colspan=3>Data Emissione</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd/MM/yyyy"))%>
        </font>
      </td>
    </tr>

    <tr>
      <td class="l" colspan=3>Tipo Provvedimento</td>
        <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(StringUtils.cStrForJS(ScaSan.getDescrTipoDecisione()))%>
        </font>
      </td>
    </tr> 
    <tr>
      <td class="l" colspan=3>Anno /Numero SIUS</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(ScaSan.getChiaveAnnoFascicoloSius())%> /
          <%=StringUtils.toStringJSP(ScaSan.getChiaveProgrFascicoloSius())%>
        </font>
      </td>
    </tr>
    <% // if(!evento1.getAnnoProtocollo().toString().equals(null))
      // { %>
        <tr>
          <td class="l" colspan=3>Anno /Numero Provvedimento</td>
          <td class="L" colspan=3>
          <font class="campo">
            <%=StringUtils.toStringJSP(ScaSan.getAnnoRegistro())%> /
            <%=StringUtils.toStringJSP(ScaSan.getNumeroRegistro())%>
          </font>
          </td>
        </tr>
    <% // } %>  
    <tr>
      <td class="l" colspan=3>Ufficio Emittente</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(StringUtils.cStrForJS(ScaSan.getDescrUfficioEmittente()))%>
          di
          <%=StringUtils.toStringJSP(StringUtils.cStrForJS(ScaSan.getComuneUfficioEmittente()))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" colspan=3>Oggetto provvedimento</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" colspan=3>Esito Provvedimento</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrEsito())%>
        </font>
      </td>
    </tr>
  
    
  <%  if("0159".equals(eventonotifica.getEvento().getCodEsito()))
    { %>
        
        <tr>  
          <td class="l" colspan=1>Numero Rate</td>
          <td class="L" colspan=1>
            <font class="campo" size=3>
            <%=StringUtils.toStringJSP(RichiCon.getNumeroRate())%>
            </font>
          </td>
          <td class="l" colspan=1>Valore Rata: Euro </td>
          <td class="L" colspan=1>
            <font class="campo" size=14>
            <%=StringUtils.toStringJSP(RichiCon.getValoreRata())%>
            </font>
          </td>                       
          <td class="l" colspan=1>&nbsp;&nbsp;&nbsp;&nbsp;Valore Ultima Rata: Euro</td>
          <td class="L" colspan=1>
            <font class="campo" size=14>
            <%=StringUtils.toStringJSP(RichiCon.getValoreUltimaRata())%>
            </font>
          </td>         
        </tr>
        <tr>
          <td class="l" colspan=2>Pagamento Prima Rata Entro il&nbsp;</td>
          <td class="L" colspan=1>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiCon.getDataInizioPagamento(), "dd-MM-yyyy"))%></font>
          <td class="l" colspan=3>  
            <font class="label">Oppure entro&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getNumeroGiorniInizioPagamento())%></font>
            <font class="label">&nbsp;giorni dalla data di Notifica</font>          
          </td>                         
        </tr>         
    <%} else if("0156".equals(eventonotifica.getEvento().getCodEsito()))
    { %>    
        <tr>
          <td class="l" colspan=3>Durata Libertà Controllata</td>
          <td class="L" colspan=3>
            <font class="label">Anni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoAnni())%>&nbsp;</font>
            <font class="label">Mesi:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoMesi())%>&nbsp;</font>
            <font class="label">Giorni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoGiorni())%>&nbsp;</font>
          </td>                         
        </tr> 
    <%} else if("0157".equals(eventonotifica.getEvento().getCodEsito()))
    { %>    
        <tr>
          <td class="l" colspan=3>Durata Lavoro Sostitutivo</td>
          <td class="L" colspan=3>
            <font class="label">Anni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoAnni())%>&nbsp;</font>
            <font class="label">Mesi:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoMesi())%>&nbsp;</font>
            <font class="label">Giorni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoGiorni())%>&nbsp;</font>
          </td>                         
        </tr> 
    <%} else if("0158".equals(eventonotifica.getEvento().getCodEsito()))
    { %>    
        <tr>
          <td class="l" colspan=3>Durata Differimento</td>
          <td class="L" colspan=3>
            <font class="label">Anni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoAnni())%>&nbsp;</font>
            <font class="label">Mesi:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoMesi())%>&nbsp;</font>
            <font class="label">Giorni:&nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(RichiCon.getDurataEsitoGiorni())%>&nbsp;</font>
          </td>                         
        </tr> 
    <%} %>    
    
    <tr>
      <td class="l" colspan=3>Data Emissione Provvedimento Sorveglianza</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(ScaSan.getDataEmissione(),"dd/MM/yyyy"))%>
        </font>
      </td>
    </tr>   
    <tr>
      <td class="l" colspan=3>Note</td>
      <td class="L" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(StringUtils.cStrForJS(ScaSan.getNote()))%>
        </font>
      </td>
    </tr>
    
    <% 
  if(eventonotifica != null && eventonotifica.getNotifiche() != null)
  {
    Iterator iter = (Arrays.asList(eventonotifica.getNotifiche())).iterator();
    while (iter.hasNext())
    {
       NotificaModel lNotMod = (NotificaModel)iter.next();

    if(lNotMod != null && lNotMod.getAutoritaEsterna() != null && lNotMod.getAutoritaEsterna().getIdAutoritaEsterna() != null)
       { %>
        <tr>
          <td class="l" colspan=3>Ufficio Competente</td>
          <td class="L" colspan=3>
            <font class="campo">
            <%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%>
            </font>
          </td>
        </tr>
        <tr>
          <td class="l" colspan=3>di</td>
          <td class="L" colspan=3>
            <font class="campo">
            <%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%>
            </font>
          </td>
        </tr>
    <% } 
    if(lNotMod != null && lNotMod.getNote() != null)
       { %>
        <tr>
          <td class="l" colspan=3>Altro Destinatario</td>
          <td class="L" colspan=3>
            <font class="campo">
            <%=lNotMod.getNote()%>
            </font>
          </td> 
        </tr>
    <% }    
    
    }
    }%>

<%	if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
        if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0)
    {%>
	    <tr>
			<td class="lNoBord">
			<FORM  method="POST" name="Definizione" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.archiviazione.action.ActLoadInserisciProvvConvPenPec">
			      <br><INPUT class="bottone" type="submit" name="OE" value="Definizione Procedimento">
			 </FORM>
	    </tr>
<% }%>           
  </table>
  <br>
        <div align=left style="visibility:hidden" id="upld">
          <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
              <table>
                    <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
                    <tr>
                        <td class="L">
                    <input  class=bottone  type="submit" value="Conferma">
                    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penapecuniaria.action.ActUploadTrasmAnnotazioneProvvedimentoSor">
                    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento() %>">
                    <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=eventonotifica.getEvento().getCodUfficio()%>">
                        </td>
                    </tr>
                </table>    
          </FORM>
        </div>
  <br>
</body>
</html>