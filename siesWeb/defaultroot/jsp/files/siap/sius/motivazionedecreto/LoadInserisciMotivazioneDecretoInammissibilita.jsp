<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Hashtable"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.motivazionedecreto.model.MotivazioneDecretoModel"%>
<%@ page import="siap.sius.motivazionedecreto.action.ICostantiMotivazioneDecreto"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="f3b.model.DecodeModel"%>



<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="idDepositoDecreto"   scope="request" class="java.lang.String"/>
<!--jsp:useBean id="motivazioneDecreto"  scope="request" class="siap.sius.motivazionedecreto.model.MotivazioneDecretoModel"/-->
<jsp:useBean id="idEvento"            scope="request" class="java.lang.String"/>
<jsp:useBean id="motivi"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="motiviPresenti"     scope="request" class="java.util.Hashtable"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Motivazione Decreto Inammissibilità</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      var desktop;
    </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  </head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <%
        String lAzione = new String();
        if( modalita.equals("I") )
        {
          lAzione = "siap.sius.motivazionedecreto.action.ActInserisciMotivazioneDecretoInammissibilita";
      %>
        <font class="campo">Inserimento Motivazioni Decreto</font>
      <%
        }
        else if( modalita.equals("M") )
        {
          lAzione = "siap.sius.motivazionedecreto.action.ActModificaMotivazioneDecretoInammissibilita";
      %>
        <font class="campo">Modifica delle Motivazioni Decreto Inammissibilità</font>
      <%
        }
      %>
      </td>
    </tr>
  </table>

  <tr>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  </tr>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMotivazioneDecretoInammissibilita">
    <table cellspacing=4 cellpadding=4>

      <tr>
      <td class="l" colspan=2>Indicare Motivi di Inammissibilità:</td>
      </tr>
<%
    if (motivi.size() > 0)
    {
      Iterator lMotivCorr = motivi.iterator();
      while (lMotivCorr.hasNext())
      {
        DecodeModel lDecod = (DecodeModel) lMotivCorr.next();
        // Codice 90 va trattato in maniera speciale
        if (lDecod.getCode().compareTo("90") != 0)
        {
          String lDescrizione = lDecod.getDescription();
          lDescrizione = lDescrizione.replace('<', 'x');
          lDescrizione = lDescrizione.replace('>', 'z');
          lDescrizione = lDescrizione.replace('?', '0');
          String pat = "x0z";
          String lSubPat = "x01z";

        // Si separa la descrizione iniziale in tante quante sono separate dai campi1
        // che non devono essere più di 1
          String[] lDescrizioni = lDescrizione.split(pat);
          if (lDescrizioni != null && lDescrizioni.length > 0)
          {
%>
      <tr>
        <td class="l"><input value="<%=lDecod.getCode()%>"  type="checkbox" name="<%=ICostantiMotivazioneDecreto.CAMPO_CK_01%>" <% if(motiviPresenti.containsKey(lDecod.getCode())){ %> checked  <%}%> > </td>

         <td class="l">
<%
         int i = 0; // indice sottostringhe separate da campo1
         for (; i <lDescrizioni.length; i++)
         {
            String lDescrizCorr = lDescrizioni[i];

            if (i == 1 )
            {
             // Individuato campo1, solo 1
%>
          <input type="text" name="<%=ICostantiMotivazioneDecreto.CAMPO_DESCR_MOTIVAZIONE+lDecod.getCode()%>" size=35   <% if(motiviPresenti.containsKey(lDecod.getCode())){ %> value= "<%=((String[]) motiviPresenti.get(lDecod.getCode()))[0]%>"  <%} else { %> value= "" <% }%> >
<%
            }
            // Si suddivide ogni stringa in sub-stringhe separate dal secondo campo
            String[] lSubDescrizioni = lDescrizCorr.split(lSubPat);
            int j = 0; // indice sottostringhe separate da campo2
            for (; j <lSubDescrizioni.length; j++)
            {
            if (j == 1 )
            {
             // Individuato campo2, solo 1
%>
          <input  type="text" name="<%=ICostantiMotivazioneDecreto.CAMPO_ALTRA_MOTIVAZIONE+lDecod.getCode()%>" <% if(motiviPresenti.containsKey(lDecod.getCode())){ %> value= "<%=((String[]) motiviPresenti.get(lDecod.getCode()))[1]%>" <% } else { %> value= "" <% }%> size=35>
<%
            }
%>
        <%=(lSubDescrizioni[j] != null ? lSubDescrizioni[j] : "-")%>
<%
            } // for j
         } // for i
%>

        </td>
      </tr>
<%
      } // endif
    }   // caso 90
   }    // endwhile
  }   // motivi > 0
%>
      <tr>
        <td class="l"><input value="90" type="checkbox" name="<%= ICostantiMotivazioneDecreto.CAMPO_CK_01%>" <% if(motiviPresenti.containsKey("90")){ %> checked  <%}%>    ></td>
        <td class="l">
          <textarea title="AltraMotivazione90" name=<%=ICostantiMotivazioneDecreto.CAMPO_ALTRA_MOTIVAZIONE+"90"%> cols=90 rows=5 > <%=(motiviPresenti.containsKey("90")) ?  ((String[]) motiviPresenti.get("90"))[1] : ""%> </textarea>
        </td>
      </tr>

      <tr>
        <td colspan=2>
        <input class="bottone" type="submit" value="Conferma">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiMotivazioneDecreto.CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO%>" value="<%=idDepositoDecreto%>" >
        <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=idEvento%>" >
        </td>
      </tr>

  </table>
  </form>

  </body>
</html>