<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Badgeville online Game</title>
</head>

<body>
<h2>Player information corresponding to a particular Team</h2>
<form:form method="post" action="save.html"
	modelAttribute="multiplePlayer">
	<table>
		<tr>
			<th>Player Number</th>
			<th>Domain</th>
			<th>email</th>
			<th>display_name</th>
		</tr>
		<c:forEach items="${multiplePlayer.playerInfo}" var="team"
			varStatus="status">
			<tr>
				<td align="center">${status.count}</td>
				<td><input name="playerInfo[${status.index}].domain" id="domain"
					value="${team.domain}" /></td>
				<td><input name="playerInfo[${status.index}].email" id="email"
					value="${team.email}" /></td>
				<td><input name="playerInfo[${status.index}].display_name" id="display_name"
					value="${team.display_name}" /></td>
			</tr>
		</c:forEach>

	</table>
	<br />
	<input type="submit" value="Winner-Tournament" />


	<input type="button" value="Player Change"
		onclick="javascript:history.back()" />
	<h3>A Pretend Forum</h3>

	<p>The following buttons are placeholders for actions in a real
		forum. However, they allow us to demonstrate the basics of integrating
		with Badgeville.</p>

	<input type="button" value="Start a discussion"
		onclick="Badgeville.credit('Start a discussion')" />
	<input type="button" value="Comment"
		onclick="Badgeville.credit({verb:'comment', forum:'sports'})" />
	<input type="button" value="Reply" onclick="Badgeville.credit('Reply')" />

	<div class="bv_showcase"></div>
	<div class="bv_activities"></div>


	<script>
		window.BadgevilleAsynchInit = function() {
			

			// Identify yourself to Badgeville by providing your public Berlin API key and
			// site URL
			Badgeville.extend(Badgeville.Settings, {
				key : '05f9e6f874832f97908553f2c9efa243',
				domain : document.getElementById('domain').value
			});

			// Register the player. This tells Badgeville who should get credit for 
			// behaviors, and whose information to display in the widgets.
			Badgeville.setPlayer({
				email : document.getElementById('email').value,
				display_name : document.getElementById('display_name').value
			});
			Badgeville.ready(function() {
				Badgeville.Gears.reward(Badgeville.p().last_reward).appendTo(
						document.body);
			});

		};

		(function() {
			var s = document.createElement('script');
			s.async = true;
			s.src = 'http://sandbox.badgeville.com/v4/badgeville-current.js';
			document.body.appendChild(s);
		}());
	</script>

</form:form>
</body>
</html>