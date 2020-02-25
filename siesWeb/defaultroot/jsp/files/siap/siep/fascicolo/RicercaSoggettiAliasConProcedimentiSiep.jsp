<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.net.URLEncoder" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoAliasFascicoloModel" %>

<jsp:useBean id="fascicoliSogAlias" scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRicerche" scope="request" class="java.lang.String" />
<jsp:useBean id="StrClassiFascicolo" scope="request" class="java.lang.String" />
<jsp:useBean id="fromDetail" scope="request" class="java.lang.String" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti/Alias con Procedimenti </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elencosogalias" action="<%=IWebConstants.PG_MAIN%>">
  
  <!--@emma 24072018 intervento post COLLAUDO 11.2, era type=text -->
  <input type="hidden" name="dett" value="<%=fromDetail%>">
  
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti</font></td>
   </tr>
  </table>

  <br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

<br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td align="right"><img src="/images/QuadratinoBlu.gif"></td>
      <td class="campo">Principale</td>
      <td align="right"><img src="/images/QuadratinoRosso.gif"></td>
      <td class="campo">Alias</td>
    </tr>
    <tr>
      <td class="int">Cognome e Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int">Alias di</td>
      <td class="int">Cod. CUI</td>
      <td class="int">Procedimenti</td>
      <td class="int">Dettaglio</td>
    </tr>
<%
    Iterator itx = fascicoliSogAlias.iterator();
    while ( itx.hasNext())
    {
      SoggettoAliasFascicoloModel lFascicoloSogAlias = (SoggettoAliasFascicoloModel)itx.next();
%>
    <tr>
      <td class="c">
      <!-- il colore nel nominativo diventa rosso se è un'Alias -->
      <%if (lFascicoloSogAlias.getSogIdSoggettoAlias()== null || lFascicoloSogAlias.getSogIdSoggettoAlias().equals(""))
        {%>
            <font class="label"><%=lFascicoloSogAlias.getCognomeNomeSoggettoAlias()%></font>
      <%}
        else
        {%>
           <font class="cRosso"><%=lFascicoloSogAlias.getCognomeNomeSoggettoAlias()%></font>
      <%}%>

      </td>
      <td class="c"><font class="label">
<%      if ((lFascicoloSogAlias.getSoggetto().getDataNascita())==null || lFascicoloSogAlias.getSoggetto().getDataNascita().equals(""))
        {%>&nbsp;<%}
        else
        {%>
          <%=DateUtils.getDateToString(lFascicoloSogAlias.getSoggetto().getDataNascita(),"dd-MM-yyyy")%>
        <%}%>
        </font>
      </td>
      <td class="l">
          <font class="label">
            <%if (lFascicoloSogAlias.getLuogoProvinciaNascita()==null || lFascicoloSogAlias.getLuogoProvinciaNascita().equals(""))
              {%>&nbsp;<%}
              else
              {%>
                 <%=lFascicoloSogAlias.getLuogoProvinciaNascita()%>
            <%}%>
          </font>
      </td>
      <td class="l">
              <font class="label"><%=lFascicoloSogAlias.getSoggetto().getPaternita()%>&nbsp;</font>
      </td>
      <td class="c"><font class="label">
      <%if (lFascicoloSogAlias.getCognomeNomeLegatoAlias()==null || lFascicoloSogAlias.getCognomeNomeLegatoAlias().equals("") )
        {%>&nbsp;<%}
        else
        {%>
            <%=lFascicoloSogAlias.getCognomeNomeLegatoAlias()%>
        <%}%>
        </font>
      </td>
      <td class="c">
           <font class="label"><%=StringUtils.toStringJSP(lFascicoloSogAlias.getSoggetto().getCodAfis())%>&nbsp;</font>
      </td>
      <td class="c">
          <font class="label"><%=lFascicoloSogAlias.getNumeroFascicoliUfficioSedeCompetente()%></font>
      </td>
      <td class="c">

             <%
             // passa come valore il nome del Soggetto Alias se è presente l'ID Alias
             String lStringaNomeAlias = "";
             if (lFascicoloSogAlias.getSogIdSoggettoAlias()!= null)
             {
                 // esegue l'encode della stringa per passarla alla query string
                 lStringaNomeAlias = URLEncoder.encode(lFascicoloSogAlias.getCognomeNomeSoggettoAlias(), "UTF-8");
             }
             %>
				<jsp:include page="<%=IWebConstants.PG_BUTTONS_MORE_PARAMETERS%>">
					<jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
        			<jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
        			<jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"/>
        			<jsp:param name="ValoreIdEntita" value="<%=lFascicoloSogAlias.getSoggetto().getIdSoggetto()%>"/>
        			<jsp:param name="CampoIdEntitaProvv" value="NomeSoggettoAlias"/>
					<jsp:param name="ValoreIdEntitaProvv" value="<%=lStringaNomeAlias%>"/>
					<jsp:param name="ChiaveQuattro" value="tipoRicerche"/>
					<jsp:param name="ValoreQuattro" value="<%=tipoRicerche%>"/>
					<jsp:param name="CampoIdEntitaPP" value="StrClassiFascicolo"/>
             		<jsp:param name="ValoreIdEntitaPP" value="<%=StrClassiFascicolo%>"/>
             		<jsp:param name="CampoDet" value="campoDet"/>             	
 				    <jsp:param name="ValoreDet" value="<%=fromDetail%>"/>
				</jsp:include>

       </td>
      </tr>
<%
    }
%>
    </table>

  </FORM>
  <br>
  </body>
</html>