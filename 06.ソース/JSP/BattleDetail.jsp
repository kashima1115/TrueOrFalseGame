<%@ page language="java" contentType="text/html; charset=Windows-31J"
	pageEncoding="Windows-31J"%>
<%@ page import="java.util.*"%>
<%@ page import="servlet.LocationBean"%>
<%@ page import="servlet.BattleDetailBean"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�ڍ׎w������</title>
</head>
<body>
	<h1>�������ʏڍ�</h1>
	<form action="../servlet/servlet.BattleResult" method="get">

<!-- 	9�}�X���ꂼ��Ɋ��蓖�ă^�[�����������Ȃ�~ -->
<!-- 	��Ȃ灛�ɂ��邱�Ƃɂ���Đ�U��U�𔻕ʂ���B -->
		<%	int a = 0;	%>
		<%	int b = 0;	%>
		<%	int c = 0;	%>
		<%	int d = 0;	%>
		<%	int e = 0;	%>
		<%	int f = 0;	%>
		<%	int g = 0;	%>
		<%	int h = 0;	%>
		<%	int i = 0;	%>
<!-- 		list�ɂ͂P�`�X�܂łɎw�����񂪊i�[����Ă��� -->
<!-- 		list2��first�ɂ͐�U�Asecond�ɂ͌�U�̏�񂪊i�[����Ă��� -->
<!-- 		result�͎��������擾���邽�߂̊֐� -->
		<%	int first = 0;	%>
		<%	int second = 0;	%>
		<%	int nine = 0;	%>
		<%  int result = 0; %>
		<%// �������ʂ̃f�[�^���X�g���擾
			List<BattleDetailBean> detailList = (List<BattleDetailBean>) request.getAttribute("DetailList");
			// �擾�ł��Ȃ������ꍇ�A���b�Z�[�W�o��
			// �擾�ł����ꍇ�A�f�[�^���o��
			if (detailList.isEmpty()) {
				out.print("�Y���̃f�[�^�͂���܂���B");
				} else { %>
		<%	for (BattleDetailBean bn : detailList) {	%>
		<%	first++;	%>
		<%	if (first == 1) {	%>
		<table border="1">
			<tr>
				<th>��U�i���j</th>
				<th>���s</th>
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
		<br /> <br /> �|�|�|�|�|�|�|�|�|�|�|VS�|�|�|�|�|�|�|�|�|�|�| <br /> <br />
		<table border="1">
			<tr>
				<th>��U�i�~�j</th>
				<th>���s</th>
			</tr>
			<tr>
				<td><%=bn.getLogic_name()%> <%=bn.getLogic_writer()%> <%=bn.getLogic_ver()%></td>
				<td><%=bn.getResult()%></td>
			</tr>
		</table>
		<% } %>
		<%	} %>
<%	} %>
		<%// �������ʂ̃f�[�^���X�g���擾
			List<LocationBean> locationList = (List<LocationBean>) request.getAttribute("LocationList");
			// �擾�ł��Ȃ������ꍇ�A���b�Z�[�W�o��
			// �擾�ł����ꍇ�A�f�[�^���o��
			if (locationList.isEmpty()) {
				out.print("�Y���̃f�[�^�͂���܂���B");
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

�ŏI�Ֆʌ���
		<table border="1" >
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%	if (a == 1) {
					%> �� <%
						} else if (a == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (b == 1) {
					%> �� <%
						} else if (b == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (c == 1) {
					%> �� <%
						} else if (c == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
			<tr>
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%
						if (d == 1) {
					%> �� <%
						} else if (d == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (e == 1) {
					%> �� <%
						} else if (e == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (f == 1) {
					%> �� <%
						} else if (f == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
			<tr>
			<tr>
				<th width="100" height="100"><font size=100pt>
					<%
						if (g == 1) {
					%> �� <%
						} else if (g == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (h == 1) {
					%> �� <%
						} else if (h == -1) {
					%> �~ <%
						} else {
					%> �| <%
						} %>
				</font></th>
				<th width="100" height="100"><font size=100pt>
					<%
						if (i == 1) {
					%> �� <%
						} else if (i == -1) {
					%> �~ <%
						} else {
					%> �| <%
						}	%>
				</font></th>
			<tr>
		</table><br/><br/>
		<%	for (BattleDetailBean bn : detailList) {	%>
		<% result++; %>
		<%	if (result == 2) {	%>
		<table border="1">
			<tr>
				<th>������</th>
				<th>�����J�n����</th>
				<th>�����I������</th>
			</tr>
			<tr>
				<td><%=bn.getYear()%>�N<%=bn.getMonth()%>��<%=bn.getDay()%>��</td>
				<td><%=bn.getStart_hour()%>��<%=bn.getStart_min()%>��<%=bn.getStart_sec()%>�b</td>
				<td><%=bn.getEnd_hour()%>��<%=bn.getEnd_min()%>��<%=bn.getEnd_sec()%>�b</td>
			</tr>
		</table>
		<% } %>
		<%	} %><br/><br/>
		<input type="submit" value="�������ʈꗗ�ɖ߂�" />
	</form>
</body>
</html>