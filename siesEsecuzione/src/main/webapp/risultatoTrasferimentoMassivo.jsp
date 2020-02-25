<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fieldset>
	<legend>Risultato trasferimento</legend>
	<c:if test="${not empty entryList}">
		<table id="box-table-a" border="1">
			<thead>
				<tr>
					<th colspan="5" align="left" bgcolor="#000000">
						<font size="+1" color="#ffffff">Risultati del trasferimento</font>
                    </th>
                </tr>
				<tr>
				    <th scope="col">Esito</th>
				    <th scope="col">Anno Atto</th>
				    <th scope="col">Numero Atto</th>
				    <th scope="col">ID Evento</th>
				    <th scope="col">Estratto</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="transfer" items="${entryList}" >
					<tr>
						<td><c:out value="${transfer.esito}"/></td>
						<td><c:out value="${transfer.annoProtocollo}"/></td>
						<td><c:out value="${transfer.numeroProtocollo}"/></td>
						<td><c:out value="${transfer.idEvento}"/></td>
						<td>
						    <c:if test="${transfer.estratto && transfer.completed}">
								<a href="load?id=<c:out value="${transfer.responseId}"/>">
									<img height="25" width="25" src="images/pdf_icon.jpg"/>
								</a>
							</c:if>
						</td>
	            	</tr>
	            </c:forEach>
			</tbody>
		</table>
	</c:if>
</fieldset>