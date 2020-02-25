<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaEventoModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>


<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="documentiFascicoliSius" scope="request" class="java.util.ArrayList" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Decisioni Sorveglianza</title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
        <%
        boolean esistonoDati = false;
        if( !documentiFascicoliSius.isEmpty() )
          esistonoDati = true;
        %>
        
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");
          window.parent.close();
        }
      }

      function insertIT( id,
                         annoSius,
                         numeroSius,
                         annoOrdinanza,
                         numeroOrdinanza,
                         tipoAutorita,
                         sedeAutorita,
                         oggetto,
                         natura,
                         giorno,
                         mese,
                         anno,
                         luogoProva,
                         giornoInizioMisura,
                         meseInizioMisura,
                         annoInizioMisura,
                         giornoInizioRevocaMisura,
                         meseInizioRevocaMisura,
                         annoInizioRevocaMisura,
                         anniRevocaReclusione,
                         mesiRevocaReclusione,
                         giorniRevocaReclusione,
                         anniRevocaArresto,
                         mesiRevocaArresto,
                         giorniRevocaArresto,
                         note,
                         tipodecisione,
                         flagScarcerato,
                         giornoFineMisura,
                         meseFineMisura,
                         annoFineMisura,
                         giorniMisura,
                         mesiMisura,
                         anniMisura,
                         flagDecisioneTribunale,
                         sedeTdsCompetente
                         )
      {

        if(annoSius!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FAS_SIUS_MA_AT%>.value=annoSius;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FAS_SIUS_MA_AT%>.value="";

        if(numeroSius!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FAS_SIUS_MA_AT%>.value=numeroSius;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FAS_SIUS_MA_AT%>.value="";

        if (annoOrdinanza!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.value=annoOrdinanza;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.value="";

        if (numeroOrdinanza!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.value=numeroOrdinanza;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.value="";

if (tipoAutorita!='-')
  window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_TIPO_UFF_EMITT_MA_AT%>.value=tipoAutorita;
else
  window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_TIPO_UFF_EMITT_MA_AT%>.value="";

        if (sedeAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT%>.value=sedeAutorita;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT%>.value="";

        if (oggetto!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT%>.value=oggetto;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT%>.value="";

        // DATA EMISSIONE
        if (giorno!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value=giorno;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value="";

        if (mese!='-' )
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value=mese;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value="";

        if (anno!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value=anno;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value="";

/*
        try {
          if (luogoProva!='-')
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>.value=luogoProva;
          else
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>.value="";
        }
        catch(err){  } */
        
        try {
         if (tipodecisione!='-')
           window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE_MA_AT%>.value=tipodecisione;
         else
           window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE_MA_AT%>.value="";
        }
        catch(err){  }


        window.parent.close();
      }
    </script>
  </head>

<body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">
          Decisioni Sorveglianza
          <%
          if(!documentiFascicoliSius.isEmpty())
          {
            MisuraAlternativaEventoModel lPrimaMisura = (MisuraAlternativaEventoModel)documentiFascicoliSius.get(0);
            if(   lPrimaMisura.getMisuraAlternativa() != null  
               && lPrimaMisura.getMisuraAlternativa().getCodTipoMisura() != null
              )
            {  
              if(   lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2000")
                 || lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2001") 
                 || lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2480")
                )
              {
              %>
              - Sospensione Esecuzione Pena
              <%
              }
              else if( lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("2140"))
              {
              %>
              - Decreti di Espulsione
              <%
              } 
              else if( lPrimaMisura.getMisuraAlternativa().getCodTipoMisura().equals("0029"))
              {
              %>
              - <%=lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione()%> 
               Opposizione su Espulsione
              <%
              }
              else if( lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione() != null )
              {
              %>
              - <%=lPrimaMisura.getMisuraAlternativa().getDescrNaturaDecisione()%>
              Misure Alternative
              <%
              }
            }
          }
%>
        </font>
      </td>
    </tr>
  </table>
  
  <br>
  
  <table>
    <tr>
      <td class="int">Fascicolo SIEP</td>
      <td class="int">Anno/Numero Sius</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Oggetto</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( !documentiFascicoliSius.isEmpty() )
    {
      Iterator itx = documentiFascicoliSius.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
        MisuraAlternativaEventoModel misuraEventoModel = (MisuraAlternativaEventoModel)itx.next();
        MisuraAlternativaModel misuraModel = misuraEventoModel.getMisuraAlternativa();
        EventoModel eventoModel = misuraEventoModel.getEvento();
        FascicoloSiepModel fascicolo = misuraEventoModel.getFascicoloSiep();
        
        String lUfficio = StringUtils.toStringJSP(eventoModel.getDescrUfficioEmittente(), "-");
        if( lUfficio.toUpperCase().startsWith("TRIB") )
        {
          lUfficio = "TDS";
        }
        else if(lUfficio.toUpperCase().startsWith("UFF"))
        {
          lUfficio = "UDS";
        }
        
        
%>
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno(), "-")%>
            /
            <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr(), "-")%>
            <br><%=StringUtils.toStringJSP(fascicolo.getDescrTipoUfficio(), "-")%> di <%=StringUtils.toStringJSP(fascicolo.getDescrComuneUfficio(), "-")%>
          </td>
          <td class="c">
            <%=StringUtils.toStringJSP(misuraModel.getChiaveAnnoFascicoloSius(), "-")%>
            /
            <%=StringUtils.toStringJSP(misuraModel.getChiaveProgrFascicoloSius(), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(eventoModel.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(eventoModel.getDescrLuogoEmittente(), "-")%>
            <br>del&nbsp;
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"dd-MM-yyyy"), "-")%>
            
            <% if(misuraModel != null && "P".equalsIgnoreCase(misuraModel.getFlagUfficioInserimento())) { %>
            <br>(<font class="cRosso">-- iscritto da SIEP --</font>)
            <% } %>
          </td>
          
          <td class="l">
            <%
            if(   misuraModel != null  && misuraModel.getCodTipoMisura() != null
               &&(   misuraModel.getCodTipoMisura().equals("2000") 
                  || misuraModel.getCodTipoMisura().equals("2001") 
                  || misuraModel.getCodTipoMisura().equals("2480")
                  || misuraModel.getCodTipoMisura().equals("2006") // Ammissione Provvisoria Affidamento in prova
                  || misuraModel.getCodTipoMisura().equals("2008") // Ammissione Provvisoria Affidamento in prova
                 )
              )
            {
              %>
              <%=StringUtils.toStringJSP(misuraModel.getDescrTipoDecisione(),"-")%>
              <%
            }
            %>
            <%=StringUtils.toStringJSP(eventoModel.getDescrMotivo(),"-")%>
          </td>
          <td class="c">
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(eventoModel.getIdEvento(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveAnnoFascicoloSius(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getChiaveProgrFascicoloSius(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getAnnoRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumeroRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(lUfficio)%>',
                                         '<%=StringUtils.cStrForJS(eventoModel.getDescrLuogoEmittente())%>',
                                         '<%=StringUtils.toStringJSP(eventoModel.getCodMotivo(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodNaturaDecisione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"yyyy"),"-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getDescrLuogoProva())%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioMisura(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataInizioRevoca(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaReclusione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaReclusione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaReclusione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniRevocaArresto(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiRevocaArresto(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniRevocaArresto(),"-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getNote())%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoDecisione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getCodTipoUfficioScarcerazione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraModel.getDataFineMisura(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumGiorniMisura(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumMesiMisura(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getNumAnniMisura(),"-")%>',
                                         '<%=StringUtils.toStringJSP(misuraModel.getFlagDecisioneTribunale(),"-")%>',
                                         '<%=StringUtils.cStrForJS(misuraModel.getDescSedeTdsCompetente())%>'
                                         );">
               
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
          </td>
        </tr>
<%
      }
    }
%>
    </table>
  </form>
</body>
</html>