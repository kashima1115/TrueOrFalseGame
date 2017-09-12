<%@ page language="java" contentType="text/html; charset=Windows-31J"
	pageEncoding="Windows-31J"%>
<%@ page import="java.util.*"%>
<%@ page import="servlet.LocationBean"%>
<%@ page import="servlet.BattleDetailBean"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>詳細指し手情報</title>
</head>
<body>
	<h1>試合結果詳細</h1>
	<form action="../servlet/servlet.BattleResult" method="get">

<!-- 	9マスそれぞれに割り当てターン数が偶数なら× -->
<!-- 	奇数なら○にすることによって先攻後攻を判別する。 -->
		<%	int a = 0;	%>
		<%	int b = 0;	%>
		<%	int c = 0;	%>
		<%	int d = 0;	%>
		<%	int e = 0;	%>
		<%	int f = 0;	%>
		<%	int g = 0;	%>
		<%	int h = 0;	%>
		<%	int i = 0;	%>
<!-- 		listには１～９までに指し手情報が格納されている -->
<!-- 		list2のfirstには先攻、secondには後攻の情報が格納されている -->
<!-- 		resultは試合情報を取得するための関数 -->
		<%	int first = 0;	%>
		<%	int second = 0;	%>
		<%	int nine = 0;	%>
		<%  int result = 0; %>
		<%// 検索結果のデータリストを取得
			List<BattleDetailBean> detailList = (List<BattleDetailBean>) request.getAttribute("DetailList");
			// 取得できなかった場合、メッセージ出力
			// 取得できた場合、データを出力
			if (detailList.isEmpty()) {
				out.print("該当のデータはありません。");
				} else { %>
		<%	for (BattleDetailBean bn : detailList) {	%>
		<%	first++;	%>
		<%	if (first == 1) {	%>
		<table border="1">
			<tr>
				<th>先攻（○）</th>
				<th>勝敗</th>
			</tr>
			<tr>
				<td><%=bn.getLogic_name()%> <%=bn.getLogic_writer()%> <%=bn.getLogic_ver()%></td>
				<td><%=bn.getResult()%></td>
			</tr>
		</table>
		<%	}	%>
		<%	} %>
		<%	for (BattleDetailBean bn : detailList) {	%>
		<% second++; %>
		<%	if (second == 2) {	%>
		<br /> <br /> －－－－－－－－－－－VS－－－－－－－－－－－ <br /> <br />
		<table border="1">
			<tr>
				<th>後攻（×）</th>
				<th>勝敗</th>
			</tr>
			<tr>
				<td><%=bn.getLogic_name()%> <%=bn.getLogic_writer()%> <%=bn.getLogic_ver()%></td>
				<td><%=bn.getResult()%></td>
			</tr>
		</table>
		<% } %>
		<%	} %>
<%	} %>
		<%// 検索結果のデータリストを取得
			List<LocationBean> locationList = (List<LocationBean>) request.getAttribute("LocationList");
			// 取得できなかった場合、メッセージ出力
			// 取得できた場合、データを出力
			if (locationList.isEmpty()) {
				out.print("該当のデータはありません。");
				} else { %>

		<%	for (LocationBean bn : locationList) {%>
		<% nine++; %>
		<% if(nine <= 9){ %>
		<%	if (bn.getLocation_x() == 0 && bn.getLocation_y() == 0) { %>
		<%	if (bn.getTurn() % 2 != 0) {
							a++;
						}
						if (bn.getTurn() % 2 == 0) {
							a--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 0 && bn.getLocation_y() == 1) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							b++;
						}
						if (bn.getTurn() % 2 == 0) {
							b--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 0 && bn.getLocation_y() == 2) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							c++;
						}
						if (bn.getTurn() % 2 == 0) {
							c--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 1 && bn.getLocation_y() == 0) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							d++;
						}
						if (bn.getTurn() % 2 == 0) {
							d--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 1 && bn.getLocation_y() == 1) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							e++;
						}
						if (bn.getTurn() % 2 == 0) {
							e--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 1 && bn.getLocation_y() == 2) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							f++;
						}
						if (bn.getTurn() % 2 == 0) {
							f--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 2 && bn.getLocation_y() == 0) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							g++;
						}
						if (bn.getTurn() % 2 == 0) {
							g--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 2 && bn.getLocation_y() == 1) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							h++;
						}
						if (bn.getTurn() % 2 == 0) {
							h--;
						}
					}	%>

		<%	if (bn.getLocation_x() == 2 && bn.getLocation_y() == 2) {	%>
		<%	if (bn.getTurn() % 2 != 0) {
							i++;
						}
						if (bn.getTurn() % 2 == 0) {
							i--;
						}
					}	%>
		<% } %>
		<%	}	%>
		<%	}	%>
		<br /> <br />

最終盤面結果
		<table border="1" >
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%	if (a == 1) {
					%> ○ <%
						} else if (a == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (b == 1) {
					%> ○ <%
						} else if (b == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (c == 1) {
					%> ○ <%
						} else if (c == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
			<tr>
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%
						if (d == 1) {
					%> ○ <%
						} else if (d == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (e == 1) {
					%> ○ <%
						} else if (e == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (f == 1) {
					%> ○ <%
						} else if (f == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
			<tr>
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%
						if (g == 1) {
					%> ○ <%
						} else if (g == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (h == 1) {
					%> ○ <%
						} else if (h == -1) {
					%> × <%
						} else {
					%> － <%
						} %>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (i == 1) {
					%> ○ <%
						} else if (i == -1) {
					%> × <%
						} else {
					%> － <%
						}	%>
				</font></th>
			<tr>
		</table><br/><br/>
		<%	for (BattleDetailBean bn : detailList) {	%>
		<% result++; %>
		<%	if (result == 2) {	%>
		<table border="1">
			<tr>
				<th>試合日</th>
				<th>試合開始時間</th>
				<th>試合終了時間</th>
			</tr>
			<tr>
				<td><%=bn.getYear()%>年<%=bn.getMonth()%>月<%=bn.getDay()%>日</td>
				<td><%=bn.getStart_hour()%>時<%=bn.getStart_min()%>分<%=bn.getStart_sec()%>秒</td>
				<td><%=bn.getEnd_hour()%>時<%=bn.getEnd_min()%>分<%=bn.getEnd_sec()%>秒</td>
			</tr>
		</table>
		<% } %>
		<%	} %><br/><br/>
		<input type="submit" value="試合結果一覧に戻る" />
	</form>
</body>
</html>