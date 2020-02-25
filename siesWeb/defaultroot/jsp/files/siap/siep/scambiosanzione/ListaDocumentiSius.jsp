<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>

<jsp:useBean id="documentiSius" scope="request" class="java.util.ArrayList" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sanzioni Sostitutive</title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !documentiSius.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");

          window.parent.close();
        }
      }  
      
      function insertIT(id,
						descrTipoDecisione,
						annoSius,
						numeroSius,                                         
						annoRegistro,
						numeroRegistro ,                                        
						descrUfficioEmittente,
						comuneUfficioEmittente,
						g_DataEmissione,
						m_DataEmissione,
						a_DataEmissione,
						descrTipoSanzione,
						descrNaturaSanzione,
						note,
						descrUfficioSorveglianza,
						comuneUfficioSorveglianza,
						natura,
						numGiorniReclusione,
						numMesiReclusione,
						numAnniReclusione,
						numGiorniArresto,
						numMesiArresto,
						numAnniArresto,
						codTipoDecisione,
						codTipoSanzione,
						codUfficioEmittente
						
      					)
      			{
      				
      			
      				 
			      	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>.value=id;
			      	

			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO%>.value=annoRegistro;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO%>.value=numeroRegistro;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>.value=comuneUfficioEmittente;
			          
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>.value=g_DataEmissione;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>.value=m_DataEmissione;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>.value=a_DataEmissione;
			        
       
				   if(natura == 'CS')
				   {
				        if (numGiorniReclusione!='-')
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_GIORNI_RECLUSIONE%>.value=numGiorniReclusione;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_GIORNI_RECLUSIONE%>.value="";
				
				        if (numMesiReclusione!='-' )
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_MESI_RECLUSIONE%>.value=numMesiReclusione;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_MESI_RECLUSIONE%>.value="";
				
				        if (numAnniReclusione!='-')
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_ANNI_RECLUSIONE%>.value=numAnniReclusione;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_ANNI_RECLUSIONE%>.value="";
				    
				      
				        if (numGiorniArresto!='-')
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_GIORNI_ARRESTO%>.value=numGiorniArresto;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_GIORNI_ARRESTO%>.value="";
				
				        if (numMesiArresto!='-' )
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_MESI_ARRESTO%>.value=numMesiArresto;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_MESI_ARRESTO%>.value="";
				
				        if (numAnniArresto!='-')
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_ANNI_ARRESTO%>.value=numAnniArresto;
				        else
				          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_NUM_ANNI_ARRESTO%>.value="";

			      	    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO%>.value=codTipoDecisione;				   
			      	    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.value=codTipoSanzione;				   
			            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>.value=codUfficioEmittente;
	
				   }
				   else
				   {
				 
			      	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO%>.value=descrTipoDecisione;
		      	    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.value=codTipoSanzione;				   
			       	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>.value=descrUfficioEmittente; 
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_COMPETENTE%>.value=descrUfficioSorveglianza;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO_COMPETENTE%>.value=comuneUfficioSorveglianza;   	 
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.value=annoSius;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.value=numeroSius; 
			       	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_NOTE%>.value=note;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_OGGETTO_PROVVEDIMENTO%>.value=descrTipoSanzione;
			        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiSanzioneSostitutiva.CAMPO_ESITO%>.value=descrNaturaSanzione;
	
				   }
			
			      	window.close();
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
        <font class="campo">Sanzioni Sostitutive</font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int">Anno/Numero Sius</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Oggetto</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( !documentiSius.isEmpty() )
    {
      Iterator itx = documentiSius.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
      	ScambioSanzioneModel scSanzione = (ScambioSanzioneModel)itx.next();
        String lUfficio = StringUtils.toStringJSP(scSanzione.getDescrUfficioEmittente(), "-");
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
            <%=StringUtils.toStringJSP(scSanzione.getChiaveAnnoFascicoloSius(), "-")%>
            /
            <%=StringUtils.toStringJSP(scSanzione.getChiaveProgrFascicoloSius(), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(scSanzione.getDescrUfficioEmittente(),"-")%> di 
            <%=StringUtils.toStringJSP(scSanzione.getComuneUfficioEmittente(), "-")%>
            <br>del&nbsp;
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(scSanzione.getDataEmissione(),"dd-MM-yyyy"), "-")%>
          </td>
          <td class="l">
			<%=StringUtils.toStringJSP(scSanzione.getDescrTipoSanzione(), "-")%>
          </td>
          <td class="c">
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(scSanzione.getIdScambioSanzione(),"-")%>',
										 '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getDescrTipoDecisione() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getChiaveAnnoFascicoloSius(),"-")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getChiaveProgrFascicoloSius(),"-")%>',                                         
                                         '<%=StringUtils.toStringJSP(scSanzione.getAnnoRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumeroRegistro(),"-")%>',                                         
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getDescrUfficioEmittente() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getComuneUfficioEmittente() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(scSanzione.getDataEmissione(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(scSanzione.getDataEmissione(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(scSanzione.getDataEmissione(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getDescrTipoSanzione() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getDescrNaturaSanzione() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getNote() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getDescrUfficioSorveglianza() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getComuneUfficioSorveglianza() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getCodNaturaSanzione(),"-")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumGiorniReclusione(),"")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumMesiReclusione(),"")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumAnniReclusione(),"")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumGiorniArresto(),"")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumMesiArresto(),"")%>',
                                         '<%=StringUtils.toStringJSP(scSanzione.getNumAnniArresto(),"")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getCodTipoDecisione() ),"-")%>',
                                         '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(scSanzione.getCodTipoSanzione() ),"-")%>',
                                          '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lUfficio),"-")%>');">
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